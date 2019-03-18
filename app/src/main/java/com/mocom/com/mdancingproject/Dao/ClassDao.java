package com.mocom.com.mdancingproject.Dao;

public class ClassDao {

    public ClassDao(){

    }

    private String eventID, eventStart, eventEnd, title, courseID, imgUrl, playlistTitle, courseStyleName, teacher;

    public ClassDao(String eventID, String eventStart, String eventEnd, String title, String courseID, String imgUrl, String playlistTitle, String courseStyleName, String teacher) {
        this.eventID = eventID;
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
        this.title = title;
        this.courseID = courseID;
        this.imgUrl = imgUrl;
        this.playlistTitle = playlistTitle;
        this.courseStyleName = courseStyleName;
        this.teacher = teacher;
    }

    public String getEventID() {
        return eventID;
    }

    public String getEventStart() {
        return eventStart;
    }

    public String getEventEnd() {
        return eventEnd;
    }

    public String getTitle() {
        return title;
    }

    public String getCourseID() {
        return courseID;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getPlaylistTitle() {
        return playlistTitle;
    }

    public String getCourseStyleName() {
        return courseStyleName;
    }

    public String getTeacher() {
        return teacher;
    }
}
