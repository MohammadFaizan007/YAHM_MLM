package com.yehm.model;

import com.google.gson.annotations.SerializedName;

public class ManageOrderlistItem {

    @SerializedName("orderNo")
    private String orderNo;

    @SerializedName("orderId")
    private String orderId;

    @SerializedName("netAmount")
    private String netAmount;

    @SerializedName("name")
    private String name;

    @SerializedName("orderDate")
    private String orderDate;

    @SerializedName("businessVolume")
    private String businessVolume;

    @SerializedName("status")
    private String status;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(String netAmount) {
        this.netAmount = netAmount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getBusinessVolume() {
        return businessVolume;
    }

    public void setBusinessVolume(String businessVolume) {
        this.businessVolume = businessVolume;
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
                "ManageOrderlistItem{" +
                        "orderNo = '" + orderNo + '\'' +
                        ",orderId = '" + orderId + '\'' +
                        ",netAmount = '" + netAmount + '\'' +
                        ",name = '" + name + '\'' +
                        ",orderDate = '" + orderDate + '\'' +
                        ",businessVolume = '" + businessVolume + '\'' +
                        ",status = '" + status + '\'' +
                        "}";
    }
}