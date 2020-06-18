package com.yehm.model;

import com.google.gson.annotations.SerializedName;

public class ResponseValidateParent {

    @SerializedName("loginId")
    private String loginId;

    @SerializedName("displayName")
    private String displayName;

    @SerializedName("response")
    private String response;

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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
                "ResponseValidateParent{" +
                        "loginId = '" + loginId + '\'' +
                        ",displayName = '" + displayName + '\'' +
                        ",response = '" + response + '\'' +
                        "}";
    }
}