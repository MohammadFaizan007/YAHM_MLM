package com.yehm.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseTreeView {

    @SerializedName("genealogytreelist")
    private List<GenealogytreelistItem> genealogytreelist;

    @SerializedName("response")
    private String response;

    public List<GenealogytreelistItem> getGenealogytreelist() {
        return genealogytreelist;
    }

    public void setGenealogytreelist(List<GenealogytreelistItem> genealogytreelist) {
        this.genealogytreelist = genealogytreelist;
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
                "ResponseTreeView{" +
                        "genealogytreelist = '" + genealogytreelist + '\'' +
                        ",response = '" + response + '\'' +
                        "}";
    }
}