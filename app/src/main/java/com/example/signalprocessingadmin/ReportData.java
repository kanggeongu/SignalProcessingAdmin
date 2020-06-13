package com.example.signalprocessingadmin;

public class ReportData {

    private String userEmail;
    private String content;

    public ReportData() {}

    public ReportData(String userEmail, String content) {
        this.userEmail = userEmail;
        this.content = content;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
