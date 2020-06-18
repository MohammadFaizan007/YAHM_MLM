package com.yehm.model;

import com.google.gson.annotations.SerializedName;

public class ResponseSupportAddRequest {

    @SerializedName("response")
    private String response;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return
                "ResponseSupportAddRequest{" +
                        "response = '" + response + '\'' +
                        "}";
    }
}