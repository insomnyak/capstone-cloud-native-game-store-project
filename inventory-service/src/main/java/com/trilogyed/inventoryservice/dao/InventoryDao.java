package com.trilogyed.inventoryservice.dao;

import com.insomnyak.interfaces.EntityDao;
import com.trilogyed.inventoryservice.model.Inventory;

import java.util.List;

public interface InventoryDao {

    Inventory add(Inventory inventory);

    Inventory find(Integer inventoryId);

    void update(Inventory inventory);

    void delete(Integer inventoryId);

    List<Inventory> findAll();

    Integer count(Integer inventoryId);

    Integer countByProductId(Integer productId);
    List<Inventory> findByProductId(Integer productId);

}
