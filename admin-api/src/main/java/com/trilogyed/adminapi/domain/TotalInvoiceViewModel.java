package com.trilogyed.adminapi.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class TotalInvoiceViewModel {
    private Integer invoiceId;
    private CustomerViewModel customerViewModel;
    private LocalDate purchaseDate;
    List<InvoiceItemViewModel> invItemList;
    private BigDecimal totalCost;

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

    public List<InvoiceItemViewModel> getInvItemList() {
        return invItemList;
    }

    public void setInvItemList(List<InvoiceItemViewModel> invItemList) {
        this.invItemList = invItemList;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TotalInvoiceViewModel that = (TotalInvoiceViewModel) o;
        return Objects.equals(invoiceId, that.invoiceId) &&
                Objects.equals(customerViewModel, that.customerViewModel) &&
                Objects.equals(purchaseDate, that.purchaseDate) &&
                Objects.equals(invItemList, that.invItemList) &&
                Objects.equals(totalCost, that.totalCost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(invoiceId, customerViewModel, purchaseDate, invItemList, totalCost);
    }
}
