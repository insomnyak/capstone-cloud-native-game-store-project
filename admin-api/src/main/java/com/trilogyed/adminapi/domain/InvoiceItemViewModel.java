package com.trilogyed.adminapi.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class InvoiceItemViewModel {
    private Integer invoiceItemId;
    private Integer invoiceId;
    private Integer inventoryId;
    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subTotal;

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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvoiceItemViewModel that = (InvoiceItemViewModel) o;
        return Objects.equals(invoiceItemId, that.invoiceItemId) &&
                Objects.equals(invoiceId, that.invoiceId) &&
                Objects.equals(inventoryId, that.inventoryId) &&
                Objects.equals(productName, that.productName) &&
                Objects.equals(quantity, that.quantity) &&
                Objects.equals(unitPrice, that.unitPrice) &&
                Objects.equals(subTotal, that.subTotal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(invoiceItemId, invoiceId, inventoryId, productName, quantity, unitPrice, subTotal);
    }
}
