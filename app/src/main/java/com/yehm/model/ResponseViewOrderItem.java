package com.yehm.model;

import com.google.gson.annotations.SerializedName;

public class ResponseViewOrderItem {

    @SerializedName("manageOrderView")
    private ManageOrderView manageOrderView;

    @SerializedName("response")
    private String response;

    public ManageOrderView getManageOrderView() {
        return manageOrderView;
    }

    public void setManageOrderView(ManageOrderView manageOrderView) {
        this.manageOrderView = manageOrderView;
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
                "ResponseViewOrderItem{" +
                        "manageOrderView = '" + manageOrderView + '\'' +
                        ",response = '" + response + '\'' +
                        "}";
    }
}