package com.example.project;

import java.util.ArrayList;

// 채팅 클릭시 보여질 프로필 사진과 이름과 대화내역
public class Chat {

    int icon;
    String name;
    String msg;

    public Chat(String name, String msg) {
        this.name = name;
        this.msg = msg;
    }

    public Chat(){}

    public Chat(int icon, String name, String msg) {
        this.icon = icon;
        this.name = name;
        this.msg = msg;
    }

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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
