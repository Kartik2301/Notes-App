package com.example.android.notesapp.Classes;

import java.util.ArrayList;

public class comment_model {
    String id;
    ArrayList<comments> list;

    public comment_model(String id, ArrayList<comments> list) {
        this.id = id;
        this.list = list;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<comments> getList() {
        return list;
    }

    public void setList(ArrayList<comments> list) {
        this.list = list;
    }
}
