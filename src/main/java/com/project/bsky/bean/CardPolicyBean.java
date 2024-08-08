package com.project.bsky.bean;

import lombok.Data;

import java.util.Date;
@Data
public class CardPolicyBean {
    private Date startDate;
    private Date endDate;
    private Integer familyAmount;
    private Integer femaleAmount;
    private Integer userId;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getFamilyAmount() {
        return familyAmount;
    }

    public void setFamilyAmount(Integer familyAmount) {
        this.familyAmount = familyAmount;
    }

    public Integer getFemaleAmount() {
        return femaleAmount;
    }

    public void setFemaleAmount(Integer femaleAmount) {
        this.femaleAmount = femaleAmount;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
