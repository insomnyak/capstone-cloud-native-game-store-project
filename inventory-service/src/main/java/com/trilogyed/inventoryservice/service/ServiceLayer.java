package com.trilogyed.inventoryservice.service;

import com.trilogyed.inventoryservice.dao.InventoryDao;
import com.trilogyed.inventoryservice.model.Inventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class ServiceLayer {

    InventoryDao dao;

    @Autowired
    public ServiceLayer(InventoryDao dao) {
        this.dao = dao;
    }

    @Transactional
    public Inventory save(Inventory inventory) {
        return dao.add(inventory);
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
}
