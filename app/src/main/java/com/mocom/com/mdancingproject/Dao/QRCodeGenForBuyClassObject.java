package com.mocom.com.mdancingproject.Dao;

public class QRCodeGenForBuyClassObject {

    private String userName, userID, eventID, eventName, coin, baht;

    public QRCodeGenForBuyClassObject(String userName, String userID, String eventID, String eventName, String coin, String baht) {
        this.userName = userName;
        this.userID = userID;
        this.eventID = eventID;
        this.eventName = eventName;
        this.coin = coin;
        this.baht = baht;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public String getBaht() {
        return baht;
    }

    public void setBaht(String baht) {
        this.baht = baht;
    }
}
