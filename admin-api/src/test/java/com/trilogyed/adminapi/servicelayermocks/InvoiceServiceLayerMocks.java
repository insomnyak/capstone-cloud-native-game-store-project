package com.trilogyed.adminapi.servicelayermocks;

import com.trilogyed.adminapi.domain.CustomerViewModel;
import com.trilogyed.adminapi.domain.InvoiceItemViewModel;
import com.trilogyed.adminapi.domain.InvoiceViewModel;
import com.trilogyed.adminapi.domain.TotalInvoiceViewModel;
import com.trilogyed.adminapi.model.*;
import com.trilogyed.adminapi.service.CustomerServiceLayer;
import com.trilogyed.adminapi.service.InvoiceServiceLayer;
import com.trilogyed.adminapi.util.feign.InventoryServiceClient;
import com.trilogyed.adminapi.util.feign.InvoiceServiceClient;
import com.trilogyed.adminapi.util.feign.ProductServiceClient;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvoiceServiceLayerMocks {
    private InvoiceServiceClient invoiceClient;
    private InventoryServiceClient inventoryClient;
    private ProductServiceClient productClient;
    private CustomerServiceLayer csl;
    private InvoiceServiceLayer sl;

    @Test
    public void addGetUpdateDeleteInvoice() {
        invoiceClient = mock(InvoiceServiceClient.class);
        inventoryClient = mock(InventoryServiceClient.class);
        productClient = mock(ProductServiceClient.class);
        csl = mock(CustomerServiceLayer.class);

        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setInvoiceId(1);
        invoiceItem.setInventoryId(1100);
        invoiceItem.setQuantity(250);
        invoiceItem.setUnitPrice(new BigDecimal("1.49"));

        InvoiceItem savedInvoiceItem = new InvoiceItem();
        savedInvoiceItem.setInvoiceItemId(10001);
        savedInvoiceItem.setInvoiceId(1);
        savedInvoiceItem.setInventoryId(1100);
        savedInvoiceItem.setQuantity(250);
        savedInvoiceItem.setUnitPrice(new BigDecimal("1.49"));

        InvoiceItem anotherInvoiceItem = new InvoiceItem();
        anotherInvoiceItem.setInvoiceId(1);
        anotherInvoiceItem.setInventoryId(2200);
        anotherInvoiceItem.setQuantity(300);
        anotherInvoiceItem.setUnitPrice(new BigDecimal("1.49"));

        InvoiceItem anotherInvoiceItem1 = new InvoiceItem();
        anotherInvoiceItem1.setInvoiceItemId(10002);
        anotherInvoiceItem1.setInvoiceId(1);
        anotherInvoiceItem1.setInventoryId(2200);
        anotherInvoiceItem1.setQuantity(300);
        anotherInvoiceItem1.setUnitPrice(new BigDecimal("1.49"));

        List<InvoiceItem> invoiceItemList1 = new ArrayList<>();
        invoiceItemList1.add(invoiceItem);
        invoiceItemList1.add(anotherInvoiceItem);

        List<InvoiceItem> invoiceItemList = new ArrayList<>();
        invoiceItemList.add(savedInvoiceItem);
        invoiceItemList.add(anotherInvoiceItem1);

        InvoiceViewModel ivm = new InvoiceViewModel();
        ivm.setCustomerId(10);
        ivm.setPurchaseDate(LocalDate.now());
        ivm.setInvoiceItemList(invoiceItemList);

        InvoiceViewModel savedInvoice = new InvoiceViewModel();
        savedInvoice.setInvoiceId(1);
        savedInvoice.setCustomerId(10);
        savedInvoice.setPurchaseDate(LocalDate.now());
        savedInvoice.setInvoiceItemList(invoiceItemList);

        InvoiceViewModel anotherInvoice = new InvoiceViewModel();
        anotherInvoice.setInvoiceId(2);
        anotherInvoice.setCustomerId(20);
        anotherInvoice.setPurchaseDate(LocalDate.now());
        anotherInvoice.setInvoiceItemList(invoiceItemList);

        List<InvoiceViewModel> invoiceList = new ArrayList<>();
        invoiceList.add(savedInvoice);
        invoiceList.add(anotherInvoice);

        List<InvoiceViewModel> invoiceList1 = new ArrayList<>();
        invoiceList1.add(savedInvoice);

        List<InvoiceViewModel> invoiceList2 = new ArrayList<>();
        invoiceList2.add(anotherInvoice);

        InvoiceViewModel updateInvoice = new InvoiceViewModel();
        updateInvoice.setInvoiceId(3);
        updateInvoice.setCustomerId(30);
        updateInvoice.setPurchaseDate(LocalDate.now());
        updateInvoice.setInvoiceItemList(invoiceItemList);

        InvoiceViewModel deleteInvoice = new InvoiceViewModel();
        deleteInvoice.setInvoiceId(4);
        deleteInvoice.setCustomerId(30);
        deleteInvoice.setPurchaseDate(LocalDate.now());
        deleteInvoice.setInvoiceItemList(invoiceItemList);

        CustomerViewModel savedCustomer = new CustomerViewModel();
        savedCustomer.setCustomerId(10);
        savedCustomer.setFirstName("John");
        savedCustomer.setLastName("Smith");
        savedCustomer.setStreet("Park Ave");
        savedCustomer.setCity("Manhattan");
        savedCustomer.setZip("10019");
        savedCustomer.setEmail("js@pens.com");
        savedCustomer.setPhone("123-456-789");
        savedCustomer.setLevelUpId(1010);
        savedCustomer.setPoints(0);
        savedCustomer.setMemberDate(LocalDate.now());

        CustomerViewModel anotherCustomer = new CustomerViewModel();
        anotherCustomer.setCustomerId(20);
        anotherCustomer.setFirstName("John");
        anotherCustomer.setLastName("Smith");
        anotherCustomer.setStreet("Park Ave");
        anotherCustomer.setCity("Manhattan");
        anotherCustomer.setZip("10019");
        anotherCustomer.setEmail("js@pens.com");
        anotherCustomer.setPhone("123-456-789");
        anotherCustomer.setLevelUpId(1020);
        anotherCustomer.setPoints(0);
        anotherCustomer.setMemberDate(LocalDate.now());

        CustomerViewModel updateCustomer = new CustomerViewModel();
        updateCustomer.setCustomerId(30);
        updateCustomer.setFirstName("Joanne");
        updateCustomer.setLastName("Smith");
        updateCustomer.setStreet("Central Park Ave");
        updateCustomer.setCity("Manhattan");
        updateCustomer.setZip("10014");
        updateCustomer.setEmail("js@pens.com");
        updateCustomer.setPhone("123-456-789");
        updateCustomer.setLevelUpId(1030);
        updateCustomer.setPoints(100);
        updateCustomer.setMemberDate(LocalDate.now());

        Product savedProduct = new Product();
        savedProduct.setProductId(100);
        savedProduct.setProductName("Lamy");
        savedProduct.setProductDescription("Fountain Pen");
        savedProduct.setListPrice(new BigDecimal("2.49"));
        savedProduct.setUnitCost(new BigDecimal("1.49"));

        Product anotherProduct = new Product();
        anotherProduct.setProductId(200);
        anotherProduct.setProductName("Lamy Elite");
        anotherProduct.setProductDescription("Fountain Pen");
        anotherProduct.setListPrice(new BigDecimal("2.49"));
        anotherProduct.setUnitCost(new BigDecimal("1.49"));

        Inventory savedInventory = new Inventory();
        savedInventory.setInventoryId(1100);
        savedInventory.setProductId(100);
        savedInventory.setQuantity(250);

        Inventory anotherInventory = new Inventory();
        anotherInventory.setInventoryId(2200);
        anotherInventory.setProductId(200);
        anotherInventory.setQuantity(350);

        doReturn(savedInvoice).when(invoiceClient).saveInvoiceViewModel(ivm);
        doReturn(savedInvoice).when(invoiceClient).findInvoiceViewModelByInvoiceId(1);
        doReturn(anotherInvoice).when(invoiceClient).findInvoiceViewModelByInvoiceId(2);
        doReturn(savedInventory).when(inventoryClient).findInventoryByInventoryId(1100);
        doReturn(anotherInventory).when(inventoryClient).findInventoryByInventoryId(2200);
        doReturn(savedProduct).when(productClient).findProductByProductId(100);
        doReturn(anotherProduct).when(productClient).findProductByProductId(200);
        doReturn(invoiceList2).when(invoiceClient).findInvoiceViewModelsByCustomerId(20);
        doReturn(invoiceList).when(invoiceClient).findAllInvoiceViewModels();
        doReturn(savedCustomer).when(csl).getCustomer(10);
        doNothing().when(csl).updateCustomer(savedCustomer);
        doReturn(anotherCustomer).when(csl).getCustomer(20);
        doNothing().when(csl).updateCustomer(anotherCustomer);
        doReturn(anotherCustomer).when(csl).getCustomer(30);
        doNothing().when(csl).updateCustomer(updateCustomer);
        doNothing().when(invoiceClient).updateInvoiceViewModel(updateInvoice);
        doReturn(updateInvoice).when(invoiceClient).findInvoiceViewModelByInvoiceId(3);
        doNothing().when(invoiceClient).deleteInvoiceViewModelByInvoiceId(4);
        doReturn(null).when(invoiceClient).findInvoiceViewModelByInvoiceId(4);

        /** Testing the adding of an invoice */
        sl = new InvoiceServiceLayer(invoiceClient, inventoryClient, productClient,csl);
        TotalInvoiceViewModel tivm = sl.createInvoice(ivm);
        TotalInvoiceViewModel tivm1 = sl.getInvoice(tivm.getInvoiceId());
        assertEquals(tivm,tivm1);

        /** Testing get all invoices */

        List<TotalInvoiceViewModel> tivmList = sl.getAllInvoices();
        assertEquals(tivmList.size(),2);

        /** Testing get Invoice by customerId */

        List<TotalInvoiceViewModel> tivmListByCustomerId = sl.getInvoicesByCustomerId(20);
        assertEquals(tivmListByCustomerId.size(),1);

        /** Testing get InvoiceItems by invoiceId */
        List<InvoiceItemViewModel> iivmList = sl.getItemsByInvoiceId(1);
        assertEquals(iivmList.size(),2);

        /** Testing update invoice */
        TotalInvoiceViewModel toUpdate = sl.getInvoice(3);
        sl.updateInvoiceIncludingInvoiceItems(toUpdate);

        TotalInvoiceViewModel afterUpdate = sl.getInvoice(3);
        assertEquals(toUpdate,afterUpdate);

        /** Testing delete invoice */
        sl.deleteInvoice(4);
        assertNull(sl.getInvoice(4));
    }


}
