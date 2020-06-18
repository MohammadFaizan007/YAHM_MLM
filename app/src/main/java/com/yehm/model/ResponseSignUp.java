package com.yehm.model;

import com.google.gson.annotations.SerializedName;

public class ResponseSignUp {

    @SerializedName("firstName")
    private Object firstName;

    @SerializedName("password")
    private Object password;

    @SerializedName("loginId")
    private Object loginId;

    @SerializedName("response")
    private String response;

    @SerializedName("mobile")
    private Object mobile;

    @SerializedName("tPassword")
    private Object tPassword;

    public Object getFirstName() {
        return firstName;
    }

    public void setFirstName(Object firstName) {
        this.firstName = firstName;
    }

    public Object getPassword() {
        return password;
    }

    public void setPassword(Object password) {
        this.password = password;
    }

    public Object getLoginId() {
        return loginId;
    }

    public void setLoginId(Object loginId) {
        this.loginId = loginId;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Object getMobile() {
        return mobile;
    }

    public void setMobile(Object mobile) {
        this.mobile = mobile;
    }

    public Object getTPassword() {
        return tPassword;
    }

    public void setTPassword(Object tPassword) {
        this.tPassword = tPassword;
    }

    @Override
    public String toString() {
        return
                "ResponseSignUp{" +
                        "firstName = '" + firstName + '\'' +
                        ",password = '" + password + '\'' +
                        ",loginId = '" + loginId + '\'' +
                        ",response = '" + response + '\'' +
                        ",mobile = '" + mobile + '\'' +
                        ",tPassword = '" + tPassword + '\'' +
                        "}";
    }
}