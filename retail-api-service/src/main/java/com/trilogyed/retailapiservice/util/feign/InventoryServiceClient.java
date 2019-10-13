package com.trilogyed.retailapiservice.util.feign;

import com.trilogyed.retailapiservice.domain.Inventory;
import com.trilogyed.retailapiservice.exception.CustomerServiceUnavailableException;
import com.trilogyed.retailapiservice.exception.InventoryServiceUnavailableException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    public void updateInventory(@Valid Inventory inventory) {
        throw inventoryServiceUnavailableException();
    }

    public void deleteInventory(Integer inventoryId) {
        throw inventoryServiceUnavailableException();
    }

    public Inventory findInventoryByInventoryId(Integer inventoryId) {
        throw inventoryServiceUnavailableException();
    }

    public List<Inventory> findAllInventories() {
        throw inventoryServiceUnavailableException();
    }

    public List<Inventory> findInventoriesByProductId(Integer productId) {
        throw inventoryServiceUnavailableException();
    }

    public Integer countInventoryByInventoryId(Integer inventoryId) {
        throw inventoryServiceUnavailableException();
    }

    public Integer countInventoriesByProductId(Integer productId) {
        throw inventoryServiceUnavailableException();
    }

    public Inventory consolidateInventoryByProductId(Integer productId) {
        throw inventoryServiceUnavailableException();
    }

    private InventoryServiceUnavailableException inventoryServiceUnavailableException() {
        throw new InventoryServiceUnavailableException("Inventory cannot be retrieved or processed at the moment.");
    }
}
