package com.mocom.com.mdancingproject.Dao;

public class QRCodeStudentGenObject {

    private String secret_key, baht, coinAmt;

    public QRCodeStudentGenObject(String secret_key, String baht, String coinAmt) {
        this.secret_key = secret_key;
        this.baht = baht;
        this.coinAmt = coinAmt;
    }

    public String getSecret_key() {
        return secret_key;
    }

    public void setSecret_key(String secret_key) {
        this.secret_key = secret_key;
    }

    public String getBaht() {
        return baht;
    }

    public void setBaht(String baht) {
        this.baht = baht;
    }

    public String getCoinAmt() {
        return coinAmt;
    }

    public void setCoinAmt(String coinAmt) {
        this.coinAmt = coinAmt;
    }
}
