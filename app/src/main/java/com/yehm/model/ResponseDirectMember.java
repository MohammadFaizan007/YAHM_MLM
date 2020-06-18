package com.yehm.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseDirectMember {

    @SerializedName("directMembers")
    private List<DirectMembersItem> directMembers;

    @SerializedName("response")
    private String response;

    public List<DirectMembersItem> getDirectMembers() {
        return directMembers;
    }

    public void setDirectMembers(List<DirectMembersItem> directMembers) {
        this.directMembers = directMembers;
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
                "ResponseDirectMember{" +
                        "directMembers = '" + directMembers + '\'' +
                        ",response = '" + response + '\'' +
                        "}";
    }
}