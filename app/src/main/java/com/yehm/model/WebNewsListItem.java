package com.yehm.model;

import com.google.gson.annotations.SerializedName;

public class WebNewsListItem {

    @SerializedName("news")
    private String news;

    @SerializedName("newsPreference")
    private String newsPreference;

    @SerializedName("createdDate")
    private String createdDate;

    @SerializedName("newsHeading")
    private String newsHeading;

    @SerializedName("pk_NewsId")
    private String pkNewsId;

    public String getNews() {
        return news;
    }

    public void setNews(String news) {
        this.news = news;
    }

    public String getNewsPreference() {
        return newsPreference;
    }

    public void setNewsPreference(String newsPreference) {
        this.newsPreference = newsPreference;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getNewsHeading() {
        return newsHeading;
    }

    public void setNewsHeading(String newsHeading) {
        this.newsHeading = newsHeading;
    }

    public String getPkNewsId() {
        return pkNewsId;
    }

    public void setPkNewsId(String pkNewsId) {
        this.pkNewsId = pkNewsId;
    }

    @Override
    public String toString() {
        return
                "WebNewsListItem{" +
                        "news = '" + news + '\'' +
                        ",newsPreference = '" + newsPreference + '\'' +
                        ",createdDate = '" + createdDate + '\'' +
                        ",newsHeading = '" + newsHeading + '\'' +
                        ",pk_NewsId = '" + pkNewsId + '\'' +
                        "}";
    }
}