package com.example.myvehicleinfojava.classes;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.ServerTimestamp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TargetAndMovements {

    public String id;
    public String title;
    public double total_cost;
    public double remaining_cost;
    public double cost;
    public double money;
    public String uid;
    @DocumentId
    public String documentID;
    @ServerTimestamp
    public Timestamp date;




    public String getDate() {
        if (date == null)
            return "";
        Date d = (date.toDate());
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("el", "GR") );
        return dateFormat.format(d);

    }


    public static class colNames{
        public static final String TITLE = "title";
        public static final String TOTAL_COST = "total_cost";
        public static final String REMAINING_COST = "remaining_cost";
        public static final String COST = "cost";
        public static final String MONEY = "money";
        public static final String DATE = "date";
        public static final String UID = "uid";

    }
}
