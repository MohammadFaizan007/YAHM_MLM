package com.yehm.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EWalletLedgerResponse {

    @SerializedName("eWalletLedgerlist")
    private List<EWalletLedgerlistItem> eWalletLedgerlist;

    @SerializedName("response")
    private String response;

    public List<EWalletLedgerlistItem> getEWalletLedgerlist() {
        return eWalletLedgerlist;
    }

    public void setEWalletLedgerlist(List<EWalletLedgerlistItem> eWalletLedgerlist) {
        this.eWalletLedgerlist = eWalletLedgerlist;
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
                "EWalletLedgerResponse{" +
                        "eWalletLedgerlist = '" + eWalletLedgerlist + '\'' +
                        ",response = '" + response + '\'' +
                        "}";
    }
}