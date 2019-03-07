package com.mocom.com.mdancingproject.Dao;

public class QRCodeGenForCheckedObject {

    String userID, eventID, checked;

    public QRCodeGenForCheckedObject(String userID, String eventID, String checked) {
        this.userID = userID;
        this.eventID = eventID;
        this.checked = checked;
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

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }
}
