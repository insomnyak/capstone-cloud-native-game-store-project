package com.trilogyed.invoiceservice.dao;

import com.trilogyed.invoiceservice.model.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Repository
public class InvoiceDaoJdbcTemplateImpl implements InvoiceDao {

    private static final String INSERT_INVOICE_SQL =
            "insert into invoice (customer_id, purchase_date) values (?,?)";
    private static final String UPDATE_INVOICE_SQL =
            "update invoice set customer_id = ?, purchase_date = ? where invoice_id = ?";
    private static final String SELECT_INVOICE_SQL =
            "select * from invoice where invoice_id = ?";
    private static final String SELECT_ALL_INVOICES_SQL =
            "select * from invoice";
    private static final String DELETE_INVOICE_SQL =
            "delete from invoice where invoice_id = ?";
    private static final String COUNT_INVOICE_SQL =
            "select count(invoice_id) from invoice where invoice_id = ?";
    private static final String COUNT_INVOICES_BY_CUSTOMER_ID_SQL =
            "select count(customer_id) from invoice where customer_id = ?";
    private static final String SELECT_INVOICES_BY_CUSTOMER_ID_SQL =
            "select * from invoice where customer_id = ?";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public InvoiceDaoJdbcTemplateImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Invoice add(Invoice invoice) {
        jdbcTemplate.update(INSERT_INVOICE_SQL,
                invoice.getCustomerId(),
                invoice.getPurchaseDate());
        Integer id = jdbcTemplate.queryForObject("select last_insert_id()", Integer.class);
        invoice.setInvoiceId(id);
        return invoice;
    }

    @Override
    public Invoice find(Integer invoiceId) {
        try {
            return jdbcTemplate.queryForObject(SELECT_INVOICE_SQL, this::mapToRowInvoice, invoiceId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void update(Invoice invoice) {
        jdbcTemplate.update(UPDATE_INVOICE_SQL,
                invoice.getCustomerId(),
                invoice.getPurchaseDate(),
                invoice.getInvoiceId());
    }

    @Override
    public void delete(Integer invoiceId) {
        jdbcTemplate.update(DELETE_INVOICE_SQL, invoiceId);
    }

    @Override
    public List<Invoice> findAll() {
        return jdbcTemplate.query(SELECT_ALL_INVOICES_SQL, this::mapToRowInvoice);
    }

    @Override
    public Integer count(Integer invoiceId) {
        return jdbcTemplate.queryForObject(COUNT_INVOICE_SQL, Integer.class, invoiceId);
    }

    @Override
    public Integer countByCustomerId(Integer customerId) {
        return jdbcTemplate.queryForObject(COUNT_INVOICES_BY_CUSTOMER_ID_SQL, Integer.class, customerId);
    }

    @Override
    public List<Invoice> findByCustomerId(Integer customerId) {
        return jdbcTemplate.query(SELECT_INVOICES_BY_CUSTOMER_ID_SQL, this::mapToRowInvoice, customerId);
    }

    private Invoice mapToRowInvoice(ResultSet rs, int numRow) throws SQLException {
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(rs.getInt("invoice_id"));
        invoice.setCustomerId(rs.getInt("customer_id"));
        invoice.setPurchaseDate(LocalDate.parse(rs.getString("purchase_date")));
        return invoice;
    }
}
