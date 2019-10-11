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
        return null;
    }

    @GetMapping("/inventories/{inventoryId}")
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

    // admin api only
//    @PostMapping("/customers")
//    @ResponseStatus(HttpStatus.CREATED)
//    public Customer createCustomer(@RequestBody @Valid Customer customer) {
//        return null;
//    }

    // admin api only
//    @PutMapping("/customers")
//    @ResponseStatus(HttpStatus.ACCEPTED)
//    public void updateCustomer(@RequestBody @Valid Customer customer) {
//
//    }

    // admin api only
//    @GetMapping("/customers")
//    @ResponseStatus(HttpStatus.OK)
//    public List<Customer> getAllCustomers() {
//        return null;
//    }

    @GetMapping("/customers/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    public Customer getCustomerByCustomerId(@PathVariable Integer customerId) {
        return null;
    }

    @PostMapping("/order")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderViewModel createInvoice(@RequestBody @Valid OrderViewModel ovm) {
        return sl.create(ovm);
    }
}
