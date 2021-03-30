package com.example.project;

public class ChatData {

    private String name;
    private String msg;
    String time;
    String profileUrl;

    public ChatData(String name, String msg, String time, String profileUrl) {
        this.name = name;
        this.msg = msg;
        this.time = time;
        this.profileUrl = profileUrl;
    }

    public ChatData(){}

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }
}
