package com.yehm.model;

import com.google.gson.annotations.SerializedName;

public class Authenticate {

    @SerializedName("firstName")
    private String firstName;

    @SerializedName("loginId")
    private String loginId;

    @SerializedName("isblocked")
    private String isblocked;

    @SerializedName("profilepic")
    private String profilepic;

    @SerializedName("doa")
    private String doa;

    @SerializedName("mobile")
    private String mobile;

    @SerializedName("tempPermanent")
    private String tempPermanent;

    @SerializedName("fk_MemID")
    private String fkMemID;

    @SerializedName("doj")
    private String doj;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
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

    public String getDoa() {
        return doa;
    }

    public void setDoa(String doa) {
        this.doa = doa;
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

    public String getDoj() {
        return doj;
    }

    public void setDoj(String doj) {
        this.doj = doj;
    }

    @Override
    public String toString() {
        return
                "Authenticate{" +
                        "firstName = '" + firstName + '\'' +
                        ",loginId = '" + loginId + '\'' +
                        ",isblocked = '" + isblocked + '\'' +
                        ",profilepic = '" + profilepic + '\'' +
                        ",doa = '" + doa + '\'' +
                        ",mobile = '" + mobile + '\'' +
                        ",tempPermanent = '" + tempPermanent + '\'' +
                        ",fk_MemID = '" + fkMemID + '\'' +
                        ",doj = '" + doj + '\'' +
                        "}";
    }
}