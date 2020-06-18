package com.yehm.model;

import com.google.gson.annotations.SerializedName;

public class DownlineListItem {

    @SerializedName("bv")
    private String bv;

    @SerializedName("sponsorId")
    private String sponsorId;

    @SerializedName("sponsorName")
    private String sponsorName;

    @SerializedName("memberName")
    private String memberName;

    @SerializedName("joiningDate")
    private String joiningDate;

    @SerializedName("packageName")
    private String packageName;

    @SerializedName("memberId")
    private String memberId;

    @SerializedName("packageAmount")
    private String packageAmount;

    @SerializedName("status")
    private String status;

    public String getBv() {
        return bv;
    }

    public void setBv(String bv) {
        this.bv = bv;
    }

    public String getSponsorId() {
        return sponsorId;
    }

    public void setSponsorId(String sponsorId) {
        this.sponsorId = sponsorId;
    }

    public String getSponsorName() {
        return sponsorName;
    }

    public void setSponsorName(String sponsorName) {
        this.sponsorName = sponsorName;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(String joiningDate) {
        this.joiningDate = joiningDate;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getPackageAmount() {
        return packageAmount;
    }

    public void setPackageAmount(String packageAmount) {
        this.packageAmount = packageAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return
                "DownlineListItem{" +
                        "bv = '" + bv + '\'' +
                        ",sponsorId = '" + sponsorId + '\'' +
                        ",sponsorName = '" + sponsorName + '\'' +
                        ",memberName = '" + memberName + '\'' +
                        ",joiningDate = '" + joiningDate + '\'' +
                        ",packageName = '" + packageName + '\'' +
                        ",memberId = '" + memberId + '\'' +
                        ",packageAmount = '" + packageAmount + '\'' +
                        ",status = '" + status + '\'' +
                        "}";
    }
}