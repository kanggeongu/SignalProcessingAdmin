package com.example.signalprocessingadmin;

public class RestrictedUserData {

    private String userEmail;
    private int endDate;

    RestrictedUserData() {}

    public RestrictedUserData(String userEmail, int endDate) {
        this.userEmail = userEmail;
        this.endDate = endDate;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public int getEndDate() {
        return endDate;
    }

    public void setEndDate(int endDate) {
        this.endDate = endDate;
    }
}
