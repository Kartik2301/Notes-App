package com.example.android.notesapp.Classes;

import java.io.Serializable;

public class upload implements Serializable {
    public String id;
    public String title;
    public String description;
    public String url;
    public int type;
    public int likes;
    public String date;

    public upload() {
    }

    public upload(String id, String title, String description, String url, int type, int likes, String date){
        this.id = id;
        this.title = title;
        this.description = description;
        this.url = url;
        this.type = type;
        this.likes = likes;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
