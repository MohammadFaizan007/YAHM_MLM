package com.yehm.model;

import com.google.gson.annotations.SerializedName;

public class Authentication {

    @SerializedName("firstName")
    private String firstName;

    @SerializedName("loginID")
    private String loginID;

    @SerializedName("isblocked")
    private String isblocked;

    @SerializedName("profilepic")
    private String profilepic;

    @SerializedName("mobile")
    private String mobile;

    @SerializedName("tempPermanent")
    private String tempPermanent;

    @SerializedName("fk_MemID")
    private String fkMemID;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLoginID() {
        return loginID;
    }

    public void setLoginID(String loginID) {
        this.loginID = loginID;
    }

    public String getIsblocked() {
        return isblocked;
    }

    public void setIsblocked(String isblocked) {
        this.isblocked = isblocked;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTempPermanent() {
        return tempPermanent;
    }

    public void setTempPermanent(String tempPermanent) {
        this.tempPermanent = tempPermanent;
    }

    public String getFkMemID() {
        return fkMemID;
    }

    public void setFkMemID(String fkMemID) {
        this.fkMemID = fkMemID;
    }

    @Override
    public String toString() {
        return
                "Authentication{" +
                        "firstName = '" + firstName + '\'' +
                        ",loginID = '" + loginID + '\'' +
                        ",isblocked = '" + isblocked + '\'' +
                        ",profilepic = '" + profilepic + '\'' +
                        ",mobile = '" + mobile + '\'' +
                        ",tempPermanent = '" + tempPermanent + '\'' +
                        ",fk_MemID = '" + fkMemID + '\'' +
                        "}";
    }
}