package com.trilogyed.adminapi.model;

import java.time.LocalDate;
import java.util.Objects;

public class Invoice {
    private Integer invoiceId;
    private Integer customerId;
    private LocalDate purchaseDate;

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Invoice invoice = (Invoice) o;
        return Objects.equals(invoiceId, invoice.invoiceId) &&
                Objects.equals(customerId, invoice.customerId) &&
                Objects.equals(purchaseDate, invoice.purchaseDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(invoiceId, customerId, purchaseDate);
    }
}
