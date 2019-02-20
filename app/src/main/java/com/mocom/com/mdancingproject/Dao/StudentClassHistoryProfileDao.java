package com.mocom.com.mdancingproject.Dao;

public class StudentClassHistoryProfileDao {

    public StudentClassHistoryProfileDao() {

    }

    private String eventID, eventTitle, description, playlist, eventStyle, teacher, active, eventDate, eventTime, branch, imgUrl;

    public StudentClassHistoryProfileDao(String eventID, String eventTitle, String description, String playlist, String eventStyle, String teacher, String active, String eventDate, String eventTime, String branch, String imgUrl) {
        this.eventID = eventID;
        this.eventTitle = eventTitle;
        this.description = description;
        this.playlist = playlist;
        this.eventStyle = eventStyle;
        this.teacher = teacher;
        this.active = active;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.branch = branch;
        this.imgUrl = imgUrl;
    }

    public String getEventID() {
        return eventID;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public String getDescription() {
        return description;
    }

    public String getPlaylist() {
        return playlist;
    }

    public String getEventStyle() {
        return eventStyle;
    }

    public String getTeacher() {
        return teacher;
    }

    public String getActive() {
        return active;
    }

    public String getEventDate() {
        return eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public String getBranch() {
        return branch;
    }

    public String getImgUrl() {
        return imgUrl;
    }
}
