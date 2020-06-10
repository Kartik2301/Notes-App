package com.example.android.notesapp;

import java.io.Serializable;

public class user implements Serializable {
    String username;
    int points;
    int rank;
    String imageUrl;

    public user(){

    }
    public user(String username, int points, int rank, String imageUrl) {
        this.username = username;
        this.points = points;
        this.rank = rank;
        this.imageUrl = imageUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
