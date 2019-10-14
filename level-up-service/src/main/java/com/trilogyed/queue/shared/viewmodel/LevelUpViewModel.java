package com.trilogyed.queue.shared.viewmodel;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

public class LevelUpViewModel {

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
        if (!(o instanceof LevelUpViewModel)) return false;
        if (!super.equals(o)) return false;
        LevelUpViewModel that = (LevelUpViewModel) o;
        return Objects.equals(getLevelUpId(), that.getLevelUpId()) &&
                Objects.equals(getCustomerId(), that.getCustomerId()) &&
                Objects.equals(getPoints(), that.getPoints()) &&
                Objects.equals(getMemberDate(), that.getMemberDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getLevelUpId(), getCustomerId(), getPoints(), getMemberDate());
    }
}
