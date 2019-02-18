package com.mocom.com.mdancingproject.Dao;

public class StudentCourseClassDao {

    public StudentCourseClassDao() {

    }

    private String eventID, imgUrl, eventTitle, playlist, eventStyle, eventTeacher, eventDate, eventTime, eventEmpty, eventBranch, eventDesc, coin;

    public StudentCourseClassDao(String eventID, String imgUrl, String eventTitle, String playlist, String eventStyle, String eventTeacher, String eventDate, String eventTime, String eventEmpty, String eventBranch, String eventDesc, String coin) {
        this.eventID = eventID;
        this.imgUrl = imgUrl;
        this.eventTitle = eventTitle;
        this.playlist = playlist;
        this.eventStyle = eventStyle;
        this.eventTeacher = eventTeacher;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.eventEmpty = eventEmpty;
        this.eventBranch = eventBranch;
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

    public String getEventStyle() {
        return eventStyle;
    }

    public String getEventTeacher() {
        return eventTeacher;
    }

    public String getEventDate() {
        return eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public String getEventEmpty() {
        return eventEmpty;
    }

    public String getEventBranch() {
        return eventBranch;
    }

    public String getEventDesc() {
        return eventDesc;
    }

    public String getCoin() {
        return coin;
    }
}
