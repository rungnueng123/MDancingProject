package com.mocom.com.mdancingproject.Dao;

public class StudentEventHomeDao {

    public StudentEventHomeDao(){

    }

    private Integer year, month, day;

    public StudentEventHomeDao(Integer year, Integer month, Integer day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public Integer getYear() {
        return year;
    }

    public Integer getMonth() {
        return month;
    }

    public Integer getDay() {
        return day;
    }
}
