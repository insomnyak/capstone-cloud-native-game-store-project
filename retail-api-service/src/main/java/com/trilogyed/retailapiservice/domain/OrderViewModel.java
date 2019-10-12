package com.trilogyed.retailapiservice.domain;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class OrderViewModel {
        @Valid
        @NotNull(message = "Must provide the customer details.")
        private Customer customer;
        private Integer invoiceId;
        private LocalDate purchaseDate;

        @Valid
        @NotNull(message = "Must provide at least one invoiceItem or Product for purchase.")
        @NotEmpty(message = "Must provide at least one invoiceItem or Product for purchase.")
        private List<InvoiceItemViewModel> invoiceItems;
        private BigDecimal orderTotal;
        private Integer awardedPoints;
        private LevelUp memberPoints;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public List<InvoiceItemViewModel> getInvoiceItems() {
        return invoiceItems;
    }

    public void setInvoiceItems(List<InvoiceItemViewModel> invoiceItems) {
        this.invoiceItems = invoiceItems;
    }

    public BigDecimal getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(BigDecimal orderTotal) {
        this.orderTotal = orderTotal;
    }

    public Integer getAwardedPoints() {
        return awardedPoints;
    }

    public void setAwardedPoints(Integer awardedPoints) {
        this.awardedPoints = awardedPoints;
    }

    public LevelUp getMemberPoints() {
        return memberPoints;
    }

    public void setMemberPoints(LevelUp memberPoints) {
        this.memberPoints = memberPoints;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderViewModel)) return false;
        OrderViewModel that = (OrderViewModel) o;
        return Objects.equals(getCustomer(), that.getCustomer()) &&
                Objects.equals(getInvoiceId(), that.getInvoiceId()) &&
                Objects.equals(getPurchaseDate(), that.getPurchaseDate()) &&
                Objects.equals(getInvoiceItems(), that.getInvoiceItems()) &&
                Objects.equals(getOrderTotal(), that.getOrderTotal()) &&
                Objects.equals(getAwardedPoints(), that.getAwardedPoints()) &&
                Objects.equals(getMemberPoints(), that.getMemberPoints());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCustomer(), getInvoiceId(), getPurchaseDate(), getInvoiceItems(), getOrderTotal(),
                getAwardedPoints(), getMemberPoints());
    }

    @Override
    public String toString() {
        return "OrderViewModel{" +
                "customer=" + customer +
                ", invoiceId=" + invoiceId +
                ", purchaseDate=" + purchaseDate +
                ", invoiceItems=" + invoiceItems +
                ", orderTotal=" + orderTotal +
                ", awardedPoints=" + awardedPoints +
                ", memberPoints=" + memberPoints +
                '}';
    }
}
