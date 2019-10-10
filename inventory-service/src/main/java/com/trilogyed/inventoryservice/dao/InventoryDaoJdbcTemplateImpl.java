package com.trilogyed.inventoryservice.dao;

import com.trilogyed.inventoryservice.model.Inventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class InventoryDaoJdbcTemplateImpl implements InventoryDao {
    private static final String INSERT_INVENTORY_SQL =
            "insert into inventory (product_id, quantity) values (?,?)";
    private static final String UPDATE_INVENTORY_SQL =
            "update inventory set product_id = ?, quantity = ? where inventory_id = ?";
    private static final String DELETE_INVENTORY_SQL =
            "delete from inventory where inventory_id = ?";
    private static final String SELECT_INVENTORY_SQL =
            "select * from inventory where inventory_id = ?";
    private static final String SELECT_ALL_INVENTORIES_SQL =
            "select * from inventory";
    private static final String COUNT_INVENTORY_SQL =
            "select count(inventory_id) from inventory where inventory_id = ?";
    private static final String COUNT_INVENTORY_BY_PRODUCT_ID_SQL =
            "select count(product_id) from inventory where product_id = ?";
    private static final String SELECT_INVENTORIES_BY_PRODUCT_ID_SQL =
            "select * from inventory where product_id = ?";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public InventoryDaoJdbcTemplateImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    @Override
    public Inventory add(Inventory inventory) {
        jdbcTemplate.update(INSERT_INVENTORY_SQL,
                inventory.getProductId(),
                inventory.getQuantity());
        Integer id = jdbcTemplate.queryForObject("select last_insert_id()", Integer.class);
        inventory.setInventoryId(id);
        return inventory;
    }

    @Override
    public Inventory find(Integer inventoryId) {
        try {
            return jdbcTemplate.queryForObject(SELECT_INVENTORY_SQL, this::mapToRowInventory, inventoryId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void update(Inventory inventory) {
        jdbcTemplate.update(UPDATE_INVENTORY_SQL,
                inventory.getProductId(),
                inventory.getQuantity(),
                inventory.getInventoryId());
    }

    @Override
    public void delete(Integer inventoryId) {
        jdbcTemplate.update(DELETE_INVENTORY_SQL, inventoryId);
    }

    @Override
    public List<Inventory> findAll() {
        return jdbcTemplate.query(SELECT_ALL_INVENTORIES_SQL, this::mapToRowInventory);
    }

    @Override
    public Integer count(Integer inventoryId) {
        return jdbcTemplate.queryForObject(COUNT_INVENTORY_SQL, Integer.class, inventoryId);
    }

    @Override
    public Integer countByProductId(Integer productId) {
        return jdbcTemplate.queryForObject(COUNT_INVENTORY_BY_PRODUCT_ID_SQL, Integer.class, productId);
    }

    @Override
    public List<Inventory> findByProductId(Integer productId) {
        return jdbcTemplate.query(SELECT_INVENTORIES_BY_PRODUCT_ID_SQL, this::mapToRowInventory, productId);
    }

    private Inventory mapToRowInventory(ResultSet rs, int rowNum) throws SQLException {
        Inventory inventory = new Inventory();
        inventory.setInventoryId(rs.getInt("inventory_id"));
        inventory.setProductId(rs.getInt("product_id"));
        inventory.setQuantity(rs.getInt("quantity"));
        return inventory;
    }

}
