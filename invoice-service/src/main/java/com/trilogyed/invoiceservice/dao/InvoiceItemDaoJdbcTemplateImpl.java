package com.trilogyed.invoiceservice.dao;

import com.trilogyed.invoiceservice.model.InvoiceItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class InvoiceItemDaoJdbcTemplateImpl implements InvoiceItemDao {

    private static final String INSERT_INVOICE_ITEM_SQL =
            "insert into invoice_item (invoice_id, inventory_id, quantity, unit_price) values (?,?,?,?)";
    private static final String UPDATE_INVOICE_ITEM_SQL =
            "update invoice_item set invoice_id = ?, inventory_id = ?, quantity = ?, " +
                    "unit_price = ? where invoice_item_id = ?";
    private static final String SELECT_INVOICE_ITEM_SQL =
            "select * from invoice_item where invoice_item_id = ?";
    private static final String SELECT_ALL_INVOICE_ITEMS_SQL =
            "select * from invoice_item";
    private static final String DELETE_INVOICE_ITEM_SQL =
            "delete from invoice_item where invoice_item_id = ?";
    private static final String COUNT_INVOICE_ITEM_SQL =
            "select count(invoice_item_id) from invoice_item where invoice_item_id = ?";
    private static final String SELECT_INVOICE_ITEMS_BY_INVOICE_ID_SQL =
            "select * from invoice_item where invoice_id = ?";
    private static final String SELECT_INVOICE_ITEMS_BY_INVENTORY_ID_SQL =
            "select * from invoice_item where inventory_id = ?";
    private static final String DELETE_INVOICE_ITEMS_BY_INVOICE_ID_SQL =
            "delete from invoice_item where invoice_id = ?";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public InvoiceItemDaoJdbcTemplateImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public InvoiceItem add(InvoiceItem invoiceItem) {
        jdbcTemplate.update(INSERT_INVOICE_ITEM_SQL,
                invoiceItem.getInvoiceId(),
                invoiceItem.getInventoryId(),
                invoiceItem.getQuantity(),
                invoiceItem.getUnitPrice());
        Integer id = jdbcTemplate.queryForObject("select last_insert_id()", Integer.class);
        invoiceItem.setInvoiceItemId(id);
        return invoiceItem;
    }

    @Override
    public InvoiceItem find(Integer invoiceItemId) {
        try {
            return jdbcTemplate.queryForObject(SELECT_INVOICE_ITEM_SQL, this::mapRowToInvoiceItem, invoiceItemId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void update(InvoiceItem invoiceItem) {
        jdbcTemplate.update(UPDATE_INVOICE_ITEM_SQL,
                invoiceItem.getInvoiceId(),
                invoiceItem.getInventoryId(),
                invoiceItem.getQuantity(),
                invoiceItem.getUnitPrice(),
                invoiceItem.getInvoiceItemId());
    }

    @Override
    public void delete(Integer invoiceItemId) {
        jdbcTemplate.update(DELETE_INVOICE_ITEM_SQL, invoiceItemId);
    }

    @Override
    public List<InvoiceItem> findAll() {
        return jdbcTemplate.query(SELECT_ALL_INVOICE_ITEMS_SQL, this::mapRowToInvoiceItem);
    }

    @Override
    public Integer count(Integer invoiceItemId) {
        return jdbcTemplate.queryForObject(COUNT_INVOICE_ITEM_SQL, Integer.class, invoiceItemId);
    }

    @Override
    public List<InvoiceItem> findByInvoiceId(Integer invoiceId) {
        return jdbcTemplate.query(SELECT_INVOICE_ITEMS_BY_INVOICE_ID_SQL, this::mapRowToInvoiceItem, invoiceId);
    }

    @Override
    public List<InvoiceItem> findByInventoryId(Integer inventoryId) {
        return jdbcTemplate.query(SELECT_INVOICE_ITEMS_BY_INVENTORY_ID_SQL, this::mapRowToInvoiceItem, inventoryId);
    }

    @Override
    public void deleteByInvoiceId(Integer invoiceId) {
        jdbcTemplate.update(DELETE_INVOICE_ITEMS_BY_INVOICE_ID_SQL, invoiceId);
    }

    private InvoiceItem mapRowToInvoiceItem(ResultSet rs, int rowNum) throws SQLException {
        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setInvoiceItemId(rs.getInt("invoice_item_id"));
        invoiceItem.setInvoiceId(rs.getInt("invoice_id"));
        invoiceItem.setInventoryId(rs.getInt("inventory_id"));
        invoiceItem.setQuantity(rs.getInt("quantity"));
        invoiceItem.setUnitPrice(rs.getBigDecimal("unit_price"));
        return invoiceItem;
    }
}
