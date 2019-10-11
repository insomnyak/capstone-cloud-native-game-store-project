package com.trilogyed.retailapiservice.controller;

import com.trilogyed.retailapiservice.domain.*;
import com.trilogyed.retailapiservice.service.ServiceLayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class RetailApiController {

    @Autowired
    ServiceLayer sl;

    @GetMapping("/inventories")
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryViewModel> getAllInventory() {
        return sl.fetchAllInventories();
    }

    @GetMapping("/inventories/{inventoryId}")
    @ResponseStatus(HttpStatus.OK)
    public InventoryViewModel getInventoryByInventoryId(@PathVariable Integer inventoryId) {
        return sl.fetchInventoryByInventoryId(inventoryId);
    }

    @GetMapping("/products/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ProductViewModel getProductByProductId(@PathVariable Integer productId) {
        return sl.fetchProductByProductId(productId);
    }

    @GetMapping("/products/inventory")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductViewModel> getAllProductsWithInventory() {
        return sl.fetchAllProductsWithInventory();
    }

    @GetMapping("/customers/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    public Customer getCustomerByCustomerId(@PathVariable Integer customerId) {
        return sl.fetchCustomer(customerId);
    }

    @GetMapping("/customers/{customerId}/invoices")
    @ResponseStatus(HttpStatus.OK)
    public CustomerViewModel getCustomerInvoices(@PathVariable Integer customerId) {
        return sl.fetchCustomerViewModel(customerId);
    }

    @PostMapping("/order")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderViewModel createInvoice(@RequestBody @Valid OrderViewModel ovm) {
        return sl.create(ovm);
    }
}
