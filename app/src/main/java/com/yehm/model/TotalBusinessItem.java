package com.yehm.model;

import com.google.gson.annotations.SerializedName;

public class TotalBusinessItem {

    @SerializedName("loginId")
    private String loginId;

    @SerializedName("displayName")
    private String displayName;

    @SerializedName("joiningDate")
    private String joiningDate;

    @SerializedName("permanentDate")
    private String permanentDate;

    @SerializedName("leg")
    private String leg;

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

    public String getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(String joiningDate) {
        this.joiningDate = joiningDate;
    }

    public String getPermanentDate() {
        return permanentDate;
    }

    public void setPermanentDate(String permanentDate) {
        this.permanentDate = permanentDate;
    }

    public String getLeg() {
        return leg;
    }

    public void setLeg(String leg) {
        this.leg = leg;
    }

    @Override
    public String toString() {
        return
                "TotalBusinessItem{" +
                        "loginId = '" + loginId + '\'' +
                        ",displayName = '" + displayName + '\'' +
                        ",joiningDate = '" + joiningDate + '\'' +
                        ",permanentDate = '" + permanentDate + '\'' +
                        ",leg = '" + leg + '\'' +
                        "}";
    }
}