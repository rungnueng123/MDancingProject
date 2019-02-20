package com.mocom.com.mdancingproject.Dao;

public class CourseHomeDao {

    private String imgUrl, courseID, courseName, courseStyle, courseDesc;

    public CourseHomeDao(String imgUrl, String courseID, String courseName, String courseStyle, String courseDesc) {
        this.imgUrl = imgUrl;
        this.courseID = courseID;
        this.courseName = courseName;
        this.courseStyle = courseStyle;
        this.courseDesc = courseDesc;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getCourseID() {
        return courseID;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseStyle() {
        return courseStyle;
    }

    public String getCourseDesc() {
        return courseDesc;
    }
}
