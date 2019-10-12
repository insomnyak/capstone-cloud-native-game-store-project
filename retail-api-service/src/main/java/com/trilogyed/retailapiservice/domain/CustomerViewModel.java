package com.trilogyed.retailapiservice.domain;

import java.util.List;
import java.util.Objects;

public class CustomerViewModel extends Customer {

    private List<OrderViewModel> orders;
    private LevelUp levelUp;

    public List<OrderViewModel> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderViewModel> orders) {
        this.orders = orders;
    }

    public LevelUp getLevelUp() {
        return levelUp;
    }

    public void setLevelUp(LevelUp levelUp) {
        this.levelUp = levelUp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomerViewModel)) return false;
        if (!super.equals(o)) return false;
        CustomerViewModel that = (CustomerViewModel) o;
        return Objects.equals(getOrders(), that.getOrders()) &&
                Objects.equals(getLevelUp(), that.getLevelUp());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getOrders(), getLevelUp());
    }

    @Override
    public String toString() {
        return "CustomerViewModel{" +
                "orders=" + orders +
                ", levelUp=" + levelUp +
                "} " + super.toString();
    }
}
