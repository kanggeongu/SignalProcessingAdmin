package com.example.signalprocessingadmin;

public class RestrictedUserData {

    private String userEmail;
    private String endDate;

    RestrictedUserData() {}

    public RestrictedUserData(String userEmail, String endDate) {
        this.userEmail = userEmail;
        this.endDate = endDate;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
