package com.trilogyed.invoiceservice.service;

import com.trilogyed.invoiceservice.dao.InvoiceDao;
import com.trilogyed.invoiceservice.dao.InvoiceDaoJdbcTemplateImpl;
import com.trilogyed.invoiceservice.dao.InvoiceItemDao;
import com.trilogyed.invoiceservice.dao.InvoiceItemDaoJdbcTemplateImpl;
import com.trilogyed.invoiceservice.model.Invoice;
import com.trilogyed.invoiceservice.model.InvoiceItem;
import com.trilogyed.invoiceservice.viewmodel.InvoiceViewModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceLayerTest {

    private InvoiceDao invoiceDao;
    private InvoiceItemDao invoiceItemDao;

    private ServiceLayer sl;

    private Invoice invoice1a;
    private Invoice invoice1b;
    private Invoice invoice2a;
    private Invoice invoice2b;
    private Invoice invoice2c;

    private InvoiceItem invoiceItem1a;
    private InvoiceItem invoiceItem1b;
    private InvoiceItem invoiceItem2a;
    private InvoiceItem invoiceItem2b;
    private InvoiceItem invoiceItem2c;
    private InvoiceItem invoiceItem2a2;
    private InvoiceItem invoiceItem2b2;
    private InvoiceItem invoiceItem2c2;

    private InvoiceViewModel ivm1a;
    private InvoiceViewModel ivm1b;
    private InvoiceViewModel ivm2a;
    private InvoiceViewModel ivm2b;
    private InvoiceViewModel ivm2c;

    @Before
    public void setUp() throws Exception {
        constructSampleData();
        setUpInvoiceDaoMock();
        setUpInvoiceItemDaoMock();
        sl = new ServiceLayer(invoiceDao, invoiceItemDao);
    }

    @After
    public void tearDown() throws Exception {
        destroySampleData();
    }

    public void constructSampleData() {
        invoice1a = new Invoice();
        invoice1b = new Invoice();
        invoice2a = new Invoice();
        invoice2b = new Invoice();
        invoice2c = new Invoice();

        invoiceItem1a = new InvoiceItem();
        invoiceItem1b = new InvoiceItem();
        invoiceItem2a = new InvoiceItem();
        invoiceItem2b = new InvoiceItem();
        invoiceItem2c = new InvoiceItem();
        invoiceItem2a2 = new InvoiceItem();
        invoiceItem2b2 = new InvoiceItem();
        invoiceItem2c2 = new InvoiceItem();

        ivm1a = new InvoiceViewModel();
        ivm1b = new InvoiceViewModel();
        ivm2a = new InvoiceViewModel();
        ivm2b = new InvoiceViewModel();
        ivm2c = new InvoiceViewModel();

        invoice1a.setPurchaseDate(LocalDate.parse("2019-01-01"));
        invoice1a.setCustomerId(10);

        invoice1b.setPurchaseDate(LocalDate.parse("2019-01-01"));
        invoice1b.setCustomerId(10);
        invoice1b.setInvoiceId(1);

        invoice2a.setPurchaseDate(LocalDate.parse("2019-02-02"));
        invoice2a.setCustomerId(20);

        invoice2b.setPurchaseDate(LocalDate.parse("2019-02-02"));
        invoice2b.setCustomerId(20);
        invoice2b.setInvoiceId(2);

        invoice2c.setPurchaseDate(LocalDate.parse("2019-02-22"));
        invoice2c.setCustomerId(20);
        invoice2c.setInvoiceId(2);

        invoiceItem1a.setInvoiceId(1);
        invoiceItem1a.setInventoryId(11);
        invoiceItem1a.setQuantity(15);
        invoiceItem1a.setUnitPrice(new BigDecimal("42.55"));

        invoiceItem1b.setInvoiceId(1);
        invoiceItem1b.setInventoryId(11);
        invoiceItem1b.setQuantity(15);
        invoiceItem1b.setUnitPrice(new BigDecimal("42.55"));
        invoiceItem1b.setInvoiceItemId(1);

        invoiceItem2a.setInvoiceId(2);
        invoiceItem2a.setInventoryId(22);
        invoiceItem2a.setQuantity(25);
        invoiceItem2a.setUnitPrice(new BigDecimal("42.55"));

        invoiceItem2b.setInvoiceId(2);
        invoiceItem2b.setInventoryId(22);
        invoiceItem2b.setQuantity(25);
        invoiceItem2b.setUnitPrice(new BigDecimal("42.55"));
        invoiceItem2b.setInvoiceItemId(2);

        invoiceItem2c.setInvoiceId(2);
        invoiceItem2c.setInventoryId(22);
        invoiceItem2c.setQuantity(14);
        invoiceItem2c.setUnitPrice(new BigDecimal("41.99"));
        invoiceItem2c.setInvoiceItemId(2);

        invoiceItem2a2.setInvoiceId(2);
        invoiceItem2a2.setInventoryId(33);
        invoiceItem2a2.setQuantity(35);
        invoiceItem2a2.setUnitPrice(new BigDecimal("43.55"));

        invoiceItem2b2.setInvoiceId(2);
        invoiceItem2b2.setInventoryId(33);
        invoiceItem2b2.setQuantity(35);
        invoiceItem2b2.setUnitPrice(new BigDecimal("43.55"));
        invoiceItem2b2.setInvoiceItemId(3);

        invoiceItem2c2.setInvoiceId(2);
        invoiceItem2c2.setInventoryId(33);
        invoiceItem2c2.setQuantity(35);
        invoiceItem2c2.setUnitPrice(new BigDecimal("43.55"));
        invoiceItem2c2.setInvoiceItemId(3);

        ivm1a.setCustomerId(invoice1a.getCustomerId());
        ivm1a.setPurchaseDate(invoice1a.getPurchaseDate());
        List<InvoiceItem> invoiceItems1 = new ArrayList<>();
        invoiceItems1.add(invoiceItem1a);
        ivm1a.setInvoiceItems(invoiceItems1);

        ivm2a.setCustomerId(invoice2a.getCustomerId());
        ivm2a.setPurchaseDate(invoice2a.getPurchaseDate());
        List<InvoiceItem> invoiceItems2 = new ArrayList<>();
        invoiceItems2.add(invoiceItem2a);
        ivm2a.setInvoiceItems(invoiceItems2);
    }

    public void destroySampleData() {
        invoice1a = null;
        invoice1b = null;
        invoice2a = null;
        invoice2b = null;
        invoice2c = null;

        invoiceItem1a = null;
        invoiceItem1b = null;
        invoiceItem2a = null;
        invoiceItem2b = null;
        invoiceItem2c = null;
        invoiceItem2a2 = null;
        invoiceItem2b2 = null;
        invoiceItem2c2 = null;

        ivm1a = null;
        ivm1b = null;
        ivm2a = null;
        ivm2b = null;
        ivm2c = null;
    }

    public void setUpInvoiceDaoMock() {
        invoiceDao = mock(InvoiceDaoJdbcTemplateImpl.class);

        List<Invoice> list1 = new ArrayList<>();
        list1.add(invoice1b);

        List<Invoice> list2 = new ArrayList<>();
        list2.add(invoice2c);

        doReturn(invoice1b).when(invoiceDao).add(invoice1a);
        doReturn(invoice1b).when(invoiceDao).find(1);
        doReturn(list1).when(invoiceDao).findByCustomerId(10);
        doReturn(list1).when(invoiceDao).findAll();
        doReturn(1).when(invoiceDao).count(1);
        doReturn(1).when(invoiceDao).countByCustomerId(10);

        doReturn(invoice2b).when(invoiceDao).add(invoice2a);
        doNothing().when(invoiceDao).update(invoice2c);
        doReturn(invoice2c).when(invoiceDao).find(2);
        doReturn(list2).when(invoiceDao).findByCustomerId(20);
        doReturn(1).when(invoiceDao).count(2);
        doReturn(1).when(invoiceDao).countByCustomerId(20);

        doNothing().when(invoiceDao).delete(5);
        doReturn(null).when(invoiceDao).find(5);
        doReturn(new ArrayList<>()).when(invoiceDao).findByCustomerId(50);
        doReturn(0).when(invoiceDao).count(5);
        doReturn(0).when(invoiceDao).countByCustomerId(50);
    }

    public void setUpInvoiceItemDaoMock() {
        invoiceItemDao = mock(InvoiceItemDaoJdbcTemplateImpl.class);

        List<InvoiceItem> list1 = new ArrayList<>();
        list1.add(invoiceItem1b);

        List<InvoiceItem> list2 = new ArrayList<>();
        list2.add(invoiceItem2b);

        List<InvoiceItem> list2b = new ArrayList<>();
        list2b.add(invoiceItem2c);
        list2b.add(invoiceItem2c2);

        doReturn(invoiceItem1b).when(invoiceItemDao).add(invoiceItem1a);
        doReturn(invoiceItem1b).when(invoiceItemDao).find(1);
        doReturn(list1).when(invoiceItemDao).findByInvoiceId(1);
        doReturn(list1).when(invoiceItemDao).findAll();
        doReturn(1).when(invoiceItemDao).count(1);
        doReturn(list1).when(invoiceItemDao).findByInventoryId(11);

        doReturn(invoiceItem2b).when(invoiceItemDao).add(invoiceItem2a);
        doNothing().when(invoiceItemDao).update(invoiceItem2c);
        doReturn(invoiceItem2c).when(invoiceItemDao).find(2);

        doReturn(invoiceItem2b2).when(invoiceItemDao).add(invoiceItem2a2);
        doNothing().when(invoiceItemDao).update(invoiceItem2c2);
        doReturn(invoiceItem2c2).when(invoiceItemDao).find(3);

        doReturn(list2b).when(invoiceItemDao).findByInvoiceId(2);
        doReturn(list2).when(invoiceItemDao).findByInventoryId(22);
        doReturn(1).when(invoiceItemDao).count(2);
        doReturn(1).when(invoiceItemDao).count(3);

        doNothing().when(invoiceItemDao).delete(5);
        doNothing().when(invoiceItemDao).deleteByInvoiceId(5);
        doReturn(null).when(invoiceItemDao).find(5);
        doReturn(new ArrayList<>()).when(invoiceItemDao).findByInvoiceId(5);
    }

    @Test
    public void saveGet_findAll_findByCustomerId_countMethods() {
        InvoiceViewModel ivm = sl.save(ivm1a);
        InvoiceViewModel ivm1 = sl.findInvoiceViewModelByInvoiceId(ivm.getInvoiceId());
        assertEquals(ivm, ivm1);

        // findAll
        List<InvoiceViewModel> ivmList = sl.findAllInvoiceViewModels();
        assertEquals(1, ivmList.size());
        assertEquals(ivm, ivmList.get(0));

        // findByCustomerId
        ivmList = sl.findInvoiceViewModelsByCustomerId(ivm.getCustomerId());
        assertEquals(1, ivmList.size());
        assertEquals(ivm, ivmList.get(0));

        // countByInvoiceId
        Integer count = sl.countInvoiceByInvoiceId(ivm.getInvoiceId());
        assertEquals(java.util.Optional.of(1).orElse(0), count);

        // countByCustomerId
        count = sl.countInvoicesByCustomerId(ivm.getCustomerId());
        assertEquals(java.util.Optional.of(1).orElse(0), count);
    }

    @Test
    public void updateGet() {
        InvoiceViewModel ivm = sl.save(ivm2a);
        ivm.setPurchaseDate(LocalDate.parse("2019-02-22"));

        InvoiceItem item1 = ivm.getInvoiceItems().get(0);
        item1.setQuantity(14);
        item1.setUnitPrice(new BigDecimal("41.99"));

        ivm.getInvoiceItems().add(invoiceItem2a2);
        sl.update(ivm);

        InvoiceViewModel ivm1 = sl.findInvoiceViewModelByInvoiceId(2);
        assertEquals(ivm, ivm1);
    }

    @Test
    public void deleteInvoiceViewModelByInvoiceId() {
        sl.deleteInvoiceViewModelByInvoiceId(5);
        InvoiceViewModel ivm = sl.findInvoiceViewModelByInvoiceId(5);
        assertNull(ivm);

        Integer count = sl.countInvoiceByInvoiceId(5);
        assertEquals(java.util.Optional.of(0).orElse(0), count);
    }
}