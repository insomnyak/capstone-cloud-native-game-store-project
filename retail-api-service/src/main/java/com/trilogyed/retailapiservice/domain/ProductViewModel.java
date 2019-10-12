package com.trilogyed.retailapiservice.domain;

import java.util.List;
import java.util.Objects;

public class ProductViewModel extends Product {
    private Integer inventoryId;
    private Integer quantity;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductViewModel)) return false;
        if (!super.equals(o)) return false;
        ProductViewModel that = (ProductViewModel) o;
        return Objects.equals(getInventoryId(), that.getInventoryId()) &&
                Objects.equals(getQuantity(), that.getQuantity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getInventoryId(), getQuantity());
    }

    @Override
    public String toString() {
        return "ProductViewModel{" +
                "inventoryId=" + inventoryId +
                ", quantity=" + quantity +
                "} " + super.toString();
    }
}
