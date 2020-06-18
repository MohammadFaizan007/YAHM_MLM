package com.yehm.model;

import com.google.gson.annotations.SerializedName;

public class SupportTicketsItem {

    @SerializedName("ticketNo")
    private String ticketNo;

    @SerializedName("replyOn")
    private String replyOn;

    @SerializedName("replyMessage")
    private String replyMessage;

    @SerializedName("subject")
    private String subject;

    @SerializedName("requestDate")
    private String requestDate;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private String status;

    public String getTicketNo() {
        return ticketNo;
    }

    public void setTicketNo(String ticketNo) {
        this.ticketNo = ticketNo;
    }

    public String getReplyOn() {
        return replyOn;
    }

    public void setReplyOn(String replyOn) {
        this.replyOn = replyOn;
    }

    public String getReplyMessage() {
        return replyMessage;
    }

    public void setReplyMessage(String replyMessage) {
        this.replyMessage = replyMessage;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
                "SupportTicketsItem{" +
                        "ticketNo = '" + ticketNo + '\'' +
                        ",replyOn = '" + replyOn + '\'' +
                        ",replyMessage = '" + replyMessage + '\'' +
                        ",subject = '" + subject + '\'' +
                        ",requestDate = '" + requestDate + '\'' +
                        ",message = '" + message + '\'' +
                        ",status = '" + status + '\'' +
                        "}";
    }
}