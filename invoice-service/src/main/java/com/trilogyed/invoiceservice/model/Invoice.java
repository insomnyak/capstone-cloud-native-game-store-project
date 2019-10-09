package com.trilogyed.invoiceservice.model;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

public class Invoice {

    private Integer invoiceId;

    @NotNull(message = "Please provide the customerId.")
    private Integer customerId;

    @NotNull(message = "Please provide the purchaseDate.")
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
        if (!(o instanceof Invoice)) return false;
        Invoice invoice = (Invoice) o;
        return Objects.equals(getInvoiceId(), invoice.getInvoiceId()) &&
                Objects.equals(getCustomerId(), invoice.getCustomerId()) &&
                Objects.equals(getPurchaseDate(), invoice.getPurchaseDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getInvoiceId(), getCustomerId(), getPurchaseDate());
    }
}
