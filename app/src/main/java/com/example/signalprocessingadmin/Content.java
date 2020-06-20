package com.example.signalprocessingadmin;

import java.io.Serializable;
import java.util.ArrayList;

public class Content implements Serializable {

    private String content;
    private String userID;
    private String contentID;
    private ArrayList<String> reporter;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Content(){}

    public Content(String content, String userID, String contentID){
        this.content = content;
        this.userID = userID;
        this.contentID = contentID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<String> getReporter() {
        return reporter;
    }

    public void setReporter(ArrayList<String> reporter) {
        this.reporter = reporter;
    }

    public String getContentID() {
        return contentID;
    }

    public void setContentID(String contentID) {
        this.contentID = contentID;
    }

    public boolean addReporter(String reporterID){
        if(!reporter.contains(reporterID)){
            reporter.add(reporterID);
            return true;
        }
        return false;
    }
}