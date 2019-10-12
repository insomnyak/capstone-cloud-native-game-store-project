package com.trilogyed.retailapiservice.domain;

import java.util.Objects;

public class InventoryViewModel {
    private Integer inventoryId;
    private Product product;
    private Integer quantity;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InventoryViewModel)) return false;
        InventoryViewModel that = (InventoryViewModel) o;
        return Objects.equals(getInventoryId(), that.getInventoryId()) &&
                Objects.equals(getProduct(), that.getProduct()) &&
                Objects.equals(getQuantity(), that.getQuantity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getInventoryId(), getProduct(), getQuantity());
    }

    @Override
    public String toString() {
        return "InventoryViewModel{" +
                "inventoryId=" + inventoryId +
                ", product=" + product +
                ", quantity=" + quantity +
                '}';
    }
}
