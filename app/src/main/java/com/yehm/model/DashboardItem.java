package com.yehm.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DashboardItem {

    @SerializedName("webNewsList")
    private List<WebNewsListItem> webNewsList;

    @SerializedName("myCommunity")
    private List<MyCommunityItem> myCommunity;

    @SerializedName("sectionTitle")
    private String sectionTitle;

    @SerializedName("businessDetails")
    private List<BusinessDetailsItem> businessDetails;

    @SerializedName("payoutStatement")
    private Object payoutStatement;

    @SerializedName("totalBusiness")
    private List<TotalBusinessItem> totalBusiness;

    public List<WebNewsListItem> getWebNewsList() {
        return webNewsList;
    }

    public void setWebNewsList(List<WebNewsListItem> webNewsList) {
        this.webNewsList = webNewsList;
    }

    public List<MyCommunityItem> getMyCommunity() {
        return myCommunity;
    }

    public void setMyCommunity(List<MyCommunityItem> myCommunity) {
        this.myCommunity = myCommunity;
    }

    public String getSectionTitle() {
        return sectionTitle;
    }

    public void setSectionTitle(String sectionTitle) {
        this.sectionTitle = sectionTitle;
    }

    public List<BusinessDetailsItem> getBusinessDetails() {
        return businessDetails;
    }

    public void setBusinessDetails(List<BusinessDetailsItem> businessDetails) {
        this.businessDetails = businessDetails;
    }

    public Object getPayoutStatement() {
        return payoutStatement;
    }

    public void setPayoutStatement(Object payoutStatement) {
        this.payoutStatement = payoutStatement;
    }

    public List<TotalBusinessItem> getTotalBusiness() {
        return totalBusiness;
    }

    public void setTotalBusiness(List<TotalBusinessItem> totalBusiness) {
        this.totalBusiness = totalBusiness;
    }

    @Override
    public String toString() {
        return
                "DashboardItem{" +
                        "webNewsList = '" + webNewsList + '\'' +
                        ",myCommunity = '" + myCommunity + '\'' +
                        ",sectionTitle = '" + sectionTitle + '\'' +
                        ",businessDetails = '" + businessDetails + '\'' +
                        ",payoutStatement = '" + payoutStatement + '\'' +
                        ",totalBusiness = '" + totalBusiness + '\'' +
                        "}";
    }
}