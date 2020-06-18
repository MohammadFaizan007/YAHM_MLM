package com.yehm.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyIncomeResponse {

    @SerializedName("response")
    private String response;

    @SerializedName("myincomelist")
    private List<MyincomelistItem> myincomelist;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public List<MyincomelistItem> getMyincomelist() {
        return myincomelist;
    }

    public void setMyincomelist(List<MyincomelistItem> myincomelist) {
        this.myincomelist = myincomelist;
    }

    @Override
    public String toString() {
        return
                "MyIncomeResponse{" +
                        "response = '" + response + '\'' +
                        ",myincomelist = '" + myincomelist + '\'' +
                        "}";
    }
}