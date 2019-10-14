package com.trilogyed.adminapi.util.feign;

import com.trilogyed.adminapi.model.Inventory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@FeignClient(name = "U2-inventory-service")
@RequestMapping("/inventory")
public interface InventoryServiceClient {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Inventory createInventory(@RequestBody @Valid Inventory inventory);

    @PutMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateInventory(@RequestBody @Valid Inventory inventory);

    @DeleteMapping(value = "/{inventoryId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteInventory(@PathVariable Integer inventoryId);

    @GetMapping(value = "/{inventoryId}")
    @ResponseStatus(HttpStatus.OK)
    public Inventory findInventoryByInventoryId(@PathVariable Integer inventoryId);

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Inventory> findAllInventories();

    @GetMapping("/product/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Inventory> findInventoriesByProductId(@PathVariable Integer productId);

    @GetMapping("/{inventoryId}/count")
    @ResponseStatus(HttpStatus.OK)
    public Integer countInventoryByInventoryId(@PathVariable Integer inventoryId);

    @GetMapping("/product/{productId}/count")
    @ResponseStatus(HttpStatus.OK)
    public Integer countInventoriesByProductId(@PathVariable Integer productId);

}
