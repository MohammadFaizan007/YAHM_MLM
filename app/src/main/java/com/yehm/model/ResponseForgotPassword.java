package com.yehm.model;

import com.google.gson.annotations.SerializedName;

public class ResponseForgotPassword {

    @SerializedName("password")
    private String password;

    @SerializedName("loginId")
    private String loginId;

    @SerializedName("response")
    private String response;

    @SerializedName("name")
    private String name;

    @SerializedName("mobileNo")
    private String mobileNo;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    @Override
    public String toString() {
        return
                "ResponseForgotPassword{" +
                        "password = '" + password + '\'' +
                        ",loginId = '" + loginId + '\'' +
                        ",response = '" + response + '\'' +
                        ",name = '" + name + '\'' +
                        ",mobileNo = '" + mobileNo + '\'' +
                        "}";
    }
}