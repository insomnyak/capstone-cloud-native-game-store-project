package com.trilogyed.adminapi.service;

import com.trilogyed.adminapi.model.Customer;
import com.trilogyed.adminapi.util.feign.InventoryServiceClient;
import com.trilogyed.adminapi.util.feign.InvoiceServiceClient;
import com.trilogyed.adminapi.util.feign.ProductServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
    
}
