package com.trilogyed.adminapi.service;

import com.trilogyed.adminapi.util.feign.InventoryServiceClient;
import com.trilogyed.adminapi.util.feign.InvoiceServiceClient;
import org.springframework.stereotype.Component;

@Component
public class InvoiceServiceLayer {
    private InvoiceServiceClient invoiceClient;
    private InventoryServiceClient inventoryClient;


}
