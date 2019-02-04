package com.mocom.com.mdancingproject.Dao;

public class StudentCourseClassDao {

    public StudentCourseClassDao() {

    }

    private String eventID, imgUrl, eventTitle, playlist, eventDate, eventTime, eventDesc, coin;

    public StudentCourseClassDao(String eventID, String imgUrl, String eventTitle, String playlist, String eventDate, String eventTime, String eventDesc, String coin) {
        this.eventID = eventID;
        this.imgUrl = imgUrl;
        this.eventTitle = eventTitle;
        this.playlist = playlist;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.eventDesc = eventDesc;
        this.coin = coin;
    }

    public String getEventID() {
        return eventID;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public String getPlaylist() {
        return playlist;
    }

    public String getEventDate() {
        return eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public String getEventDesc() {
        return eventDesc;
    }

    public String getCoin() {
        return coin;
    }
}
