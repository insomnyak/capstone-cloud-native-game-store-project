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

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(value = SpringJUnit4ClassRunner.class)
@SpringBootTest
public class InvoiceDaoJdbcTemplateImplTest {

    @Autowired
    InvoiceDao invoiceDao;

    @Autowired
    InvoiceItemDao invoiceItemDao;

    @Before
    public void setUp() throws Exception {
        List<InvoiceItem> invoiceItems = invoiceItemDao.findAll();
        invoiceItems.forEach(invoiceItem -> invoiceItemDao.delete(invoiceItem.getInvoiceItemId()));

        List<Invoice> invoices = invoiceDao.findAll();
        invoices.forEach(invoice -> invoiceDao.delete(invoice.getInvoiceId()));
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void addGetUpdateDelete() {

        // add
        Invoice invoice = new Invoice();
        invoice.setCustomerId(1);
        invoice.setPurchaseDate(LocalDate.parse("2019-01-01"));
        invoiceDao.add(invoice);

        Invoice invoice1 = invoiceDao.find(invoice.getInvoiceId());
        assertEquals(invoice, invoice1);

        // update
        invoice.setPurchaseDate(LocalDate.parse("2019-01-05"));
        invoiceDao.update(invoice);
        invoice1 = invoiceDao.find(invoice.getInvoiceId());
        assertEquals(invoice, invoice1);

        // delete
        invoiceDao.delete(invoice.getInvoiceId());
        invoice1 = invoiceDao.find(invoice.getInvoiceId());
        assertNull(invoice1);
    }

    @Test
    public void findAll_findByCustomerId() {
        Invoice invoice = new Invoice();
        invoice.setCustomerId(1);
        invoice.setPurchaseDate(LocalDate.parse("2019-01-01"));
        invoiceDao.add(invoice);

        invoice = new Invoice();
        invoice.setCustomerId(1);
        invoice.setPurchaseDate(LocalDate.parse("2019-01-01"));
        invoiceDao.add(invoice);

        invoice = new Invoice();
        invoice.setCustomerId(2);
        invoice.setPurchaseDate(LocalDate.parse("2019-01-01"));
        invoiceDao.add(invoice);

        List<Invoice> invoices = invoiceDao.findAll();
        assertEquals(3, invoices.size());

        invoices = invoiceDao.findByCustomerId(1);
        assertEquals(2, invoices.size());

        invoices = invoiceDao.findByCustomerId(3);
        assertEquals(0, invoices.size());
    }

    @Test
    public void count_countByCustomerId() {
        Invoice invoice = new Invoice();
        invoice.setCustomerId(1);
        invoice.setPurchaseDate(LocalDate.parse("2019-01-01"));
        invoiceDao.add(invoice);

        invoice = new Invoice();
        invoice.setCustomerId(1);
        invoice.setPurchaseDate(LocalDate.parse("2019-01-01"));
        invoiceDao.add(invoice);

        invoice = new Invoice();
        invoice.setCustomerId(2);
        invoice.setPurchaseDate(LocalDate.parse("2019-01-01"));
        invoiceDao.add(invoice);

        // count by invoiceId
        Integer count = invoiceDao.count(invoice.getInvoiceId());
        assertEquals(java.util.Optional.of(1).orElse(0), count);

        // count by customerId
        count = invoiceDao.countByCustomerId(1);
        assertEquals(java.util.Optional.of(2).orElse(0), count);

        count = invoiceDao.count(3);
        assertEquals(java.util.Optional.of(0).orElse(0), count);
    }

    @Test
    public void constraints() {
        try {
            Invoice invoice = new Invoice();
            invoice.setPurchaseDate(LocalDate.parse("2019-01-01"));
            invoiceDao.add(invoice);
        } catch (DataIntegrityViolationException ignore) {}

        try {
            Invoice invoice = new Invoice();
            invoice.setCustomerId(1);
            invoiceDao.add(invoice);
        } catch (DataIntegrityViolationException ignore) {}

        List<Invoice> invoices = invoiceDao.findAll();
        assertEquals(0, invoices.size());
    }
}