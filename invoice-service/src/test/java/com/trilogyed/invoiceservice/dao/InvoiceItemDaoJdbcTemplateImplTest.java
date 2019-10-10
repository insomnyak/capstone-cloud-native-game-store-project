package com.trilogyed.invoiceservice.dao;

import com.trilogyed.invoiceservice.model.Invoice;
import com.trilogyed.invoiceservice.model.InvoiceItem;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(value = SpringJUnit4ClassRunner.class)
@SpringBootTest
public class InvoiceItemDaoJdbcTemplateImplTest {

    @Autowired
    InvoiceDao invoiceDao;

    @Autowired
    InvoiceItemDao invoiceItemDao;

    private Invoice invoice;

    @Before
    public void setUp() throws Exception {
        List<InvoiceItem> invoiceItems = invoiceItemDao.findAll();
        invoiceItems.forEach(invoiceItem -> invoiceItemDao.delete(invoiceItem.getInvoiceItemId()));

        List<Invoice> invoices = invoiceDao.findAll();
        invoices.forEach(invoice -> invoiceDao.delete(invoice.getInvoiceId()));

        invoice = new Invoice();
        invoice.setCustomerId(1);
        invoice.setPurchaseDate(LocalDate.parse("2019-01-01"));
        invoiceDao.add(invoice);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void addGetUpdateDelete() {
        // add
        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setInvoiceId(invoice.getInvoiceId());
        invoiceItem.setUnitPrice(new BigDecimal("4.55"));
        invoiceItem.setQuantity(55);
        invoiceItem.setInventoryId(4);
        invoiceItemDao.add(invoiceItem);

        InvoiceItem invoiceItem1 = invoiceItemDao.find(invoiceItem.getInvoiceItemId());
        assertEquals(invoiceItem, invoiceItem1);

        // update
        invoiceItem.setQuantity(44);
        invoiceItemDao.update(invoiceItem);
        invoiceItem1 = invoiceItemDao.find(invoiceItem.getInvoiceItemId());
        assertEquals(invoiceItem, invoiceItem1);

        // delete
        invoiceItemDao.delete(invoiceItem.getInvoiceItemId());
        invoiceItem1 = invoiceItemDao.find(invoiceItem.getInvoiceItemId());
        assertNull(invoiceItem1);
    }

    @Test
    public void findAll_findByInvoiceId_findByInventoryId() {
        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setInvoiceId(invoice.getInvoiceId());
        invoiceItem.setUnitPrice(new BigDecimal("4.55"));
        invoiceItem.setQuantity(55);
        invoiceItem.setInventoryId(4);
        invoiceItemDao.add(invoiceItem);

        List<InvoiceItem> invoiceItems = invoiceItemDao.findAll();
        assertEquals(1, invoiceItems.size());

        invoiceItems = invoiceItemDao.findByInvoiceId(invoice.getInvoiceId());
        assertEquals(1, invoiceItems.size());

        invoiceItems = invoiceItemDao.findByInvoiceId(9999);
        assertEquals(0, invoiceItems.size());

        invoiceItems = invoiceItemDao.findByInventoryId(4);
        assertEquals(1, invoiceItems.size());

        invoiceItems = invoiceItemDao.findByInventoryId(9999);
        assertEquals(0, invoiceItems.size());
    }

    @Test
    public void count() {
        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setInvoiceId(invoice.getInvoiceId());
        invoiceItem.setUnitPrice(new BigDecimal("4.55"));
        invoiceItem.setQuantity(55);
        invoiceItem.setInventoryId(4);
        invoiceItemDao.add(invoiceItem);

        Integer count = invoiceItemDao.count(invoiceItem.getInvoiceItemId());
        assertEquals(java.util.Optional.of(1).orElse(0), count);

        count = invoiceItemDao.count(9999);
        assertEquals(java.util.Optional.of(0).orElse(0), count);
    }

    @Test
    public void contraints() {
        try {
            InvoiceItem invoiceItem = new InvoiceItem();
            invoiceItem.setUnitPrice(new BigDecimal("4.55"));
            invoiceItem.setQuantity(55);
            invoiceItem.setInventoryId(4);
            invoiceItemDao.add(invoiceItem);
        } catch (DataIntegrityViolationException e) {}

        try {
            InvoiceItem invoiceItem = new InvoiceItem();
            invoiceItem.setInvoiceId(invoice.getInvoiceId());
            invoiceItem.setQuantity(55);
            invoiceItem.setInventoryId(4);
            invoiceItemDao.add(invoiceItem);
        } catch (DataIntegrityViolationException e) {}

        try {
            InvoiceItem invoiceItem = new InvoiceItem();
            invoiceItem.setInvoiceId(invoice.getInvoiceId());
            invoiceItem.setUnitPrice(new BigDecimal("4.55"));
            invoiceItem.setInventoryId(4);
            invoiceItemDao.add(invoiceItem);
        } catch (DataIntegrityViolationException e) {}

        try {
            InvoiceItem invoiceItem = new InvoiceItem();
            invoiceItem.setInvoiceId(invoice.getInvoiceId());
            invoiceItem.setUnitPrice(new BigDecimal("4.55"));
            invoiceItem.setQuantity(55);
            invoiceItemDao.add(invoiceItem);
        } catch (DataIntegrityViolationException e) {}

        List<InvoiceItem> invoiceItems = invoiceItemDao.findAll();
        assertEquals(0, invoiceItems.size());
    }
}