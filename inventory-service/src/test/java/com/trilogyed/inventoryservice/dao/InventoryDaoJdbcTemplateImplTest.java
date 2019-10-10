package com.trilogyed.inventoryservice.dao;

import com.trilogyed.inventoryservice.model.Inventory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(value = SpringJUnit4ClassRunner.class)
@SpringBootTest
public class InventoryDaoJdbcTemplateImplTest {

    @Autowired
    private InventoryDao dao;

    @Before
    public void setUp() throws Exception {
        List<Inventory> inventories = dao.findAll();
        inventories.forEach(inventory -> dao.delete(inventory.getInventoryId()));
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void addGetUpdate_findAll_findByProductId_Delete() {
        // addGet
        Inventory inventory = new Inventory();
        inventory.setQuantity(55);
        inventory.setProductId(1);
        dao.add(inventory);

        Inventory inventory1 = dao.find(inventory.getInventoryId());
        assertEquals(inventory, inventory1);

        // updateGet
        inventory.setQuantity(39);
        dao.update(inventory);
        inventory1 = dao.find(inventory.getInventoryId());
        assertEquals(inventory, inventory1);

        // findAll
        List<Inventory> inventories = dao.findAll();
        assertEquals(1, inventories.size());

        // findByProductId
        inventories = dao.findByProductId(1);
        assertEquals(1, inventories.size());

        inventories = dao.findByProductId(5);
        assertEquals(0, inventories.size());

        // delete
        dao.delete(inventory.getInventoryId());
        inventory1 = dao.find(inventory.getInventoryId());
        assertNull(inventory1);

    }

    @Test
    public void count_countByProductId() {
        Inventory inventory = new Inventory();
        inventory.setQuantity(55);
        inventory.setProductId(1);
        dao.add(inventory);

        Inventory inventory1 = new Inventory();
        inventory1.setQuantity(55);
        inventory1.setProductId(1);
        dao.add(inventory1);

        // count
        Integer count = dao.count(inventory.getInventoryId());
        assertEquals(java.util.Optional.of(1).orElse(0), count);

        count = dao.count(999);
        assertEquals(java.util.Optional.of(0).orElse(0), count);

        // countByProductId
        count = dao.countByProductId(inventory.getProductId());
        assertEquals(java.util.Optional.of(2).orElse(0), count);

        count = dao.countByProductId(0);
        assertEquals(java.util.Optional.of(0).orElse(0), count);
    }

    @Test
    public void testContraints() {
        try {
            Inventory inventory = new Inventory();
            inventory.setProductId(1);
            dao.add(inventory);
        } catch (DataIntegrityViolationException ignore) {}

        try {
            Inventory inventory = new Inventory();
            inventory.setQuantity(55);
            dao.add(inventory);
        } catch (DataIntegrityViolationException ignore) {}

        List<Inventory> inventories = dao.findAll();
        assertEquals(0, inventories.size());
    }
}