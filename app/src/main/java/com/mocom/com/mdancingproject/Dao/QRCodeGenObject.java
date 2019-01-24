package com.mocom.com.mdancingproject.Dao;

public class QRCodeGenObject {

    private String fullName;
    private Integer age;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public QRCodeGenObject(String fullName, Integer age) {
        this.fullName = fullName;
        this.age = age;
    }
}
