package com.mocom.com.mdancingproject.Dao;

public class YoutubeSuggestDao {

    private String title, youtubeUrl, author;

    public YoutubeSuggestDao(String title, String youtubeUrl, String author) {
        this.title = title;
        this.youtubeUrl = youtubeUrl;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYoutubeUrl() {
        return youtubeUrl;
    }

    public void setYoutubeUrl(String youtubeUrl) {
        this.youtubeUrl = youtubeUrl;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
