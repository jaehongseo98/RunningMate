package com.example.project;

import android.media.Image;

public class SaveCalDTO {
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

    private static SaveCalDTO instance = new SaveCalDTO();

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
}
