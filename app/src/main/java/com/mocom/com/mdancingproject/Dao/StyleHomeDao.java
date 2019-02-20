package com.mocom.com.mdancingproject.Dao;

public class StyleHomeDao {

    private String styleID, styleName, styleDesc, styleImage;

    public StyleHomeDao(String styleID, String styleName, String styleDesc, String styleImage) {
        this.styleID = styleID;
        this.styleName = styleName;
        this.styleDesc = styleDesc;
        this.styleImage = styleImage;
    }

    public String getStyleID() {
        return styleID;
    }

    public String getStyleName() {
        return styleName;
    }

    public String getStyleDesc() {
        return styleDesc;
    }

    public String getStyleImage() {
        return styleImage;
    }
}
