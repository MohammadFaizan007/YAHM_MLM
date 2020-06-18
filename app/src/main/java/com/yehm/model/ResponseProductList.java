package com.yehm.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseProductList {

    @SerializedName("response")
    private String response;

    @SerializedName("productlist")
    private List<ProductlistItem> productlist;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public List<ProductlistItem> getProductlist() {
        return productlist;
    }

    public void setProductlist(List<ProductlistItem> productlist) {
        this.productlist = productlist;
    }

    @Override
    public String toString() {
        return
                "ResponseProductList{" +
                        "response = '" + response + '\'' +
                        ",productlist = '" + productlist + '\'' +
                        "}";
    }
}