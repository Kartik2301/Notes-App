package com.example.android.notesapp;

public class data_item {
    String string;
    String imageId;

    data_item(String string, String imageId){
        this.string = string;
        this.imageId = imageId;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }
}
