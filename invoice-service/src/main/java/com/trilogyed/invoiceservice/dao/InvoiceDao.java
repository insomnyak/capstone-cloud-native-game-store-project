package com.trilogyed.invoiceservice.dao;

import com.insomnyak.interfaces.EntityDao;
import com.trilogyed.invoiceservice.model.Invoice;

import java.util.List;

public interface InvoiceDao {

    Invoice add(Invoice invoice);

    Invoice find(Integer invoiceId);

    void update(Invoice invoice);

    void delete(Integer invoiceId);

    List<Invoice> findAll();

    Integer count(Integer invoiceId);

    Integer countByCustomerId(Integer customerId);

    List<Invoice> findByCustomerId(Integer customerId);
}
