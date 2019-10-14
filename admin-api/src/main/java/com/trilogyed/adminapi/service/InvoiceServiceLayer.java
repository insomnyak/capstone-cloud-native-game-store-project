package com.trilogyed.adminapi.service;

import com.sun.codemodel.internal.JForEach;
import com.trilogyed.adminapi.domain.CustomerViewModel;
import com.trilogyed.adminapi.domain.InvoiceItemViewModel;
import com.trilogyed.adminapi.domain.InvoiceViewModel;
import com.trilogyed.adminapi.domain.TotalInvoiceViewModel;
import com.trilogyed.adminapi.exception.CannotCreateInvoice;
import com.trilogyed.adminapi.model.*;
import com.trilogyed.adminapi.util.feign.InventoryServiceClient;
import com.trilogyed.adminapi.util.feign.InvoiceServiceClient;
import com.trilogyed.adminapi.util.feign.ProductServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class InvoiceServiceLayer {
    private InvoiceServiceClient invoiceClient;
    private InventoryServiceClient inventoryClient;
    private ProductServiceClient productClient;
    private CustomerServiceLayer csl;

    @Autowired
    public InvoiceServiceLayer(InvoiceServiceClient invoiceClient, InventoryServiceClient inventoryClient, ProductServiceClient productClient,CustomerServiceLayer csl) {
        this.invoiceClient = invoiceClient;
        this.inventoryClient = inventoryClient;
        this.productClient = productClient;
        this.csl = csl;
    }

    @Transactional
    public TotalInvoiceViewModel createInvoice(InvoiceViewModel ivm)
    {
        List<InvoiceItem> iiList = ivm.getInvoiceItemList();
        List<InvoiceItem> outOfStockItems = new ArrayList<>();
        iiList.stream().forEach(invoiceItem -> {
                    Inventory inventory = inventoryClient.findInventoryByInventoryId(invoiceItem.getInventoryId());
                    if (invoiceItem.getQuantity() > inventory.getQuantity()) {
                        throw new CannotCreateInvoice("One of the items in your list is out of stock. Please check the inventory.");
                    }
                    inventory.setQuantity(inventory.getQuantity()- invoiceItem.getQuantity());
                    inventoryClient.updateInventory(inventory);
                });
        ivm = invoiceClient.saveInvoiceViewModel(ivm);
        return buildTotalInvoiceViewModel(ivm);
    }

    public TotalInvoiceViewModel getInvoice(Integer invoiceId)
    {
        InvoiceViewModel ivm = invoiceClient.findInvoiceViewModelByInvoiceId(invoiceId);
        return buildTotalInvoiceViewModel(ivm);
    }

    public List<TotalInvoiceViewModel> getAllInvoices()
    {
        List<InvoiceViewModel> ivmList = invoiceClient.findAllInvoiceViewModels();
        List<TotalInvoiceViewModel> tivmList = new ArrayList<>();
        ivmList.stream().forEach(invoiceViewModel ->{
            TotalInvoiceViewModel tvm = buildTotalInvoiceViewModel(invoiceViewModel);
            tivmList.add(tvm);
        });
        return tivmList;
    }

    public List<TotalInvoiceViewModel> getInvoicesByCustomerId(Integer customerId)
    {
        List<InvoiceViewModel> ivmList = invoiceClient.findInvoiceViewModelsByCustomerId(customerId);
        List<TotalInvoiceViewModel> tivmList = new ArrayList<>();
        ivmList.stream().forEach(invoiceViewModel ->{
            TotalInvoiceViewModel tvm = buildTotalInvoiceViewModel(invoiceViewModel);
            tivmList.add(tvm);
        });
        return tivmList;
    }

    public List<InvoiceItemViewModel> getItemsByInvoiceId(Integer invoiceId)
    {
        InvoiceViewModel invoiceViewModel = invoiceClient.findInvoiceViewModelByInvoiceId(invoiceId);
        List<InvoiceItem> invoiceItemList = invoiceViewModel.getInvoiceItemList();
        List<InvoiceItemViewModel> iivmList = new ArrayList<>();
        for (InvoiceItem invoiceItem: invoiceItemList)
        {
            InvoiceItemViewModel iivm = buildInvoiceItemViewModel(invoiceItem);
            iivmList.add(iivm);
        }
        return iivmList;
    }

    public void updateInvoiceIncludingInvoiceItems(TotalInvoiceViewModel tvm)
    {
        InvoiceViewModel ivm = invoiceClient.findInvoiceViewModelByInvoiceId(tvm.getInvoiceId());
        ivm.setCustomerId(tvm.getCustomerViewModel().getCustomerId());
        ivm.setPurchaseDate(tvm.getPurchaseDate());
        List<InvoiceItemViewModel> iivmList = tvm.getInvItemList();
        List<InvoiceItem> iiList = new ArrayList<>();
        iivmList.stream().forEach(iivm -> {
            InvoiceItem invoiceItem = new InvoiceItem();
            {
                if (iivm.getInvoiceItemId()==null) invoiceItem.setInvoiceItemId(0);
                else invoiceItem.setInvoiceItemId(iivm.getInvoiceItemId());
                invoiceItem.setInvoiceId(iivm.getInvoiceId());
                invoiceItem.setInventoryId(iivm.getInventoryId());
                Inventory inventory = inventoryClient.findInventoryByInventoryId(iivm.getInventoryId());
                if(invoiceItem.getQuantity()>inventory.getQuantity()) {
                    throw new CannotCreateInvoice("One of the items in your list is out of stock. Please check the inventory.");
                }else {
                    inventory.setQuantity(inventory.getQuantity() - invoiceItem.getQuantity());
                    inventoryClient.updateInventory(inventory);
                }
                invoiceItem.setQuantity(iivm.getQuantity());
                invoiceItem.setUnitPrice(iivm.getUnitPrice());
                iiList.add(invoiceItem);
            }
        });
        ivm.setInvoiceItemList(iiList);
        invoiceClient.updateInvoiceViewModel(ivm);
        tvm = buildTotalInvoiceViewModel(ivm);
    }

    public void deleteInvoice(Integer invoiceId)
    {
        invoiceClient.deleteInvoiceViewModelByInvoiceId(invoiceId);
    }

    /** Check Quantity */
    public boolean isQuantityInStock(List<InvoiceItem> outOfStockItems)
    {
        if (outOfStockItems.size()!=0) return false;
        else return true;

    }

    /** Helper Method - Building the TotalInvoiceViewModel */
    public TotalInvoiceViewModel buildTotalInvoiceViewModel(InvoiceViewModel ivm)
    {
        if (ivm==null) return null;
        final BigDecimal[] total = {new BigDecimal("0.00")};
        TotalInvoiceViewModel tivm = new TotalInvoiceViewModel();
        tivm.setInvoiceId(ivm.getInvoiceId());
        tivm.setPurchaseDate(ivm.getPurchaseDate());
        List<InvoiceItem>  invoiceItemList = ivm.getInvoiceItemList();
        List<InvoiceItemViewModel> iivmList = new ArrayList<>();
        for(InvoiceItem invoiceItem:invoiceItemList)
        {
            InvoiceItemViewModel iivm = buildInvoiceItemViewModel(invoiceItem);
            total[0] = iivm.getSubTotal().add(total[0]);
            iivmList.add(iivm);
        }
        tivm.setInvItemList(iivmList);
        tivm.setTotalCost(total[0]);
        CustomerViewModel cvm = csl.getCustomer(ivm.getCustomerId());
        int points = calculatePoints(total[0]) + cvm.getPoints();
        cvm.setPoints(points);
        csl.updateCustomer(cvm);
        tivm.setCustomerViewModel(cvm);
        return tivm;
    }

    /** Calculate levelup points */
    public Integer calculatePoints(BigDecimal totalCost)
    {
        Integer totalIntValue = totalCost.intValue();
        Integer numberOfFifty=0;
        while(totalIntValue>49)
        {
            totalIntValue -= 50;
            numberOfFifty++;
        }
        Integer points = numberOfFifty*10;
        return points;
    }

    /** Deleting extra Inventories */
    public Inventory deleteExtraInventories(List<Inventory> inventoryList)
    {
        Comparator<Inventory> maxId = Comparator.comparing(Inventory::getInventoryId);
        Inventory maxIdInv = inventoryList.stream().max(maxId).get();
        for (Inventory inventory: inventoryList)
        {
            if(inventory.getInventoryId()!=maxIdInv.getInventoryId())
            {
                maxIdInv.setQuantity(maxIdInv.getQuantity()+inventory.getQuantity());
                inventoryClient.deleteInventory(inventory.getInventoryId());
            }
        }
        inventoryClient.updateInventory(maxIdInv);
        return maxIdInv;
    }
    /** Helper Method - Building the InvoiceItemViewModel */
    public InvoiceItemViewModel buildInvoiceItemViewModel(InvoiceItem invoiceItem)
    {
        if (invoiceItem==null) return null;
        InvoiceItemViewModel iivm = new InvoiceItemViewModel();
        iivm.setInvoiceItemId(invoiceItem.getInvoiceItemId());
        iivm.setInvoiceId(invoiceItem.getInvoiceId());
        iivm.setInventoryId(invoiceItem.getInventoryId());
        Inventory inventory = inventoryClient.findInventoryByInventoryId(invoiceItem.getInventoryId());
        Product product = productClient.findProductByProductId(inventory.getProductId());
        iivm.setProductName(product.getProductName());

        iivm.setQuantity(invoiceItem.getQuantity());
        iivm.setUnitPrice(invoiceItem.getUnitPrice());
        BigDecimal subTotal = invoiceItem.getUnitPrice().multiply(new BigDecimal(invoiceItem.getQuantity())).setScale(2);
        iivm.setSubTotal(subTotal);
        return iivm;
    }
}
