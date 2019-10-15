package com.trilogyed.retailapiservice.controller;

import com.trilogyed.retailapiservice.domain.*;
import com.trilogyed.retailapiservice.service.ServiceLayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RefreshScope
@CacheConfig(cacheNames = {"retailController"})
@RequestMapping("/gameStore")
public class RetailApiController {

    @Autowired
    ServiceLayer sl;

    @GetMapping("/inventory")
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryViewModel> getAllInventory() {
        return sl.fetchAllInventories();
    }

    @GetMapping("/inventory/{inventoryId}")
    @ResponseStatus(HttpStatus.OK)
    public InventoryViewModel getInventoryByInventoryId(@PathVariable Integer inventoryId) {
        return sl.fetchInventoryByInventoryId(inventoryId);
    }

    @GetMapping("/product/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ProductViewModel getProductByProductId(@PathVariable Integer productId) {
        return sl.fetchProductByProductId(productId);
    }

    @GetMapping("/product/inventory")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductViewModel> getAllProductsWithInventory() {
        return sl.fetchAllProductsWithInventory();
    }

    @Cacheable
    @GetMapping("/customer/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    public Customer getCustomerByCustomerId(@PathVariable Integer customerId) {
        return sl.fetchCustomer(customerId);
    }

    @Cacheable(condition = "#result != null")
    @GetMapping("/customer/{customerId}/invoices")
    @ResponseStatus(HttpStatus.OK)
    public CustomerViewModel getCustomerInvoices(@PathVariable Integer customerId) {
        return sl.fetchCustomerViewModel(customerId);
    }

    @CachePut(key = "#result.getInvoiceId()")
    @CacheEvict(key = "#ovm.getCustomer().getCustomerId()")
    @PostMapping("/order")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderViewModel createInvoice(@RequestBody @Valid OrderViewModel ovm) {
        return sl.create(ovm);
    }

    @CacheEvict
    @DeleteMapping("/customer/{customerId}/cache")
    public void evictCacheByCustomerId(@PathVariable Integer customerId) {}

    @CacheEvict(allEntries = true)
    @DeleteMapping("/retail-api/cache")
    public void evictCache() {};
}
