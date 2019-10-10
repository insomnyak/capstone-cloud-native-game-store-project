package com.trilogyed.invoiceservice.dao;

import com.insomnyak.interfaces.EntityDao;
import com.trilogyed.invoiceservice.model.InvoiceItem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface InvoiceItemDao {

    InvoiceItem add(InvoiceItem invoiceItem);

    InvoiceItem find(Integer invoiceItemId);

    void update(InvoiceItem invoiceItem);

    void delete(Integer invoiceItemId);

    List<InvoiceItem> findAll();

    Integer count(Integer invoiceItemId);

    List<InvoiceItem> findByInvoiceId(Integer invoiceId);

    List<InvoiceItem> findByInventoryId(Integer inventoryId);

    void deleteByInvoiceId(Integer invoiceId);

}
