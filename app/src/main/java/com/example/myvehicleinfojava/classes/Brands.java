package com.example.myvehicleinfojava.classes;

import java.util.ArrayList;
import java.util.List;

public class Brands {
    public String name;
    public String logoPathStr;
    public int id;
    public String idStr;

    public static List<String> convertListToNameList(List<Brands> list){
        List<String> newList = new ArrayList();
        for (Brands x : list)
            newList.add(x.name);
        return newList;
    }

    @Override
    public String toString() {
        return name;
    }
}
