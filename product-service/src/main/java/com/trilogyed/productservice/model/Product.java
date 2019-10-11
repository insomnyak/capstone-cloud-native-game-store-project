package com.trilogyed.productservice.model;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Objects;

public class Product {
    private Integer productId;
    @NotBlank
    @Size(max=50)
    private String productName;
    @NotBlank
    @Size(max=255)
    private String productDescription;
    @NotNull
    @Digits(integer = 5,fraction = 2)
    private BigDecimal listPrice;
    @NotNull
    @Digits(integer = 5,fraction = 2)
    private BigDecimal unitCost;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(productId, product.productId) &&
                Objects.equals(productName, product.productName) &&
                Objects.equals(productDescription, product.productDescription) &&
                Objects.equals(listPrice, product.listPrice) &&
                Objects.equals(unitCost, product.unitCost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, productName, productDescription, listPrice, unitCost);
    }
}
