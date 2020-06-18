package com.yehm.model;

import com.google.gson.annotations.SerializedName;

public class ProductlistItem {

    @SerializedName("image")
    private String image;

    @SerializedName("bv")
    private String bv;

    @SerializedName("mrp")
    private String mrp;

    @SerializedName("pK_ProductId")
    private String pKProductId;

    @SerializedName("productName")
    private String productName;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getPKProductId() {
        return pKProductId;
    }

    public void setPKProductId(String pKProductId) {
        this.pKProductId = pKProductId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Override
    public String toString() {
        return
                "ProductlistItem{" +
                        "image = '" + image + '\'' +
                        ",bv = '" + bv + '\'' +
                        ",mrp = '" + mrp + '\'' +
                        ",pK_ProductId = '" + pKProductId + '\'' +
                        ",productName = '" + productName + '\'' +
                        "}";
    }
}