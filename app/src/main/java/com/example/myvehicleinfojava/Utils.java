package com.example.myvehicleinfojava;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;


import com.google.firebase.Timestamp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Utils {

   public static void setDatePicker(Context ctx, TextView tv){
       tv.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               // on below line we are getting
               // the instance of our calendar.
               final Calendar c = Calendar.getInstance();

               // on below line we are getting
               // our day, month and year.
               int year = c.get(Calendar.YEAR);
               int month = c.get(Calendar.MONTH);
               int day = c.get(Calendar.DAY_OF_MONTH);

               // on below line we are creating a variable for date picker dialog.
               DatePickerDialog datePickerDialog = new DatePickerDialog(
                       // on below line we are passing context.
                       ctx,
                       new DatePickerDialog.OnDateSetListener() {
                           @SuppressLint("SetTextI18n")
                           @Override
                           public void onDateSet(DatePicker view, int year,
                                                 int monthOfYear, int dayOfMonth) {
                               // on below line we are setting date to our text view.
                               tv.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                           }
                       },
                       // on below line we are passing year,
                       // month and day for selected date in our date picker.
                       year, month, day);
               // at last we are calling show to
               // display our date picker dialog.
               datePickerDialog.show();
           }
       });
   }

   public static Timestamp convertToTimestamp(String dateStr){

       //Date string with offset information
       //String dateString = "03-08-2019";
       DateFormat dateFormat = new SimpleDateFormat("dd-M-yyyy");
       try {

           Date date = dateFormat.parse(dateStr);
           Timestamp ts=new Timestamp(date);
           return ts;

       } catch (Exception e) {
           e.printStackTrace();
       }
       return null;
   }


   public static String getCurrentDate(){
       Date c = Calendar.getInstance().getTime();
       SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
       return df.format(c);
   }


    public static Double checkAndParseDouble(String txt){
       if (txt == null || txt.isEmpty())
           return 0.0;
       return Double.parseDouble(txt);

    }

    public static Integer checkAndParseInteger(String txt){
        if (txt == null || txt.isEmpty())
            return 0;
        return Integer.parseInt(txt);

    }

}
