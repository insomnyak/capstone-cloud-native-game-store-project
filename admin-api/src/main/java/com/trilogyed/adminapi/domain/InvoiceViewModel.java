package com.trilogyed.adminapi.domain;

import com.trilogyed.adminapi.model.InvoiceItem;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class InvoiceViewModel {
    private Integer invoiceId;
    private CustomerViewModel customerViewModel;
    private LocalDate purchaseDate;
    List<InvoiceItem> invoiceItemList;

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public CustomerViewModel getCustomerViewModel() {
        return customerViewModel;
    }

    public void setCustomerViewModel(CustomerViewModel customerViewModel) {
        this.customerViewModel = customerViewModel;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public List<InvoiceItem> getInvoiceItemList() {
        return invoiceItemList;
    }

    public void setInvoiceItemList(List<InvoiceItem> invoiceItemList) {
        this.invoiceItemList = invoiceItemList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvoiceViewModel that = (InvoiceViewModel) o;
        return Objects.equals(invoiceId, that.invoiceId) &&
                Objects.equals(customerViewModel, that.customerViewModel) &&
                Objects.equals(purchaseDate, that.purchaseDate) &&
                Objects.equals(invoiceItemList, that.invoiceItemList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(invoiceId, customerViewModel, purchaseDate, invoiceItemList);
    }
}
