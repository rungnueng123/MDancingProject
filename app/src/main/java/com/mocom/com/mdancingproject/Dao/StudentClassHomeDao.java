package com.mocom.com.mdancingproject.Dao;

public class StudentClassHomeDao {
    public StudentClassHomeDao() {

    }

    private String eventID, eventStart, eventEnd, description, title, courseID, course, imgUrl, playlistTitle, courseStyleName;

    public StudentClassHomeDao(String eventID, String eventStart, String eventEnd, String description, String title, String courseID, String course, String imgUrl, String playlistTitle, String courseStyleName) {
        this.eventID = eventID;
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
        this.description = description;
        this.title = title;
        this.courseID = courseID;
        this.course = course;
        this.imgUrl = imgUrl;
        this.playlistTitle = playlistTitle;
        this.courseStyleName = courseStyleName;
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

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getCourseID() {
        return courseID;
    }

    public String getCourse() {
        return course;
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
}
