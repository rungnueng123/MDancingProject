package com.mocom.com.mdancingproject.Dao;

public class StudentCoinPackageDao {

    public StudentCoinPackageDao(){

    }

    private String coinPackID, namePack, bath, coin, imgUrl;

    public StudentCoinPackageDao(String coinPackID, String namePack, String bath, String coin, String imgUrl) {
        this.coinPackID = coinPackID;
        this.namePack = namePack;
        this.bath = bath;
        this.coin = coin;
        this.imgUrl = imgUrl;
    }

    public String getCoinPackID() {
        return coinPackID;
    }

    public String getNamePack() {
        return namePack;
    }

    public String getBath() {
        return bath;
    }

    public String getCoin() {
        return coin;
    }

    public String getImgUrl() {
        return imgUrl;
    }
}
