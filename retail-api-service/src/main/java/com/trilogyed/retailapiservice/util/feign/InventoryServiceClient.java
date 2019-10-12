package com.trilogyed.retailapiservice.util.feign;

import com.trilogyed.retailapiservice.domain.Inventory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@FeignClient(name = "U2-inventory-service")
@RequestMapping("/inventory")
public interface InventoryServiceClient {

    @PutMapping
    public void updateInventory(@RequestBody @Valid Inventory inventory);

    @DeleteMapping("/{inventoryId}")
    public void deleteInventory(@PathVariable Integer inventoryId);

    @GetMapping("/{inventoryId}")
    public Inventory findInventoryByInventoryId(@PathVariable Integer inventoryId);

    @GetMapping
    public List<Inventory> findAllInventories();

    @GetMapping("/product/{productId}")
    public List<Inventory> findInventoriesByProductId(@PathVariable Integer productId);

    @GetMapping("/{inventoryId}/count")
    public Integer countInventoryByInventoryId(@PathVariable Integer inventoryId);

    @GetMapping("/product/{productId}/count")
    public Integer countInventoriesByProductId(@PathVariable Integer productId);

    @PutMapping("/product/{productId}/consolidation")
    public Inventory consolidateInventoryByProductId(@PathVariable Integer productId);

//    @PostMapping
//    public Inventory createInventory(@RequestBody @Valid Inventory inventory);
}
