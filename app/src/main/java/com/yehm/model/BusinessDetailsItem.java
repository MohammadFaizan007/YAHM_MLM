package com.yehm.model;

import com.google.gson.annotations.SerializedName;

public class BusinessDetailsItem {

    @SerializedName("nonActiveLeft")
    private String nonActiveLeft;

    @SerializedName("carryForwardLeft")
    private String carryForwardLeft;

    @SerializedName("directMemberRight")
    private String directMemberRight;

    @SerializedName("rightTotal")
    private String rightTotal;

    @SerializedName("totalBusinessRight")
    private String totalBusinessRight;

    @SerializedName("totalBusinessLeft")
    private String totalBusinessLeft;

    @SerializedName("leftTotal")
    private String leftTotal;

    @SerializedName("directMemberLeft")
    private String directMemberLeft;

    @SerializedName("activeRight")
    private String activeRight;

    @SerializedName("flushBusiness")
    private String flushBusiness;

    @SerializedName("paidBusiness")
    private String paidBusiness;

    @SerializedName("activeLeft")
    private String activeLeft;

    @SerializedName("nonActiveRight")
    private String nonActiveRight;

    @SerializedName("carryForwardRight")
    private String carryForwardRight;


    @SerializedName("currLeft")
    private String currLeft;

    @SerializedName("currRight")
    private String currRight;



    public String getCurrLeft() {
        return currLeft;
    }

    public void setCurrLeft(String currLeft) {
        this.currLeft = currLeft;
    }

    public String getCurrRight() {
        return currRight;
    }

    public void setCurrRight(String currRight) {
        this.currRight = currRight;
    }

    public String getNonActiveLeft() {
        return nonActiveLeft;
    }

    public void setNonActiveLeft(String nonActiveLeft) {
        this.nonActiveLeft = nonActiveLeft;
    }

    public String getCarryForwardLeft() {
        return carryForwardLeft;
    }

    public void setCarryForwardLeft(String carryForwardLeft) {
        this.carryForwardLeft = carryForwardLeft;
    }

    public String getDirectMemberRight() {
        return directMemberRight;
    }

    public void setDirectMemberRight(String directMemberRight) {
        this.directMemberRight = directMemberRight;
    }

    public String getRightTotal() {
        return rightTotal;
    }

    public void setRightTotal(String rightTotal) {
        this.rightTotal = rightTotal;
    }

    public String getTotalBusinessRight() {
        return totalBusinessRight;
    }

    public void setTotalBusinessRight(String totalBusinessRight) {
        this.totalBusinessRight = totalBusinessRight;
    }

    public String getTotalBusinessLeft() {
        return totalBusinessLeft;
    }

    public void setTotalBusinessLeft(String totalBusinessLeft) {
        this.totalBusinessLeft = totalBusinessLeft;
    }

    public String getLeftTotal() {
        return leftTotal;
    }

    public void setLeftTotal(String leftTotal) {
        this.leftTotal = leftTotal;
    }

    public String getDirectMemberLeft() {
        return directMemberLeft;
    }

    public void setDirectMemberLeft(String directMemberLeft) {
        this.directMemberLeft = directMemberLeft;
    }

    public String getActiveRight() {
        return activeRight;
    }

    public void setActiveRight(String activeRight) {
        this.activeRight = activeRight;
    }

    public String getFlushBusiness() {
        return flushBusiness;
    }

    public void setFlushBusiness(String flushBusiness) {
        this.flushBusiness = flushBusiness;
    }

    public String getPaidBusiness() {
        return paidBusiness;
    }

    public void setPaidBusiness(String paidBusiness) {
        this.paidBusiness = paidBusiness;
    }

    public String getActiveLeft() {
        return activeLeft;
    }

    public void setActiveLeft(String activeLeft) {
        this.activeLeft = activeLeft;
    }

    public String getNonActiveRight() {
        return nonActiveRight;
    }

    public void setNonActiveRight(String nonActiveRight) {
        this.nonActiveRight = nonActiveRight;
    }

    public String getCarryForwardRight() {
        return carryForwardRight;
    }

    public void setCarryForwardRight(String carryForwardRight) {
        this.carryForwardRight = carryForwardRight;
    }

    @Override
    public String toString() {
        return
                "BusinessDetailsItem{" +
                        "nonActiveLeft = '" + nonActiveLeft + '\'' +
                        ",carryForwardLeft = '" + carryForwardLeft + '\'' +
                        ",directMemberRight = '" + directMemberRight + '\'' +
                        ",rightTotal = '" + rightTotal + '\'' +
                        ",totalBusinessRight = '" + totalBusinessRight + '\'' +
                        ",totalBusinessLeft = '" + totalBusinessLeft + '\'' +
                        ",leftTotal = '" + leftTotal + '\'' +
                        ",directMemberLeft = '" + directMemberLeft + '\'' +
                        ",activeRight = '" + activeRight + '\'' +
                        ",flushBusiness = '" + flushBusiness + '\'' +
                        ",paidBusiness = '" + paidBusiness + '\'' +
                        ",activeLeft = '" + activeLeft + '\'' +
                        ",nonActiveRight = '" + nonActiveRight + '\'' +
                        ",carryForwardRight = '" + carryForwardRight + '\'' +
                        "}";
    }
}