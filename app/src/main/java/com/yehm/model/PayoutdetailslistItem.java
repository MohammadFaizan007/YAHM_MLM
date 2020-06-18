package com.yehm.model;

import com.google.gson.annotations.SerializedName;

public class PayoutdetailslistItem {

    @SerializedName("date")
    private String date;

    @SerializedName("amount")
    private String amount;

    @SerializedName("loginId")
    private String loginId;

    @SerializedName("incomeType")
    private String incomeType;

    @SerializedName("name")
    private String name;

    @SerializedName("commission")
    private String commission;

    @SerializedName("businessAmount")
    private String businessAmount;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getIncomeType() {
        return incomeType;
    }

    public void setIncomeType(String incomeType) {
        this.incomeType = incomeType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public String getBusinessAmount() {
        return businessAmount;
    }

    public void setBusinessAmount(String businessAmount) {
        this.businessAmount = businessAmount;
    }

    @Override
    public String toString() {
        return
                "PayoutdetailslistItem{" +
                        "date = '" + date + '\'' +
                        ",amount = '" + amount + '\'' +
                        ",loginId = '" + loginId + '\'' +
                        ",incomeType = '" + incomeType + '\'' +
                        ",name = '" + name + '\'' +
                        ",commission = '" + commission + '\'' +
                        ",businessAmount = '" + businessAmount + '\'' +
                        "}";
    }
}