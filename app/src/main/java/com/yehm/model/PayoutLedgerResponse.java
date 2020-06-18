package com.yehm.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PayoutLedgerResponse {

    @SerializedName("response")
    private String response;

    @SerializedName("payoutRequestlist")
    private List<PayoutRequestlistItem> payoutRequestlist;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public List<PayoutRequestlistItem> getPayoutRequestlist() {
        return payoutRequestlist;
    }

    public void setPayoutRequestlist(List<PayoutRequestlistItem> payoutRequestlist) {
        this.payoutRequestlist = payoutRequestlist;
    }

    @Override
    public String toString() {
        return
                "PayoutLedgerResponse{" +
                        "response = '" + response + '\'' +
                        ",payoutRequestlist = '" + payoutRequestlist + '\'' +
                        "}";
    }
}