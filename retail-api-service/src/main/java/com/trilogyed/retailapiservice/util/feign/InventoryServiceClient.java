package com.trilogyed.retailapiservice.util.feign;

import com.trilogyed.retailapiservice.domain.Inventory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@FeignClient(name = "U2-INVENTORY-SERVICE", configuration = DefaultFeignClientConfiguration.class)
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
