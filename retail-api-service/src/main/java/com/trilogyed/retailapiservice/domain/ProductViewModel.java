package com.trilogyed.retailapiservice.domain;

import java.util.List;
import java.util.Objects;

public class ProductViewModel extends Product {
    private List<Inventory> inventory;

    public List<Inventory> getInventory() {
        return inventory;
    }

    public void setInventory(List<Inventory> inventory) {
        this.inventory = inventory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductViewModel)) return false;
        if (!super.equals(o)) return false;
        ProductViewModel that = (ProductViewModel) o;
        return Objects.equals(getInventory(), that.getInventory());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getInventory());
    }
}
