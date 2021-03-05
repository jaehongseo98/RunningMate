package com.example.project;
// 채팅 클릭시 보여질 프로필 사진과 이름과 대화내역
public class Chat {

    int icon;
    String name;
    String message;

    public Chat(int icon, String name, String message) {
        this.icon = icon;
        this.name = name;
        this.message = message;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
