package com.example.android.notesapp.Classes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SortingClass {
    ArrayList<upload> list;

    public SortingClass(ArrayList<upload> list) {
        this.list = list;
    }

    public ArrayList<upload> sortByLikes() {
        Collections.sort(list, new SortByLikes());
        return list;
    }

    public ArrayList<upload> sortByDate() {
        Collections.sort(list, new SortByDate());
        return list;
    }

}
class SortByLikes implements Comparator<upload> {
    public int compare(upload a, upload b) {
        return b.getLikes() - a.getLikes();
    }
}

class SortByDate implements  Comparator<upload> {
    public int compare(upload a, upload b) {
        return a.getDate().compareTo(b.getDate());
    }
}