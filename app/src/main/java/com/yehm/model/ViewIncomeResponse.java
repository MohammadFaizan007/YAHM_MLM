package com.yehm.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ViewIncomeResponse {

    @SerializedName("payoutdetailslist")
    private List<PayoutdetailslistItem> payoutdetailslist;

    @SerializedName("response")
    private String response;

    public List<PayoutdetailslistItem> getPayoutdetailslist() {
        return payoutdetailslist;
    }

    public void setPayoutdetailslist(List<PayoutdetailslistItem> payoutdetailslist) {
        this.payoutdetailslist = payoutdetailslist;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return
                "ViewIncomeResponse{" +
                        "payoutdetailslist = '" + payoutdetailslist + '\'' +
                        ",response = '" + response + '\'' +
                        "}";
    }
}