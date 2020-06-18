package com.yehm.model;

import com.google.gson.annotations.SerializedName;

public class MyincomelistItem {

    @SerializedName("loginId")
    private String loginId;

    @SerializedName("displayName")
    private String displayName;

    @SerializedName("payoutNo")
    private String payoutNo;

    @SerializedName("bankHolderName")
    private String bankHolderName;

    @SerializedName("superStarClub")
    private String superStarClub;

    @SerializedName("starClub")
    private String starClub;

    @SerializedName("silverClub")
    private String silverClub;

    @SerializedName("processingFee")
    private String processingFee;

    @SerializedName("incomeMonth")
    private String incomeMonth;

    @SerializedName("memberBranch")
    private String memberBranch;

    @SerializedName("platinumClub")
    private String platinumClub;

    @SerializedName("teamPerformanceBonus")
    private String teamPerformanceBonus;

    @SerializedName("ifscCode")
    private String ifscCode;

    @SerializedName("fK_MemId")
    private String fKMemId;

    @SerializedName("netAmount")
    private String netAmount;

    @SerializedName("directIncome")
    private String directIncome;

    @SerializedName("memberBankName")
    private String memberBankName;

    @SerializedName("superPlatinumClub")
    private String superPlatinumClub;

    @SerializedName("grossAmount")
    private String grossAmount;

    @SerializedName("closingDate")
    private String closingDate;

    @SerializedName("goldClub")
    private String goldClub;

    @SerializedName("bronzeClub")
    private String bronzeClub;

    @SerializedName("tdsAmount")
    private String tdsAmount;

    @SerializedName("memberAccNo")
    private String memberAccNo;

    @SerializedName("pK_PaidMembersId")
    private String pKPaidMembersId;

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPayoutNo() {
        return payoutNo;
    }

    public void setPayoutNo(String payoutNo) {
        this.payoutNo = payoutNo;
    }

    public String getBankHolderName() {
        return bankHolderName;
    }

    public void setBankHolderName(String bankHolderName) {
        this.bankHolderName = bankHolderName;
    }

    public String getSuperStarClub() {
        return superStarClub;
    }

    public void setSuperStarClub(String superStarClub) {
        this.superStarClub = superStarClub;
    }

    public String getStarClub() {
        return starClub;
    }

    public void setStarClub(String starClub) {
        this.starClub = starClub;
    }

    public String getSilverClub() {
        return silverClub;
    }

    public void setSilverClub(String silverClub) {
        this.silverClub = silverClub;
    }

    public String getProcessingFee() {
        return processingFee;
    }

    public void setProcessingFee(String processingFee) {
        this.processingFee = processingFee;
    }

    public String getIncomeMonth() {
        return incomeMonth;
    }

    public void setIncomeMonth(String incomeMonth) {
        this.incomeMonth = incomeMonth;
    }

    public String getMemberBranch() {
        return memberBranch;
    }

    public void setMemberBranch(String memberBranch) {
        this.memberBranch = memberBranch;
    }

    public String getPlatinumClub() {
        return platinumClub;
    }

    public void setPlatinumClub(String platinumClub) {
        this.platinumClub = platinumClub;
    }

    public String getTeamPerformanceBonus() {
        return teamPerformanceBonus;
    }

    public void setTeamPerformanceBonus(String teamPerformanceBonus) {
        this.teamPerformanceBonus = teamPerformanceBonus;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public String getFKMemId() {
        return fKMemId;
    }

    public void setFKMemId(String fKMemId) {
        this.fKMemId = fKMemId;
    }

    public String getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(String netAmount) {
        this.netAmount = netAmount;
    }

    public String getDirectIncome() {
        return directIncome;
    }

    public void setDirectIncome(String directIncome) {
        this.directIncome = directIncome;
    }

    public String getMemberBankName() {
        return memberBankName;
    }

    public void setMemberBankName(String memberBankName) {
        this.memberBankName = memberBankName;
    }

    public String getSuperPlatinumClub() {
        return superPlatinumClub;
    }

    public void setSuperPlatinumClub(String superPlatinumClub) {
        this.superPlatinumClub = superPlatinumClub;
    }

    public String getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(String grossAmount) {
        this.grossAmount = grossAmount;
    }

    public String getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(String closingDate) {
        this.closingDate = closingDate;
    }

    public String getGoldClub() {
        return goldClub;
    }

    public void setGoldClub(String goldClub) {
        this.goldClub = goldClub;
    }

    public String getBronzeClub() {
        return bronzeClub;
    }

    public void setBronzeClub(String bronzeClub) {
        this.bronzeClub = bronzeClub;
    }

    public String getTdsAmount() {
        return tdsAmount;
    }

    public void setTdsAmount(String tdsAmount) {
        this.tdsAmount = tdsAmount;
    }

    public String getMemberAccNo() {
        return memberAccNo;
    }

    public void setMemberAccNo(String memberAccNo) {
        this.memberAccNo = memberAccNo;
    }

    public String getPKPaidMembersId() {
        return pKPaidMembersId;
    }

    public void setPKPaidMembersId(String pKPaidMembersId) {
        this.pKPaidMembersId = pKPaidMembersId;
    }

    @Override
    public String toString() {
        return
                "MyincomelistItem{" +
                        "loginId = '" + loginId + '\'' +
                        ",displayName = '" + displayName + '\'' +
                        ",payoutNo = '" + payoutNo + '\'' +
                        ",bankHolderName = '" + bankHolderName + '\'' +
                        ",superStarClub = '" + superStarClub + '\'' +
                        ",starClub = '" + starClub + '\'' +
                        ",silverClub = '" + silverClub + '\'' +
                        ",processingFee = '" + processingFee + '\'' +
                        ",incomeMonth = '" + incomeMonth + '\'' +
                        ",memberBranch = '" + memberBranch + '\'' +
                        ",platinumClub = '" + platinumClub + '\'' +
                        ",teamPerformanceBonus = '" + teamPerformanceBonus + '\'' +
                        ",ifscCode = '" + ifscCode + '\'' +
                        ",fK_MemId = '" + fKMemId + '\'' +
                        ",netAmount = '" + netAmount + '\'' +
                        ",directIncome = '" + directIncome + '\'' +
                        ",memberBankName = '" + memberBankName + '\'' +
                        ",superPlatinumClub = '" + superPlatinumClub + '\'' +
                        ",grossAmount = '" + grossAmount + '\'' +
                        ",closingDate = '" + closingDate + '\'' +
                        ",goldClub = '" + goldClub + '\'' +
                        ",bronzeClub = '" + bronzeClub + '\'' +
                        ",tdsAmount = '" + tdsAmount + '\'' +
                        ",memberAccNo = '" + memberAccNo + '\'' +
                        ",pK_PaidMembersId = '" + pKPaidMembersId + '\'' +
                        "}";
    }
}