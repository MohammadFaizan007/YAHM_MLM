package com.yehm.model;

import com.google.gson.annotations.SerializedName;

public class ResponseUpdateMemberPersonalDetails {

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
                "ResponseUpdateMemberPersonalDetails{" +
                        "response = '" + response + '\'' +
                        "}";
    }
}