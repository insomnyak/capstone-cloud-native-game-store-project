package com.trilogyed.inventoryservice.controller;

import com.trilogyed.inventoryservice.model.Inventory;
import com.trilogyed.inventoryservice.service.ServiceLayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/inventory")
@RefreshScope
@CacheConfig(cacheNames = {"inventoryController"})
public class InventoryController {

    @Autowired
    ServiceLayer sl;

    @CachePut(key = "'id:' + #result.getInventoryId()")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Inventory createInventory(@RequestBody @Valid Inventory inventory) {
        return sl.save(inventory);
    }

    @CacheEvict(key = "'id:' + #inventory.getInventoryId()")
    @PutMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateInventory(@RequestBody @Valid Inventory inventory) {
        sl.update(inventory);
    }

    @Caching(evict = {
            @CacheEvict(key = "'id:' + #inventoryId"),
            @CacheEvict(key = "'count:' + #inventoryId")
    })
    @DeleteMapping("/{inventoryId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteInventory(@PathVariable Integer inventoryId) {
        sl.delete(inventoryId);
    }

    @Cacheable
    @GetMapping("/{inventoryId}")
    @ResponseStatus(HttpStatus.OK)
    public Inventory findInventoryByInventoryId(@PathVariable Integer inventoryId) {
        return sl.find(inventoryId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Inventory> findAllInventories() {
        return sl.findAll();
    }

    @GetMapping("/product/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Inventory> findInventoriesByProductId(@PathVariable Integer productId) {
        return sl.findByProductId(productId);
    }

    @Cacheable(key = "'count:' + #inventoryId")
    @GetMapping("/{inventoryId}/count")
    @ResponseStatus(HttpStatus.OK)
    public Integer countInventoryByInventoryId(@PathVariable Integer inventoryId) {
        return sl.count(inventoryId);
    }

    @GetMapping("/product/{productId}/count")
    @ResponseStatus(HttpStatus.OK)
    public Integer countInventoriesByProductId(@PathVariable Integer productId) {
        return sl.countByProductId(productId);
    }

    @PutMapping("/product/{productId}/consolidation")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Inventory consolidateInventoryByProductId(@PathVariable Integer productId) {
        return sl.cleanInventory(productId, null);
    }
}
