package com.trilogyed.adminapi.domain;

import java.math.BigDecimal;

import java.util.Objects;

public class ItemViewModel {
    private Integer productId;
    private String productName;
    private String productDescription;
    private BigDecimal listPrice;
    private BigDecimal unitCost;
    private Integer inventoryId;
    private Integer quantityInInventory;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public BigDecimal getListPrice() {
        return listPrice;
    }

    public void setListPrice(BigDecimal listPrice) {
        this.listPrice = listPrice;
    }

    public BigDecimal getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(BigDecimal unitCost) {
        this.unitCost = unitCost;
    }

    public Integer getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Integer inventoryId) {
        this.inventoryId = inventoryId;
    }

    public Integer getQuantityInInventory() {
        return quantityInInventory;
    }

    public void setQuantityInInventory(Integer quantityInInventory) {
        this.quantityInInventory = quantityInInventory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemViewModel that = (ItemViewModel) o;
        return Objects.equals(productId, that.productId) &&
                Objects.equals(productName, that.productName) &&
                Objects.equals(productDescription, that.productDescription) &&
                Objects.equals(listPrice, that.listPrice) &&
                Objects.equals(unitCost, that.unitCost) &&
                Objects.equals(inventoryId, that.inventoryId) &&
                Objects.equals(quantityInInventory, that.quantityInInventory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, productName, productDescription, listPrice, unitCost, inventoryId, quantityInInventory);
    }
}
