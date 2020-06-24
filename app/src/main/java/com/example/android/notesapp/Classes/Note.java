package com.example.android.notesapp.Classes;

public class Note {
    String body;
    Long ID;

    public Note(String body, Long ID) {
        this.body = body;
        this.ID = ID;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }
}
