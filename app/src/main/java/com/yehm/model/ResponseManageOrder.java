package com.yehm.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseManageOrder {

    @SerializedName("manageOrderlist")
    private List<ManageOrderlistItem> manageOrderlist;

    @SerializedName("response")
    private String response;

    public List<ManageOrderlistItem> getManageOrderlist() {
        return manageOrderlist;
    }

    public void setManageOrderlist(List<ManageOrderlistItem> manageOrderlist) {
        this.manageOrderlist = manageOrderlist;
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
                "ResponseManageOrder{" +
                        "manageOrderlist = '" + manageOrderlist + '\'' +
                        ",response = '" + response + '\'' +
                        "}";
    }
}