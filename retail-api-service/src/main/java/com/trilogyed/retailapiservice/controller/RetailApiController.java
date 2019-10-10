package com.trilogyed.retailapiservice.controller;

import com.trilogyed.retailapiservice.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class RetailApiController {

    @GetMapping("/inventory")
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryViewModel> getAllInventory() {
        return null;
    }

    @GetMapping("/inventory/{inventoryId}")
    @ResponseStatus(HttpStatus.OK)
    public InventoryViewModel getInventoryByInventoryId(@PathVariable Integer inventoryId) {
        return null;
    }

    @GetMapping("/products/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ProductViewModel getProductByProductId(@PathVariable Integer productId) {
        return null;
    }

    @GetMapping("/products/inventory")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductViewModel> getAllProductsWithInventory() {
        return null;
    }

    @PostMapping("/customers")
    @ResponseStatus(HttpStatus.CREATED)
    public Customer createCustomer(@RequestBody @Valid Customer customer) {
        return null;
    }

    @PutMapping("/customers")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateCustomer(@RequestBody @Valid Customer customer) {

    }

    @GetMapping("/customers")
    @ResponseStatus(HttpStatus.OK)
    public List<CustomerViewModel> getAllCustomers() {
        return null;
    }

    @GetMapping("/customers/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    public CustomerViewModel getCustomerByCustomerId() {
        return null;
    }

    @PostMapping("/invoices")
    @ResponseStatus(HttpStatus.CREATED)
    public InvoiceViewModel createInvoice(@RequestBody @Valid InvoiceViewModel ivm) {
        return null;
    }
}
