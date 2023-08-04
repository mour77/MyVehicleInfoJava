package com.example.myvehicleinfojava.classes;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.ServerTimestamp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Notification {

    public String id;
    public String title;
    public String description;
    public String uid;
    @DocumentId
    public String documentID;
    @ServerTimestamp
    public Timestamp date;
    public boolean isSent;




    public String getDate() {
        if (date == null)
            return "";
        Date d = (date.toDate());
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("el", "GR") );
        return dateFormat.format(d);

    }


    public static class colNames{
        public static final String DESCRIPTION = "description";
        public static final String TITLE = "title";
        public static final String IS_SENT = "isSent";
        public static final String DATE = "date";
        public static final String UID = "uid";

    }
}
