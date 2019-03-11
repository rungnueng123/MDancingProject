package com.mocom.com.mdancingproject.Dao;

public class NameCheckedDao {

    public NameCheckedDao(){

    }

    String stuName;

    public NameCheckedDao(String stuName) {
        this.stuName = stuName;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }
}
