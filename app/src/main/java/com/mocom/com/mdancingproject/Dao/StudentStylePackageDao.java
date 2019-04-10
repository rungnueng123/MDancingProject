package com.mocom.com.mdancingproject.Dao;

public class StudentStylePackageDao {

    public StudentStylePackageDao(){

    }

    private String stylePackID, namePack, styleID, style, coin, times, imgUrl;

    public StudentStylePackageDao(String stylePackID, String namePack, String styleID, String style, String coin, String times, String imgUrl) {
        this.stylePackID = stylePackID;
        this.namePack = namePack;
        this.styleID = styleID;
        this.style = style;
        this.coin = coin;
        this.times = times;
        this.imgUrl = imgUrl;
    }

    public String getStylePackID() {
        return stylePackID;
    }

    public String getNamePack() {
        return namePack;
    }

    public String getStyleID() {
        return styleID;
    }

    public String getStyle() {
        return style;
    }

    public String getCoin() {
        return coin;
    }

    public String getTimes() {
        return times;
    }

    public String getImgUrl() {
        return imgUrl;
    }
}
