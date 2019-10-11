package com.trilogyed.retailapiservice.service;

import com.insomnyak.util.MapClasses;
import com.trilogyed.queue.shared.viewmodel.LevelUpViewModel;
import com.trilogyed.retailapiservice.domain.*;
import com.trilogyed.retailapiservice.exception.EmptyInventoryException;
import com.trilogyed.retailapiservice.exception.InvalidCustomerException;
import com.trilogyed.retailapiservice.exception.InvalidItemQuantityException;
import com.trilogyed.retailapiservice.exception.TupleNotFoundException;
import com.trilogyed.retailapiservice.util.feign.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component @Primary
public class ServiceLayer {

    private CustomerServiceClient customerClient;
    private InventoryServiceClient inventoryClient;
    private InvoiceServiceClient invoiceClient;
    private LevelUpServiceClient levelUpClient;
    private ProductServiceClient productClient;

    @Autowired
    ServiceLayerRabbitMqHelper rabbitMqHelper;

    @Autowired
    public ServiceLayer(CustomerServiceClient customerClient,
                        InventoryServiceClient inventoryClient,
                        InvoiceServiceClient invoiceClient,
                        LevelUpServiceClient levelUpClient,
                        ProductServiceClient productClient) {
        this.customerClient = customerClient;
        this.inventoryClient = inventoryClient;
        this.invoiceClient = invoiceClient;
        this.levelUpClient = levelUpClient;
        this.productClient = productClient;
    }

    @Transactional
    public OrderViewModel create(OrderViewModel ovm) {
        /*
        Receive:
            OrderViewModel:
                Customer
                List<InvoiceItems>
                    - Product
                    - quantity

        Response:
            OrderViewModel:
                awardedPoints
                LevelUp
                    - levelUpId
                    - customerId
                    - points
                    - memberDate
                Customer
                invoiceId
                purchaseDate
                List<InvoiceItemViewModel>
                    - invoiceItemId
                    - invoiceId
                    - inventoryId
                    - Product
                    - quantity
                orderTotal
         */

        /*
            CHECKS
                1. quantity is 0 < qty < # items in inventory
                2. product is valid
                3. customer is valid
         */
        //"This must match the entry in the system. Use /customers/{customerId} for exact values used."

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

        /*4
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

        // set ovm invoiceId & orderTotal
        ovm.setOrderTotal(orderTotal);
        ovm.setInvoiceId(ivm.getInvoiceId());

        // determine LevelUp points
        Integer awardedPoints = Integer.parseInt(
                orderTotal.divide(new BigDecimal("50"), RoundingMode.FLOOR).toBigInteger().toString());
        ovm.setAwardedPoints(awardedPoints);

        // update levelUp
        List<LevelUp> levelUpList = levelUpClient.findLevelUpsByCustomerId(customerId);
        LevelUp levelUp;
        if (levelUpList.size() > 1) {
            levelUp = levelUpClient.consolidateLevelUpsByCustomerId(customerId);
            levelUp.setPoints(levelUp.getPoints() + awardedPoints);
            rabbitMqHelper.updateLevelUp(
                    (new MapClasses<>(levelUp, LevelUpViewModel.class)).mapFirstToSecond(false));
        } else if (levelUpList.size() == 0) {
            LevelUpViewModel luvm = new LevelUpViewModel();
            luvm.setCustomerId(customerId);
            luvm.setMemberDate(LocalDate.now());
            luvm.setPoints(awardedPoints);
            luvm = rabbitMqHelper.saveLevelUp(luvm);
            levelUp = (new MapClasses<>(luvm, LevelUp.class)).mapFirstToSecond(false);
        } else {
            levelUp = levelUpList.get(0);
            levelUp.setPoints(levelUp.getPoints() + awardedPoints);
            rabbitMqHelper.updateLevelUp(
                    (new MapClasses<>(levelUp, LevelUpViewModel.class)).mapFirstToSecond(false));
        }

        ovm.setMemberPoints(levelUp);

        return ovm;
    }


}
