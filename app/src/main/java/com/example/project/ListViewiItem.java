package com.example.project;

import android.graphics.drawable.Drawable;

public class ListViewiItem {
    private String title;
    private Drawable icon;
    private String desc;

    public ListViewiItem(String title, Drawable icon, String desc) {
        this.title = title;
        this.icon = icon;
        this.desc = desc;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
