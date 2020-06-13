package com.example.signalprocessingadmin;

public class ReportViewData {

    private String ID;
    private String userEmail;
    private String content;

    public ReportViewData() {}

    public ReportViewData(String ID, String userEmail, String content) {
        this.ID = ID;
        this.userEmail = userEmail;
        this.content = content;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
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
