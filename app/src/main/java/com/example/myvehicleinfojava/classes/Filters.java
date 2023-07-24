package com.example.myvehicleinfojava.classes;

import android.widget.ListView;

import java.util.ArrayList;

public class Filters {

    private String name;
    private boolean isChecked ;

    public Filters(String name, boolean isChecked) {
        this.name = name;
        this.isChecked = isChecked;
    }

    public static ArrayList<Filters> getHistoryFilterList(){
        ArrayList<Filters> list = new ArrayList<>();
        list.add(new Filters("Καύσιμο",true));
        list.add(new Filters("Επιδιόρθωση",true));

        return list;
    }
}
