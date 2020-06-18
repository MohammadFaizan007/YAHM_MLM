package com.yehm.model;

import com.google.gson.annotations.SerializedName;

public class ResponseLogin {

    @SerializedName("authenticate")
    private Authenticate authenticate;

    @SerializedName("response")
    private String response;

    public Authenticate getAuthenticate() {
        return authenticate;
    }

    public void setAuthenticate(Authenticate authenticate) {
        this.authenticate = authenticate;
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
                "ResponseLogin{" +
                        "authenticate = '" + authenticate + '\'' +
                        ",response = '" + response + '\'' +
                        "}";
    }
}