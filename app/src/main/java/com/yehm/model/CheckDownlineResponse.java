package com.yehm.model;

import com.google.gson.annotations.SerializedName;

public class CheckDownlineResponse {

    @SerializedName("msg")
    private Object msg;

    @SerializedName("loginId")
    private String loginId;

    @SerializedName("searchLoginId")
    private Object searchLoginId;

    @SerializedName("response")
    private String response;

    @SerializedName("parentId")
    private Object parentId;

    @SerializedName("status")
    private String status;

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public Object getSearchLoginId() {
        return searchLoginId;
    }

    public void setSearchLoginId(Object searchLoginId) {
        this.searchLoginId = searchLoginId;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Object getParentId() {
        return parentId;
    }

    public void setParentId(Object parentId) {
        this.parentId = parentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return
                "CheckDownlineResponse{" +
                        "msg = '" + msg + '\'' +
                        ",loginId = '" + loginId + '\'' +
                        ",searchLoginId = '" + searchLoginId + '\'' +
                        ",response = '" + response + '\'' +
                        ",parentId = '" + parentId + '\'' +
                        ",status = '" + status + '\'' +
                        "}";
    }
}