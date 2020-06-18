package com.yehm.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseSupportList {

    @SerializedName("response")
    private String response;

    @SerializedName("supportTickets")
    private List<SupportTicketsItem> supportTickets;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public List<SupportTicketsItem> getSupportTickets() {
        return supportTickets;
    }

    public void setSupportTickets(List<SupportTicketsItem> supportTickets) {
        this.supportTickets = supportTickets;
    }

    @Override
    public String toString() {
        return
                "ResponseSupportList{" +
                        "response = '" + response + '\'' +
                        ",supportTickets = '" + supportTickets + '\'' +
                        "}";
    }
}