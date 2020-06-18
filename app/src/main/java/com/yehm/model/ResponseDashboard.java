package com.yehm.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseDashboard {

    @SerializedName("leftBV")
    private String leftBV;

    @SerializedName("response")
    private String response;

    @SerializedName("payout")
    private String payout;

    @SerializedName("label")
    private String label;

    @SerializedName("dashboard")
    private List<DashboardItem> dashboard;

    @SerializedName("currentBV")
    private String currentBV;

    @SerializedName("rightBV")
    private String rightBV;

/*
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
*/


    public String getLeftBV() {
        return leftBV;
    }

    public void setLeftBV(String leftBV) {
        this.leftBV = leftBV;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getPayout() {
        return payout;
    }

    public void setPayout(String payout) {
        this.payout = payout;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<DashboardItem> getDashboard() {
        return dashboard;
    }

    public void setDashboard(List<DashboardItem> dashboard) {
        this.dashboard = dashboard;
    }

    public String getCurrentBV() {
        return currentBV;
    }

    public void setCurrentBV(String currentBV) {
        this.currentBV = currentBV;
    }

    public String getRightBV() {
        return rightBV;
    }

    public void setRightBV(String rightBV) {
        this.rightBV = rightBV;
    }

    @Override
    public String toString() {
        return
                "ResponseDashboard{" +
                        "leftBV = '" + leftBV + '\'' +
                        ",response = '" + response + '\'' +
                        ",payout = '" + payout + '\'' +
                        ",label = '" + label + '\'' +
                        ",dashboard = '" + dashboard + '\'' +
                        ",currentBV = '" + currentBV + '\'' +
                        ",rightBV = '" + rightBV + '\'' +
                        "}";
    }
}