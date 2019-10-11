package com.trilogyed.inventoryservice.service;

import com.trilogyed.inventoryservice.dao.InventoryDao;
import com.trilogyed.inventoryservice.model.Inventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

@Component
public class ServiceLayer {

    InventoryDao dao;

    @Autowired
    public ServiceLayer(InventoryDao dao) {
        this.dao = dao;
    }

    @Transactional
    public Inventory save(Inventory inventory) {
        // check if productId exists
        // add inventory quantity to existing entry
        // if more than one productId exists, combine entries
        Integer count = dao.countByProductId(inventory.getProductId());
        if (count > 1) {
            return cleanInventory(inventory.getProductId(), inventory.getQuantity());
        } else if (count == 1) {
            Inventory existingInventory = dao.findByProductId(inventory.getProductId()).get(0);
            existingInventory.setQuantity(existingInventory.getQuantity() + inventory.getQuantity());
            dao.update(existingInventory);
            return existingInventory;
        } else {
            return dao.add(inventory);
        }
    }

    public void update(Inventory inventory) {
        dao.update(inventory);
    }

    public void delete(Integer inventoryId) {
        dao.delete(inventoryId);
    }

    public Inventory find(Integer inventoryId) {
        return dao.find(inventoryId);
    }

    public List<Inventory> findAll() {
        return dao.findAll();
    }

    public List<Inventory> findByProductId(Integer productId) {
        return dao.findByProductId(productId);
    }

    public Integer count(Integer inventoryId) {
        return dao.count(inventoryId);
    }

    public Integer countByProductId(Integer productId) {
        return dao.countByProductId(productId);
    }

    @Transactional
    public Inventory cleanInventory(Integer productId, Integer quantity) {
        if (quantity == null) quantity = 0;

        List<Inventory> inventories = dao.findByProductId(productId);
        if (inventories.size() <= 1) return null;

        List<Integer> inventoryIdsToRemove = new ArrayList<>();
        Integer totalQuantity = 0;
        Integer inventoryIdToKeep = null;
        Inventory newInventory = null;
        for (Inventory inventory : inventories) {
            totalQuantity += inventory.getQuantity();
            if (inventoryIdToKeep == null) {
                inventoryIdToKeep = inventory.getInventoryId();
                newInventory = inventory;
            } else {
                inventoryIdsToRemove.add(inventory.getInventoryId());
            }
        }

        // consolidate multiple Inventories for productId
        newInventory.setQuantity(totalQuantity);
        newInventory.setInventoryId(inventoryIdToKeep);
        newInventory.setProductId(productId);
        dao.update(newInventory);

        // delete duplicates
        inventoryIdsToRemove.forEach(id -> dao.delete(id));

        return newInventory;
    }
}
