package com.yehm.model;

import com.google.gson.annotations.SerializedName;

public class ResponseViewProfile {

    @SerializedName("response")
    private String response;

    @SerializedName("apiUserProfile")
    private ApiUserProfile apiUserProfile;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public ApiUserProfile getApiUserProfile() {
        return apiUserProfile;
    }

    public void setApiUserProfile(ApiUserProfile apiUserProfile) {
        this.apiUserProfile = apiUserProfile;
    }

    @Override
    public String toString() {
        return
                "ResponseViewProfile{" +
                        "response = '" + response + '\'' +
                        ",apiUserProfile = '" + apiUserProfile + '\'' +
                        "}";
    }
}