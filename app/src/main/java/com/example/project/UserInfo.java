package com.example.project;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;


public class UserInfo {
    private static UserInfo instance = new UserInfo();
    public String id;
    public String pw;
    public String nikname;
    public String birth;
    public String email;
    public String address;


    public UserInfo(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getNikname() {
        return nikname;
    }

    public void setNikname(String nikname) {
        this.nikname = nikname;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static UserInfo getInstance(){
        return instance;
    }
    public UserInfo(String id, String pw, String nikname, String birth, String email, String address) {
        this.id = id;
        this.pw = pw;
        this.nikname = nikname;
        this.birth = birth;
        this.email = email;
        this.address = address;
    }


    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("pw", pw);
        result.put("nickname", nikname);
        result.put("birth", birth);
        result.put("email", email);
        result.put("address", address);

        return result;
    }


}
