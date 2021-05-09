package com.example.project;

import android.media.Image;

import java.util.HashMap;
import java.util.Map;

public class SaveCalDTO { //캘린더에서 쓰는 DTO
    String eat;
    String exercise;
    Image todaypic;
    String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private static SaveCalDTO instance = new SaveCalDTO(); //전역적으로 객체를 쓰기 위한 야매방법 추후 세션을 이용한 방법으로 보강 필요

    public static SaveCalDTO getInstance(){
        return instance;
    }

    public SaveCalDTO(){

    }

    public String getEat() {
        return eat;
    }

    public void setEat(String eat) {
        this.eat = eat;
    }

    public SaveCalDTO(Image todaypic) {
        this.todaypic = todaypic;
    }

    public String getExercise() {
        return exercise;
    }

    public void setExercise(String exercise) {
        this.exercise = exercise;
    }

    public Image getTodaypic() {
        return todaypic;
    }

    public void setTodaypic(Image todaypic) {
        this.todaypic = todaypic;
    }

    public SaveCalDTO(String eat, String exercise, Image todaypic) {
        this.eat = eat;
        this.exercise = exercise;
        this.todaypic = todaypic;
    }

    public Map<String, String> toMap() {
        HashMap<String, String> result = new HashMap<>();


        return result;
    }
}
