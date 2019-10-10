package com.trilogyed.invoiceservice.service;

import com.insomnyak.util.MapClasses;
import com.trilogyed.invoiceservice.dao.InvoiceDao;
import com.trilogyed.invoiceservice.dao.InvoiceItemDao;
import com.trilogyed.invoiceservice.model.Invoice;
import com.trilogyed.invoiceservice.model.InvoiceItem;
import com.trilogyed.invoiceservice.viewmodel.InvoiceViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ServiceLayer {

    private InvoiceDao invoiceDao;
    private InvoiceItemDao invoiceItemDao;

    @Autowired
    public ServiceLayer(InvoiceDao invoiceDao, InvoiceItemDao invoiceItemDao) {
        this.invoiceDao = invoiceDao;
        this.invoiceItemDao = invoiceItemDao;
    }

    public InvoiceViewModel save(InvoiceViewModel ivm) {
        Invoice invoice = buildInvoice(ivm);
        invoice = invoiceDao.add(invoice);

        Integer invoiceId = invoice.getInvoiceId();

        ivm.setInvoiceId(invoiceId);

        if (ivm.getInvoiceItems() != null) {
            for (InvoiceItem invoiceItem : ivm.getInvoiceItems()) {
                invoiceItem.setInvoiceId(invoiceId);
                InvoiceItem invoiceItem1 = invoiceItemDao.add(invoiceItem);
                invoiceItem.setInvoiceItemId(invoiceItem1.getInvoiceItemId());
            }
        }

        return ivm;
    }

    public void update(InvoiceViewModel ivm) {
        Invoice invoice = buildInvoice(ivm);
        invoiceDao.update(invoice);

        // add invoiceItem if invoiceItemId is null or provided invoiceItemId does not exist.
        // update invoiceItem if invoiceItemId exists
        // delete any invoiceItems in DB not found it this list
        if (ivm.getInvoiceItems() != null) {
            Integer invoiceId = ivm.getInvoiceId();

            List<InvoiceItem> updatedInvoiceItems = ivm.getInvoiceItems();
            List<InvoiceItem> currentInvoiceItems = invoiceItemDao.findByInvoiceId(invoiceId);

            // filter current Invoice Items in DB to get items that should be deleted
            List<InvoiceItem> invoiceItemsToDelete = currentInvoiceItems.stream().filter(invoiceItem -> {
                boolean existsInUpdate = updatedInvoiceItems.stream().anyMatch(updatedInvoiceItem ->
                        updatedInvoiceItem.getInvoiceItemId() == invoiceItem.getInvoiceItemId());
                return !existsInUpdate;
            }).collect(Collectors.toList());

            // filter provided updated list to get net new items that should be added
            List<InvoiceItem> invoiceItemsToAdd = updatedInvoiceItems.stream().filter(invoiceItem -> {
                if (invoiceItem.getInvoiceItemId() == null) return true;
                boolean existsInDB = currentInvoiceItems.stream().anyMatch(currentInvoiceItem ->
                        currentInvoiceItem.getInvoiceItemId() == invoiceItem.getInvoiceItemId());
                return !existsInDB;
            }).collect(Collectors.toList());

            // filter provided updated list to get items that should be updated
            List<InvoiceItem> invoiceItemsToUpdate = updatedInvoiceItems.stream().filter(invoiceItem -> {
                boolean existsInInvoiceItemsToAdd = invoiceItemsToAdd.stream().anyMatch(itemToAdd ->
                        itemToAdd.getInvoiceItemId() == invoiceItem.getInvoiceItemId());
                boolean existsInInvoiceItemsToDelete = invoiceItemsToDelete.stream().anyMatch(itemToDelete ->
                        itemToDelete.getInvoiceItemId() == invoiceItem.getInvoiceItemId());
                return !existsInInvoiceItemsToAdd && !existsInInvoiceItemsToDelete;
            }).collect(Collectors.toList());

            // delete
            for (InvoiceItem invoiceItem : invoiceItemsToDelete) {
                invoiceItemDao.delete(invoiceItem.getInvoiceItemId());
            }

            // add
            for (InvoiceItem invoiceItem : invoiceItemsToAdd) {
                invoiceItem.setInvoiceId(invoiceId);
                InvoiceItem invoiceItem1 = invoiceItemDao.add(invoiceItem);
                invoiceItem.setInvoiceItemId(invoiceItem1.getInvoiceItemId());
            }

            // update
            for (InvoiceItem invoiceItem : invoiceItemsToUpdate) {
                invoiceItemDao.update(invoiceItem);
            }
        }
    }

    public void deleteInvoiceViewModelByInvoiceId(Integer invoiceId) {
        invoiceItemDao.deleteByInvoiceId(invoiceId);
        invoiceDao.delete(invoiceId);
    }

    public InvoiceViewModel findInvoiceViewModelByInvoiceId(Integer invoiceId) {
        Invoice invoice = invoiceDao.find(invoiceId);
        if (invoice == null) {
            return null;
        }
        return buildInvoiceViewModel(invoice);
    }

    public List<InvoiceViewModel> findAllInvoiceViewModels() {
        List<Invoice> invoices = invoiceDao.findAll();
        List<InvoiceViewModel> ivmList = new ArrayList<>();

        for (Invoice invoice : invoices) {
            InvoiceViewModel ivm = buildInvoiceViewModel(invoice);
            ivm.setInvoiceItems(invoiceItemDao.findByInvoiceId(invoice.getInvoiceId()));
            ivmList.add(ivm);
        }

        return ivmList;
    }

    public List<InvoiceViewModel> findInvoiceViewModelsByCustomerId(Integer customerId) {
        List<Invoice> invoices = invoiceDao.findByCustomerId(customerId);
        List<InvoiceViewModel> ivmList = new ArrayList<>();

        for (Invoice invoice : invoices) {
            InvoiceViewModel ivm = buildInvoiceViewModel(invoice);
            ivm.setInvoiceItems(invoiceItemDao.findByInvoiceId(invoice.getInvoiceId()));
            ivmList.add(ivm);
        }

        return ivmList;
    }

    public Integer countInvoiceByInvoiceId(Integer invoiceId) {
        return invoiceDao.count(invoiceId);
    }

    public Integer countInvoicesByCustomerId(Integer customerId) {
        return invoiceDao.countByCustomerId(customerId);
    }

    private Invoice buildInvoice(InvoiceViewModel ivm) {
        return (new MapClasses<>(ivm, Invoice.class)).mapFirstToSecond(false);
    }

    private InvoiceViewModel buildInvoiceViewModel(Invoice invoice) {
        List<InvoiceItem> invoiceItems = invoiceItemDao.findByInvoiceId(invoice.getInvoiceId());
        InvoiceViewModel ivm = (new MapClasses<>(invoice, InvoiceViewModel.class)).mapFirstToSecond(false);
        ivm.setInvoiceItems(invoiceItems);
        return ivm;
    }
}
