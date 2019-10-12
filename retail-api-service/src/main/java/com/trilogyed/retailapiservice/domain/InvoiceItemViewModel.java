package com.trilogyed.retailapiservice.domain;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;

public class InvoiceItemViewModel {

    private Integer invoiceItemId;
    private Integer invoiceId;
    private Integer inventoryId;

    @Valid
    @NotNull(message = "Please provide the product.")
    private Product product;

    @NotNull(message = "Please provide the quantity")
    @Min(value = 1, message = "quantity must be >= 1")
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
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
        if (!(o instanceof InvoiceItemViewModel)) return false;
        InvoiceItemViewModel that = (InvoiceItemViewModel) o;
        return Objects.equals(getInvoiceItemId(), that.getInvoiceItemId()) &&
                Objects.equals(getInvoiceId(), that.getInvoiceId()) &&
                Objects.equals(getInventoryId(), that.getInventoryId()) &&
                Objects.equals(getProduct(), that.getProduct()) &&
                Objects.equals(getQuantity(), that.getQuantity()) &&
                Objects.equals(getUnitPrice(), that.getUnitPrice());
    }

    @Override
    public int hashCode() {
        return Objects
                .hash(getInvoiceItemId(), getInvoiceId(), getInventoryId(), getProduct(), getQuantity(),
                        getUnitPrice());
    }

    @Override
    public String toString() {
        return "InvoiceItemViewModel{" +
                "invoiceItemId=" + invoiceItemId +
                ", invoiceId=" + invoiceId +
                ", inventoryId=" + inventoryId +
                ", product=" + product +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                '}';
    }
}
