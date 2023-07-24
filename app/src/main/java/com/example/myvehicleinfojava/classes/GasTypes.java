package com.example.myvehicleinfojava.classes;

import java.util.ArrayList;
import java.util.List;

public class GasTypes {
    public Integer ID;
    public String ID_str;
    public String Name;

    public static List<String> convertListToNameList(List<GasTypes> list){
        List<String> newList = new ArrayList();
        for (GasTypes x : list)
            newList.add(x.Name);
        return newList;
    }
}
