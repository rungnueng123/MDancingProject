package com.mocom.com.mdancingproject.Dao;

public class LoginDao {
    public LoginDao() {

    }

    private String UserID;
    private String User;
    private String Email;
    private String GroupID;
    private String Groups;

    public LoginDao(String userID, String user, String email, String groupID, String groups) {
        UserID = userID;
        User = user;
        Email = email;
        GroupID = groupID;
        Groups = groups;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getGroupID() {
        return GroupID;
    }

    public void setGroupID(String groupID) {
        GroupID = groupID;
    }

    public String getGroups() {
        return Groups;
    }

    public void setGroups(String groups) {
        Groups = groups;
    }
}
