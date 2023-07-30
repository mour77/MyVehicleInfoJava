package com.example.myvehicleinfojava.classes;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class History {

    public String description ,remarks;
    public String vehicleID;
    public int odometer;
    public double money;

    public String uid;
    public double  litres;
    @ServerTimestamp
    public Timestamp date;

    public MapInfo mapInfo;

    public int categoryID;
    public History(){

    }
    public String getDate() {
        //  Date d = new Date(date.getSeconds());
        if (date == null)
            return "";
        Date d = (date.toDate());
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("el", "GR") );
        return dateFormat.format(d);

    }

    public static class MapInfo{
        public String latitude , longitude;
        public String mechanicName , remarks;
    }

}
