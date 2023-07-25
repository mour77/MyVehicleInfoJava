package com.example.myvehicleinfojava.classes;

import com.example.myvehicleinfojava.firebaseClasses.Categories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Filters {

    public String name;
    public boolean isChecked ;
    public int categoryID;

    public Filters(String name, boolean isChecked, int categoryID) {
        this.name = name;
        this.isChecked = isChecked;
        this.categoryID = categoryID;
    }

    public static ArrayList<Filters> getHistoryFilterList(){
        ArrayList<Filters> list = new ArrayList<>();
        list.add(new Filters("Καύσιμο",true , Categories.GAS));
        list.add(new Filters("Επιδιόρθωση",true, Categories.REPAIR));

        return list;
    }


    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public static Integer[] getFilteredCategories(ArrayList<Filters> list){

        List<Integer> filteredList= list.stream()
                .filter(s -> s.isChecked)
                .map(Filters::getCategoryID).collect(Collectors.toList());

        Integer []  categoryIDs = filteredList.toArray(new Integer[0]);
        return categoryIDs;
//        return list.stream()
//                .filter(s -> s.isChecked)
//                .map(Filters::getCategoryID).toArray();
    }



}
