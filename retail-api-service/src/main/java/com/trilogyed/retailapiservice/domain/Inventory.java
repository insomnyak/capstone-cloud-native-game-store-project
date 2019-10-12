package com.trilogyed.retailapiservice.domain;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class Inventory {
    private Integer inventoryId;

    @NotNull(message = "Please provide a productId.")
    private Integer productId;

    @NotNull(message = "Please provide a quantity.")
    @Min(value = 0, message = "quantity must be >= 0.")
    private Integer quantity;

    public Integer getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Integer inventoryId) {
        this.inventoryId = inventoryId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Inventory)) return false;
        Inventory inventory = (Inventory) o;
        return Objects.equals(getInventoryId(), inventory.getInventoryId()) &&
                Objects.equals(getProductId(), inventory.getProductId()) &&
                Objects.equals(getQuantity(), inventory.getQuantity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getInventoryId(), getProductId(), getQuantity());
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "inventoryId=" + inventoryId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                '}';
    }
}
