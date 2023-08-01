package com.example.myvehicleinfojava.classes;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Gas {




    public String uid;
    public double money, litres;
    @ServerTimestamp
    public Timestamp date;


    public Gas(){

    }


    public String getDate() {
      //  Date d = new Date(date.getSeconds());
        Date d = (date.toDate());
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("el", "GR") );
        return dateFormat.format(d);

    }


    public static class colNames{
        public static final String LITRES = "litres";
        public static final String MONEY = "money";
        public static final String DATE = "date";
        public static final String UID = "uid";
        public static final String VEHICLE_ID = "vehicleID";
        public static final String CATEGORY_ID = "categoryID";

    }


}
