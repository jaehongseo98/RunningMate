package com.example.project;

// 전체친구 클릭시 보여질 프로필 사진과 이름
public class Profile {
    String profileUrl;
    String name;

    public Profile(String profileUrl, String name) {
        this.profileUrl = profileUrl;
        this.name = name;
    }

    public Profile(){}

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}