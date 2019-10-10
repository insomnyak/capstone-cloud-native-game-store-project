package com.trilogyed.adminapi.service;

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
import java.time.LocalDate;
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
    public InvoiceServiceLayer(InvoiceServiceClient invoiceClient, InventoryServiceClient inventoryClient, ProductServiceClient productClient, CustomerServiceLayer csl) {
        this.invoiceClient = invoiceClient;
        this.inventoryClient = inventoryClient;
        this.productClient = productClient;
        this.csl = csl;
    }

    @Transactional
    public TotalInvoiceViewModel createInvoice(InvoiceViewModel ivm)
    {
        List<InvoiceItem> iiList = ivm.getInvoiceItemList();
        iiList.stream().forEach(invoiceItem -> {
            Inventory inventory = inventoryClient.findInventoryByInventoryId(invoiceItem.getInventoryId());
            if(invoiceItem.getQuantity()>inventory.getQuantity()) throw new CannotCreateInvoice("We do not have enough stock for this item in our inventory.");
        });
        ivm = invoiceClient.saveInvoiceViewModel(ivm);
        return buildTotalInvoiceViewModel(ivm);
    }

    /** Helper Method - Building the TotalInvoiceViewModel */
    public TotalInvoiceViewModel buildTotalInvoiceViewModel(InvoiceViewModel ivm)
    {
        TotalInvoiceViewModel tivm = new TotalInvoiceViewModel();
        tivm.setInvoiceId(ivm.getInvoiceId());
        tivm.setCustomerViewModel(csl.getCustomer(ivm.getCustomerId()));
        tivm.setPurchaseDate(ivm.getPurchaseDate());
        List<InvoiceItem>  invoiceItemList = ivm.getInvoiceItemList();
        List<InvoiceItemViewModel> iivmList = new ArrayList<>();
        final BigDecimal[] total = {new BigDecimal("0.00")};
        invoiceItemList.stream().forEach(invoiceItem -> {
            InvoiceItemViewModel iivm = buildInvoiceItemViewModel(invoiceItem);
            total[0] = iivm.getSubTotal().add(total[0]);
            iivmList.add(iivm);
        });
        tivm.setInvItemList(iivmList);
        tivm.setTotalCost(total[0]);
        return tivm;
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
