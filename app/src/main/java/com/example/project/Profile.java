package com.example.project;
// 전체친구 클릭시 보여질 프로필 사진과 이름
public class Profile {
    int icon;
    String name;

    public Profile(int icon, String name) {
        this.icon = icon;
        this.name = name;
    }


    public Profile(){}

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}