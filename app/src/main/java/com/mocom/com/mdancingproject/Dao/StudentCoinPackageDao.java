package com.mocom.com.mdancingproject.Dao;

public class StudentCoinPackageDao {

    public StudentCoinPackageDao(){

    }

    private String coinPackID, namePack, baht, coin, imgUrl;

    public StudentCoinPackageDao(String coinPackID, String namePack, String baht, String coin, String imgUrl) {
        this.coinPackID = coinPackID;
        this.namePack = namePack;
        this.baht = baht;
        this.coin = coin;
        this.imgUrl = imgUrl;
    }

    public String getCoinPackID() {
        return coinPackID;
    }

    public String getNamePack() {
        return namePack;
    }

    public String getBaht() {
        return baht;
    }

    public String getCoin() {
        return coin;
    }

    public String getImgUrl() {
        return imgUrl;
    }
}
