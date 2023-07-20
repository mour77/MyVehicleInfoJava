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
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", new Locale("el", "GR") );
        return dateFormat.format(d);

    }



}
