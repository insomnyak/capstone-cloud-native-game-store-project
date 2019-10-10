package com.trilogyed.adminapi.model;

import java.math.BigDecimal;
import java.util.Objects;

public class InvoiceItem {
    private Integer invoiceItemId;
    private Integer invoiceId;
    private Integer inventoryId;
    private Integer quantity;
    private BigDecimal unitPrice;

    public Integer getInvoiceItemId() {
        return invoiceItemId;
    }

    public void setInvoiceItemId(Integer invoiceItemId) {
        this.invoiceItemId = invoiceItemId;
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
        if (o == null || getClass() != o.getClass()) return false;
        InvoiceItem that = (InvoiceItem) o;
        return Objects.equals(invoiceItemId, that.invoiceItemId) &&
                Objects.equals(invoiceId, that.invoiceId) &&
                Objects.equals(inventoryId, that.inventoryId) &&
                Objects.equals(quantity, that.quantity) &&
                Objects.equals(unitPrice, that.unitPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(invoiceItemId, invoiceId, inventoryId, quantity, unitPrice);
    }
}
