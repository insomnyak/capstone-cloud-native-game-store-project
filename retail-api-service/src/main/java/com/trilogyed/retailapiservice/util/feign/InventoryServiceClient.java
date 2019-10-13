package com.trilogyed.retailapiservice.util.feign;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.ribbon.proxy.annotation.Hystrix;
import com.trilogyed.retailapiservice.domain.Inventory;
import com.trilogyed.retailapiservice.exception.CustomerServiceUnavailableException;
import com.trilogyed.retailapiservice.exception.InventoryServiceUnavailableException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@FeignClient(name = "U2-INVENTORY-SERVICE", fallback = InventoryServiceClientFallback.class)
public interface InventoryServiceClient {

    @PutMapping("/inventory")
    public void updateInventory(@RequestBody @Valid Inventory inventory);

    @DeleteMapping("/inventory/{inventoryId}")
    public void deleteInventory(@PathVariable Integer inventoryId);

    @GetMapping("/inventory/{inventoryId}")
    public Inventory findInventoryByInventoryId(@PathVariable Integer inventoryId);

    @GetMapping("/inventory")
    public List<Inventory> findAllInventories();

    @GetMapping("/inventory/product/{productId}")
    public List<Inventory> findInventoriesByProductId(@PathVariable Integer productId);

    @GetMapping("/inventory/{inventoryId}/count")
    public Integer countInventoryByInventoryId(@PathVariable Integer inventoryId);

    @GetMapping("/inventory/product/{productId}/count")
    public Integer countInventoriesByProductId(@PathVariable Integer productId);

    @PutMapping("/inventory/product/{productId}/consolidation")
    public Inventory consolidateInventoryByProductId(@PathVariable Integer productId);
}

@Component
class InventoryServiceClientFallback implements InventoryServiceClient {
    private Inventory inventory = new Inventory() {{
        setQuantity(-1);
        setInventoryId(-1);
        setProductId(-1);
    }};
    List<Inventory> inventories = new ArrayList<Inventory>() {{
        add(inventory);
    }};

    public void updateInventory(Inventory inventory) {
        throw inventoryServiceUnavailableException();
    }

    public void deleteInventory(Integer inventoryId) {
        throw inventoryServiceUnavailableException();
    }

    public Inventory findInventoryByInventoryId(Integer inventoryId) {
        return inventory;
    }

    public List<Inventory> findAllInventories() {
        return inventories;
    }

    public List<Inventory> findInventoriesByProductId(Integer productId) {
        return inventories;
    }

    public Integer countInventoryByInventoryId(Integer inventoryId) {
        return -1;
    }

    public Integer countInventoriesByProductId(Integer productId) {
        return -1;
    }

    public Inventory consolidateInventoryByProductId(Integer productId) {
        return inventory;
    }

    private InventoryServiceUnavailableException inventoryServiceUnavailableException() {
        throw new InventoryServiceUnavailableException("Inventory cannot be retrieved or processed at the moment.");
    }
}
