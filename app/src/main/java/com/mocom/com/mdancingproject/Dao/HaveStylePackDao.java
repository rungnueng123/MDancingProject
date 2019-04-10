package com.mocom.com.mdancingproject.Dao;

public class HaveStylePackDao {

    private String stylePack, styleName, haveTime, imgUrl;

    public HaveStylePackDao(String stylePack, String styleName, String haveTime, String imgUrl) {
        this.stylePack = stylePack;
        this.styleName = styleName;
        this.haveTime = haveTime;
        this.imgUrl = imgUrl;
    }

    public String getStylePack() {
        return stylePack;
    }

    public void setStylePack(String stylePack) {
        this.stylePack = stylePack;
    }

    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    public String getHaveTime() {
        return haveTime;
    }

    public void setHaveTime(String haveTime) {
        this.haveTime = haveTime;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
