package com.mocom.com.mdancingproject.Dao;

public class ImageBannerDao {

    public ImageBannerDao(){

    }

    private String id,title,desc,imageUrl, posterUrl;

    public ImageBannerDao(String id, String title, String desc, String imageUrl, String posterUrl) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.imageUrl = imageUrl;
        this.posterUrl = posterUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }
}
