package com.trilogyed.levelupservice.model;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

public class LevelUp {

    private Integer levelUpId;

    @NotNull(message = "Please provide customerId.")
    private Integer customerId;

    @NotNull(message = "Please provide points.")
    private Integer points;

    @NotNull(message = "Please provide the memberDate.")
    private LocalDate memberDate;

    public Integer getLevelUpId() {
        return levelUpId;
    }

    public void setLevelUpId(Integer levelUpId) {
        this.levelUpId = levelUpId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public LocalDate getMemberDate() {
        return memberDate;
    }

    public void setMemberDate(LocalDate memberDate) {
        this.memberDate = memberDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LevelUp)) return false;
        LevelUp levelUp = (LevelUp) o;
        return Objects.equals(getLevelUpId(), levelUp.getLevelUpId()) &&
                Objects.equals(getCustomerId(), levelUp.getCustomerId()) &&
                Objects.equals(getPoints(), levelUp.getPoints()) &&
                Objects.equals(getMemberDate(), levelUp.getMemberDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLevelUpId(), getCustomerId(), getPoints(), getMemberDate());
    }
}
