package com.yehm.model;

import com.google.gson.annotations.SerializedName;

public class ResponseSponsorName {

    @SerializedName("response")
    private String response;

    @SerializedName("memberName")
    private String memberName;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    @Override
    public String toString() {
        return
                "ResponseSponsorName{" +
                        "response = '" + response + '\'' +
                        ",memberName = '" + memberName + '\'' +
                        "}";
    }
}