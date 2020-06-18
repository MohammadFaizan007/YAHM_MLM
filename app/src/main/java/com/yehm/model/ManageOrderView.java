package com.yehm.model;

import com.google.gson.annotations.SerializedName;

public class ManageOrderView {

    @SerializedName("sgst")
    private String sgst;

    @SerializedName("netAmt")
    private String netAmt;

    @SerializedName("productCode")
    private String productCode;

    @SerializedName("quantity")
    private String quantity;

    @SerializedName("bv")
    private String bv;

    @SerializedName("mrp")
    private String mrp;

    @SerializedName("cgst")
    private String cgst;

    @SerializedName("productName")
    private String productName;

    @SerializedName("igst")
    private String igst;

    public String getSgst() {
        return sgst;
    }

    public void setSgst(String sgst) {
        this.sgst = sgst;
    }

    public String getNetAmt() {
        return netAmt;
    }

    public void setNetAmt(String netAmt) {
        this.netAmt = netAmt;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getBv() {
        return bv;
    }

    public void setBv(String bv) {
        this.bv = bv;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getCgst() {
        return cgst;
    }

    public void setCgst(String cgst) {
        this.cgst = cgst;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getIgst() {
        return igst;
    }

    public void setIgst(String igst) {
        this.igst = igst;
    }

    @Override
    public String toString() {
        return
                "ManageOrderView{" +
                        "sgst = '" + sgst + '\'' +
                        ",netAmt = '" + netAmt + '\'' +
                        ",productCode = '" + productCode + '\'' +
                        ",quantity = '" + quantity + '\'' +
                        ",bv = '" + bv + '\'' +
                        ",mrp = '" + mrp + '\'' +
                        ",cgst = '" + cgst + '\'' +
                        ",productName = '" + productName + '\'' +
                        ",igst = '" + igst + '\'' +
                        "}";
    }
}