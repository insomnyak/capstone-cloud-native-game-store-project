package com.trilogyed.retailapiservice.service;

import com.insomnyak.util.MapClasses;
import com.netflix.client.ClientException;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.trilogyed.queue.shared.viewmodel.LevelUpViewModel;
import com.trilogyed.retailapiservice.domain.*;
import com.trilogyed.retailapiservice.exception.*;
import com.trilogyed.retailapiservice.util.feign.*;
import com.trilogyed.retailapiservice.util.rest.InventoryRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component @Primary
public class ServiceLayer {

    private CustomerServiceClient customerClient;
    private InventoryServiceClient inventoryClient;
    private InvoiceServiceClient invoiceClient;
    private LevelUpServiceClient levelUpClient;
    private ProductServiceClient productClient;

    private ServiceLayerRabbitMqHelper rabbitMqHelper;

    @Autowired
    public ServiceLayer(CustomerServiceClient customerClient,
                        InventoryServiceClient inventoryClient,
                        InvoiceServiceClient invoiceClient,
                        LevelUpServiceClient levelUpClient,
                        ProductServiceClient productClient,
                        ServiceLayerRabbitMqHelper rabbitMqHelper) {
        this.customerClient = customerClient;
        this.inventoryClient = inventoryClient;
        this.invoiceClient = invoiceClient;
        this.levelUpClient = levelUpClient;
        this.productClient = productClient;
        this.rabbitMqHelper = rabbitMqHelper;
    }

    @Transactional
    public OrderViewModel create(OrderViewModel ovm) {
        /*
            CHECKS
                1. quantity is 0 < qty < # items in inventory
                2. product is valid
                3. customer is valid
         */

        // CHECK: customer is valid
        int customerId = ovm.getCustomer().getCustomerId();
        Customer customer = customerClient.getCustomerByCustomerId(customerId);
        if (customer == null) {
            throw new TupleNotFoundException(String.format("The customerId, %d, does not match any customer" +
                    "in our records. " +
                    "If you do not have a customerId, please reach out to your Admin Professional " +
                    "so they can create one for you.", customerId));
        }

        if (!ovm.getCustomer().equals(customer)) {
            throw new InvalidCustomerException(String.format("The customer details do not match the record in the " +
                    "database. This must match the entry in the system. " +
                    "Use /customers/{customerId} for exact values used. \n" +
                    "Details Entered: %s", ovm.getCustomer().toString()));
        }

        List<Inventory> inventoryToUpdate = new ArrayList<>();
        InvoiceViewModel ivm = new InvoiceViewModel();
        ivm.setPurchaseDate(LocalDate.now());
        ivm.setInvoiceItems(new ArrayList<>());
        ivm.setCustomerId(customerId);
        List<InvoiceItem> invoiceItems = ivm.getInvoiceItems();
        BigDecimal orderTotal = new BigDecimal("0.00");

        for (InvoiceItemViewModel iivm : ovm.getInvoiceItems()) {
            // CHECK: products are valid
            int productId = iivm.getProduct().getProductId();
            Product product = productClient.findProductByProductId(productId);
            if (product == null) {
                throw new TupleNotFoundException(String.format("productId %d was not found." +
                        "To ensure you are using the correct productId, please use one of the following endpoints: " +
                        "/inventories, /products/inventory", productId));
            }
            iivm.setProduct(product);
            // CHECK quantity is valid
            List<Inventory> inventoryList = inventoryClient.findInventoriesByProductId(productId);
            Inventory inventory;
            if (inventoryList.size() > 1) {
                inventory = inventoryClient.consolidateInventoryByProductId(productId);
            } else if (inventoryList.size() == 0) {
                throw new EmptyInventoryException(String.format(
                        "There is no inventory available for productId %d", productId));
            } else {
                inventory = inventoryList.get(0);
            }
            int requestedQty = iivm.getQuantity();
            int availableQty = inventory.getQuantity();
            if (requestedQty > availableQty) {
                throw new InvalidItemQuantityException(String.format("The quantity, %d, cannot be greater than" +
                        " the available inventory, %d.", requestedQty, availableQty));
            }

            // update inventory and add to inventoryToUpdate
            inventory.setQuantity(availableQty - requestedQty);
            inventoryToUpdate.add(inventory);

            // add invoiceItem
            InvoiceItem invoiceItem = (new MapClasses<>(iivm, InvoiceItem.class)).mapFirstToSecond(false);
            invoiceItem.setUnitPrice(product.getUnitCost());
            invoiceItem.setInventoryId(inventory.getInventoryId());
            invoiceItems.add(invoiceItem);

            // set missing iivm properties
            iivm.setInventoryId(inventory.getInventoryId());
            iivm.setUnitPrice(product.getUnitCost());

            orderTotal = orderTotal.add(invoiceItem.getUnitPrice().multiply(new BigDecimal(requestedQty)));
        }

        /*
            0. update inventory
            1. create invoiceViewModel
            2. set invoiceId & invoiceItemId
            4. determine total
            5. determine LevelUp points
            6. send LevelUp points
            7. set LevelUp parameters
            8. return OrderViewModel
         */

        // update inventory
        for (Inventory inventory : inventoryToUpdate) {
            inventoryClient.updateInventory(inventory);
        }

        // create invoice & invoiceItems
        ivm = invoiceClient.saveInvoiceViewModel(ivm);
        invoiceItems = ivm.getInvoiceItems();

        // set order invoiceId & invoiceItemIds
        List<InvoiceItem> finalInvoiceItems = invoiceItems;
        ovm.getInvoiceItems().forEach(iivm -> {
            InvoiceItem invoiceItem = finalInvoiceItems.stream().filter(ii ->
                    ii.getInventoryId().equals(iivm.getInventoryId())).collect(Collectors.toList()).get(0);
            (new MapClasses<>(invoiceItem, iivm)).mapFirstToSecond(false, true);
        });

        // set ovm invoiceId, orderTotal & purchaseDate
        ovm.setOrderTotal(orderTotal);
        ovm.setInvoiceId(ivm.getInvoiceId());
        ovm.setPurchaseDate(ivm.getPurchaseDate());

        // determine LevelUp points
        Integer awardedPoints = calculateAwardedPoints(orderTotal);
        ovm.setAwardedPoints(awardedPoints);

        // update levelUp
        ovm.setMemberPoints(fetchLevelUpByCustomerId(customerId, awardedPoints));

        return ovm;
    }

    @HystrixCommand(fallbackMethod = "inventoryFallback")
    public List<InventoryViewModel> fetchAllInventories() {
        List<InventoryViewModel> ivmList = new ArrayList<>();

        List<Inventory> inventoryList = inventoryClient.findAllInventories();
        for (Inventory inventory : inventoryList) {
            InventoryViewModel ivm = build(inventory, false);
            if (ivm != null) ivmList.add(ivm);
        }

        return ivmList;
    }

    public List<InventoryViewModel> inventoryFallback() {
        throw new InventoryServiceUnavailableException("Unable to fetch inventory at the moment");
    }

    public InventoryViewModel fetchInventoryByInventoryId(Integer inventoryId) {
        Inventory inventory = inventoryClient.findInventoryByInventoryId(inventoryId);
        if (inventory == null) {
            throw new TupleNotFoundException("Inventory not found for inventoryId " + inventoryId);
        }
        InventoryViewModel ivm = build(inventory, false);
        if (ivm == null) throw new TupleNotFoundException("Inventory not found for inventoryId " + inventoryId);

        return ivm;
    }

    public ProductViewModel fetchProductByProductId(Integer productId) {
        Product product = productClient.findProductByProductId(productId);
        if (product == null) {
            throw new TupleNotFoundException("Product not found for productId " + productId);
        }

        ProductViewModel pvm =  build(product, true);

        if (pvm == null) {
            throw new ProductUnavailableException("This product has now available inventory.");
        }

        return pvm;
    }

    public List<ProductViewModel> fetchAllProductsWithInventory() {
        List<Product> productList = productClient.findAllProducts();
        List<ProductViewModel> pvmList = new ArrayList<>();

        for (Product product : productList) {
            ProductViewModel pvm = build(product, true);
            if (pvm == null) continue;
            pvmList.add(pvm);
        }

        if (pvmList.size() == 0) {
            throw new EmptyInventoryException("There are no products with available inventory.");
        }

        return pvmList;
    }

    public Customer fetchCustomer(Integer customerId) {
        Customer customer = customerClient.getCustomerByCustomerId(customerId);
        if (customer == null) {
            throw new TupleNotFoundException(String.format("customerId %d not found. " +
                    "Please contact an Admin Professional to create a valid customer profile.", customerId));
        }
        return customer;
    }

    public CustomerViewModel fetchCustomerViewModel(Integer customerId) {
        Customer customer = fetchCustomer(customerId);
        return build(customer, true);
    }

    /* ******************
        HELPER METHODS
     ****************** */

    private InventoryViewModel build(Inventory inventory, boolean ignoreNonNullsInSecond) {
        Product product = productClient.findProductByProductId(inventory.getProductId());
        if (product == null) {
            inventoryClient.deleteInventory(inventory.getInventoryId());
            return null;
        } else {
            InventoryViewModel ivm = (new MapClasses<>(inventory, InventoryViewModel.class))
                    .mapFirstToSecond(false, ignoreNonNullsInSecond);
            ivm.setProduct(product);
            return ivm;
        }
    }

    private ProductViewModel build(Product product, boolean ignoreNonNullsInSecond) {
        ProductViewModel pvm =  (new MapClasses<>(product, ProductViewModel.class))
                .mapFirstToSecond(false, ignoreNonNullsInSecond);
        //pvm.setInventory(inventoryClient.findInventoriesByProductId(product.getProductId()));
        Inventory inventory = fetchInventoryByProductId(product.getProductId());
        if (inventory == null || inventory.getQuantity() == 0) {
            return null;
        }
        (new MapClasses<>(inventory, pvm)).mapFirstToSecond(false, ignoreNonNullsInSecond);
        return pvm;
    }

    private Inventory fetchInventoryByProductId(Integer productId) {
        List<Inventory> inventoryList = inventoryClient.findInventoriesByProductId(productId);
        Inventory inventory;
        if (inventoryList.size() > 1) {
            inventory = inventoryClient.consolidateInventoryByProductId(productId);
        } else if (inventoryList.size() == 1) {
            inventory = inventoryList.get(0);
        } else {
            inventory = null;
        }

        return inventory;
    }

    private CustomerViewModel build(Customer customer, boolean ignoreNonNullsInSecond) {
        CustomerViewModel cvm = (new MapClasses<>(customer, CustomerViewModel.class))
                .mapFirstToSecond(false, ignoreNonNullsInSecond);

        LevelUp levelUp = fetchLevelUpByCustomerId(customer.getCustomerId());
        cvm.setLevelUp(levelUp);

        List<OrderViewModel> ovmList = new ArrayList<>();

        List<InvoiceViewModel> invoiceViewModelList =
                invoiceClient.findInvoiceViewModelsByCustomerId(customer.getCustomerId());

        Integer totalAwardedPoints = 0;
        for (InvoiceViewModel ivm : invoiceViewModelList) {
            OrderViewModel ovm = build(customer, ivm);
            totalAwardedPoints += ovm.getAwardedPoints();
            ovm.setMemberPoints(levelUp);
            ovmList.add(ovm);
        }

        if (totalAwardedPoints > levelUp.getPoints()) {
            levelUp.setPoints(totalAwardedPoints);
            Thread updateLevelUp = new Thread(() -> {
                rabbitMqHelper.updateLevelUp((new MapClasses<>(levelUp, LevelUpViewModel.class))
                        .mapFirstToSecond(false));
            });
            updateLevelUp.start();
        }

        cvm.setOrders(ovmList);

        return cvm;
    }

    private LevelUp fetchLevelUpByCustomerId(Integer customerId)
            throws QueueRequestTimeoutException, MicroserviceUnavailableException {
        List<LevelUp> levelUpList = levelUpClient.findLevelUpsByCustomerId(customerId);
        LevelUp levelUp;
        if (levelUpList.size() > 1) {
            LevelUpViewModel luvm = rabbitMqHelper.consolidateLevelUpsByCustomerId(customerId);
            levelUp = (new MapClasses<>(luvm, LevelUp.class)).mapFirstToSecond(true);
        } else if (levelUpList.size() == 0) {
            levelUp = null;
        } else {
            levelUp = levelUpList.get(0);
        }
        return levelUp;
    }

    private LevelUp fetchLevelUpByCustomerId(Integer customerId, Integer awardedPoints) {
        LevelUp levelUp = null;
        boolean useFallback = false;
        try {
            levelUp = fetchLevelUpByCustomerId(customerId);
            if (levelUp.getLevelUpId() == -1) {
                useFallback = true;
            }
        } catch (MicroserviceUnavailableException | QueueRequestTimeoutException e) {
            useFallback = true;
        } finally {
            try {
                if (!useFallback) {
                    if (levelUp != null) {
                        levelUp.setPoints(levelUp.getPoints() + awardedPoints);
                        rabbitMqHelper.updateLevelUp(
                                (new MapClasses<>(levelUp, LevelUpViewModel.class)).mapFirstToSecond(false));
                    } else {
                        LevelUpViewModel luvm = new LevelUpViewModel();
                        luvm.setCustomerId(customerId);
                        luvm.setMemberDate(LocalDate.now());
                        luvm.setPoints(awardedPoints);
                        luvm = rabbitMqHelper.saveLevelUp(luvm);
                        levelUp = (new MapClasses<>(luvm, LevelUp.class)).mapFirstToSecond(false);
                    }
                } else {
                    levelUp = new LevelUp() {{
                        setLevelUpId(-1);
                        setPoints(awardedPoints);
                        setCustomerId(customerId);
                        setMemberDate(LocalDate.now());
                    }};
                    rabbitMqHelper.updateLevelUpFallback(
                            (new MapClasses<>(levelUp, LevelUpViewModel.class)).mapFirstToSecond(false));
                }
            } catch (QueueRequestTimeoutException ignore) {}
        }

        return levelUp;
    }

    private OrderViewModel build(Customer customer, InvoiceViewModel ivm) {
        OrderViewModel ovm = (new MapClasses<>(ivm, OrderViewModel.class)).mapFirstToSecond(true);
        ovm.setCustomer(customer);

        List<InvoiceItem> invoiceItemList = ivm.getInvoiceItems();
        List<InvoiceItemViewModel> invoiceItemViewModelList = new ArrayList<>();

        // orderTotal
        final BigDecimal[] orderTotal = {new BigDecimal("0.00").setScale(2, RoundingMode.HALF_UP)};
        invoiceItemList.forEach(ii ->
                orderTotal[0] = orderTotal[0].add(ii.getUnitPrice().multiply(new BigDecimal(ii.getQuantity()))));
        ovm.setOrderTotal(orderTotal[0]);

        // points awarded
        Integer awardedPoints = calculateAwardedPoints(orderTotal[0]);
        ovm.setAwardedPoints(awardedPoints);

        LevelUp levelUp = null;
        try {
            levelUp = fetchLevelUpByCustomerId(customer.getCustomerId());
        } catch (MicroserviceUnavailableException | QueueRequestTimeoutException e) {
            levelUp = new LevelUp() {{
                setPoints(awardedPoints);
                setCustomerId(customer.getCustomerId());
            }};
        } finally {
            ovm.setMemberPoints(levelUp);
        }

        // invoiceItems
        for (InvoiceItem ii : invoiceItemList) {
            InvoiceItemViewModel iivm = (new MapClasses<>(ii, InvoiceItemViewModel.class)).mapFirstToSecond(false);
            Inventory inventory = inventoryClient.findInventoryByInventoryId(ii.getInventoryId());
            if (inventory != null) iivm.setProduct(productClient.findProductByProductId(inventory.getProductId()));
            invoiceItemViewModelList.add(iivm);
        }

        ovm.setInvoiceItems(invoiceItemViewModelList);

        return ovm;
    }

    private Integer calculateAwardedPoints(BigDecimal orderTotal) {
        return Integer.parseInt(
                orderTotal.divide(new BigDecimal("50"), RoundingMode.FLOOR).toBigInteger().toString())*10;
    }
}
