package com.example.android.notesapp.Classes;

public class saved_items {
    String location;
    String topic;

    public saved_items(String location, String topic) {
        this.location = location;
        this.topic = topic;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
