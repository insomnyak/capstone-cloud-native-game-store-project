package com.trilogyed.retailapiservice.domain;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class InvoiceViewModel {
    private Integer invoiceId;

    @NotNull(message = "Please provide the customerId.")
    private Integer customerId;

    @NotNull(message = "Please provide the purchaseDate.")
    private LocalDate purchaseDate;

    @Valid
    private List<InvoiceItem> invoiceItems;

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

    public List<InvoiceItem> getInvoiceItems() {
        return invoiceItems;
    }

    public void setInvoiceItems(List<InvoiceItem> invoiceItems) {
        this.invoiceItems = invoiceItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InvoiceViewModel)) return false;
        InvoiceViewModel that = (InvoiceViewModel) o;
        return Objects.equals(getInvoiceId(), that.getInvoiceId()) &&
                Objects.equals(getCustomerId(), that.getCustomerId()) &&
                Objects.equals(getPurchaseDate(), that.getPurchaseDate()) &&
                Objects.equals(getInvoiceItems(), that.getInvoiceItems());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getInvoiceId(), getCustomerId(), getPurchaseDate(), getInvoiceItems());
    }

    @Override
    public String toString() {
        return "InvoiceViewModel{" +
                "invoiceId=" + invoiceId +
                ", customerId=" + customerId +
                ", purchaseDate=" + purchaseDate +
                ", invoiceItems=" + invoiceItems +
                '}';
    }
}
