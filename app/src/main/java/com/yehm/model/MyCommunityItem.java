package com.yehm.model;

import com.google.gson.annotations.SerializedName;

public class MyCommunityItem {

    @SerializedName("sponsorUserId")
    private String sponsorUserId;

    @SerializedName("name")
    private String name;

    @SerializedName("sponsorName")
    private String sponsorName;

    @SerializedName("userId")
    private String userId;

    @SerializedName("phoneNo")
    private String phoneNo;

    public String getSponsorUserId() {
        return sponsorUserId;
    }

    public void setSponsorUserId(String sponsorUserId) {
        this.sponsorUserId = sponsorUserId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSponsorName() {
        return sponsorName;
    }

    public void setSponsorName(String sponsorName) {
        this.sponsorName = sponsorName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    @Override
    public String toString() {
        return
                "MyCommunityItem{" +
                        "sponsorUserId = '" + sponsorUserId + '\'' +
                        ",name = '" + name + '\'' +
                        ",sponsorName = '" + sponsorName + '\'' +
                        ",userId = '" + userId + '\'' +
                        ",phoneNo = '" + phoneNo + '\'' +
                        "}";
    }
}