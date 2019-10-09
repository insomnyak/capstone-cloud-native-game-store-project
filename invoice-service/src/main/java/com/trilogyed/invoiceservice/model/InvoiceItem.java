package com.trilogyed.invoiceservice.model;

import java.math.BigDecimal;
import java.util.Objects;

public class InvoiceItem {
    private Integer invoiceItem;
    private Integer invoiceId;
    private Integer inventoryId;
    private Integer quantity;
    private BigDecimal unitPrice;

    public Integer getInvoiceItem() {
        return invoiceItem;
    }

    public void setInvoiceItem(Integer invoiceItem) {
        this.invoiceItem = invoiceItem;
    }

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Integer getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Integer inventoryId) {
        this.inventoryId = inventoryId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InvoiceItem)) return false;
        InvoiceItem that = (InvoiceItem) o;
        return Objects.equals(getInvoiceItem(), that.getInvoiceItem()) &&
                Objects.equals(getInvoiceId(), that.getInvoiceId()) &&
                Objects.equals(getInventoryId(), that.getInventoryId()) &&
                Objects.equals(getQuantity(), that.getQuantity()) &&
                Objects.equals(getUnitPrice(), that.getUnitPrice());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getInvoiceItem(), getInvoiceId(), getInventoryId(), getQuantity(), getUnitPrice());
    }
}