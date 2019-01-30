package com.mocom.com.mdancingproject.Dao;

public class StudentClassHomeDao {
    public StudentClassHomeDao() {

    }

    private String eventID, eventStart, eventEnd, description, title, course, imgUrl, playlistTitle, courseStyleName;

    public StudentClassHomeDao(String eventID, String eventStart, String eventEnd, String description, String title, String course, String imgUrl, String playlistTitle, String courseStyleName) {
        this.eventID = eventID;
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
        this.description = description;
        this.title = title;
        this.course = course;
        this.imgUrl = imgUrl;
        this.playlistTitle = playlistTitle;
        this.courseStyleName = courseStyleName;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getEventStart() {
        return eventStart;
    }

    public void setEventStart(String eventStart) {
        this.eventStart = eventStart;
    }

    public String getEventEnd() {
        return eventEnd;
    }

    public void setEventEnd(String eventEnd) {
        this.eventEnd = eventEnd;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getPlaylistTitle() {
        return playlistTitle;
    }

    public void setPlaylistTitle(String playlistTitle) {
        this.playlistTitle = playlistTitle;
    }

    public String getCourseStyleName() {
        return courseStyleName;
    }

    public void setCourseStyleName(String courseStyleName) {
        this.courseStyleName = courseStyleName;
    }
}
