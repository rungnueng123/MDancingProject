package com.mocom.com.mdancingproject.Dao;

public class StyleHomeDao {

    private String styleID, styleName, styleDesc, styleImage, styleSelect;

    public StyleHomeDao(String styleID, String styleName, String styleDesc, String styleImage, String styleSelect) {
        this.styleID = styleID;
        this.styleName = styleName;
        this.styleDesc = styleDesc;
        this.styleImage = styleImage;
        this.styleSelect = styleSelect;
    }

    public String getStyleID() {
        return styleID;
    }

    public void setStyleID(String styleID) {
        this.styleID = styleID;
    }

    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    public String getStyleDesc() {
        return styleDesc;
    }

    public void setStyleDesc(String styleDesc) {
        this.styleDesc = styleDesc;
    }

    public String getStyleImage() {
        return styleImage;
    }

    public void setStyleImage(String styleImage) {
        this.styleImage = styleImage;
    }

    public String getStyleSelect() {
        return styleSelect;
    }

    public void setStyleSelect(String styleSelect) {
        this.styleSelect = styleSelect;
    }
}
