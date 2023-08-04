package com.example.myvehicleinfojava.classes;

import android.app.Activity;

import com.example.myvehicleinfojava.Utils;

import org.json.JSONArray;
import org.json.JSONException;

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


    public String getLogoPathStr() {
        return logoPathStr;
    }

    public void setLogoPathStr(Activity act) {

        try {
            String jsonStr = Utils.loadJSONFromAsset(act, "dataManufacturesLogos.json");
            JSONArray info = null;
            info = new JSONArray(jsonStr);

            for (int i=0; i<info.length(); i++){
                String brandName = info.getJSONObject(i).getString("name");
                if (brandName.equals(name)){
                    logoPathStr = info.getJSONObject(i).getJSONObject("image").getString("localThumb");
                    break;
                }
            }


        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }



}
