package com.example.myvehicleinfojava.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.myvehicleinfojava.MainActivity;
import com.example.myvehicleinfojava.R;
import com.example.myvehicleinfojava.Utils;
import com.example.myvehicleinfojava.classes.Vehicle;
import com.example.myvehicleinfojava.listeners.GeneralListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FilterHistoryDialog {

    public static void show(Activity act , GeneralListener listener){

        AlertDialog.Builder builder = new AlertDialog.Builder(act);
        builder.setTitle("Φίλτρα");

        // set the custom layout
        final View customLayout = act.getLayoutInflater().inflate(R.layout.filter_history_dialog, null);
        builder.setView(customLayout);
        ListView filterList = customLayout.findViewById(R.id.filterListLV);



        // add a button
        builder.setPositiveButton("OK", (dialog, which) -> {





        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
