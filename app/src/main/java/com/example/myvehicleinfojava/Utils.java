package com.example.myvehicleinfojava;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;


import androidx.fragment.app.FragmentManager;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.Timestamp;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import androidx.core.util.Pair;

import org.json.JSONArray;
import org.json.JSONException;

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
                               tv.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

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


   public static void setPeriodPicker(FragmentManager frg , TextView tv){
       MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
       builder.setTitleText("Select a date range");

       // Building the date picker dialog
       MaterialDatePicker<Pair<Long, Long>> datePicker = builder.build();
       datePicker.addOnPositiveButtonClickListener(selection -> {

           // Retrieving the selected start and end dates
           Long startDate = selection.first;
           Long endDate = selection.second;

           // Formating the selected dates as strings
           SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
           String startDateString = sdf.format(new Date(startDate));
           String endDateString = sdf.format(new Date(endDate));

           // Creating the date range string
           String selectedDateRange = startDateString + " - " + endDateString;

           // Displaying the selected date range in the TextView
           tv.setText(selectedDateRange);
       });

       // Showing the date picker dialog
       datePicker.show(frg, "DATE_PICKER");
   }

   public static Timestamp convertToTimestamp(String dateStr){

       //Date string with offset information
       //String dateString = "03-08-2019";
       DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
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
       SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
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




    public static String loadJSONFromAsset(Activity act, String pathFile) {
        String json = null;
        try {
            InputStream is = act.getAssets().open(pathFile);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }




    public static Drawable loadImageFromAssets(Context ctx, String path) {
        try {
            InputStream ims = null;
            ims = ctx.getAssets().open(path);
            return Drawable.createFromStream(ims, null);

        } catch (IOException e) {
            return null;
        }
    }


    public static String getLogoPathStrByBrandName(Activity act, String brandName) {
        try {
            String jsonStr = Utils.loadJSONFromAsset(act, "dataManufacturesLogos.json");
            JSONArray info = null;
            info = new JSONArray(jsonStr);

            for (int i=0; i<info.length(); i++){
                if (brandName.equals(info.getJSONObject(i).getString("name"))){
                    return info.getJSONObject(i).getJSONObject("image").getString("localThumb");
                }
            }


        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return "";
    }

}
