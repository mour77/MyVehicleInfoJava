package com.example.myvehicleinfojava.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.myvehicleinfojava.R;
import com.example.myvehicleinfojava.Utils;
import com.example.myvehicleinfojava.classes.Repair;
import com.example.myvehicleinfojava.firebaseClasses.Categories;
import com.example.myvehicleinfojava.listeners.GeneralListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddRepairDialog {

    public static void show(Activity act , String vehicleID, GeneralListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(act);
       // builder.setTitle(Collections.REPAIRS.text);
        builder.setTitle("repair");

        // set the custom layout
        final View customLayout = act.getLayoutInflater().inflate(R.layout.add_repair_dialog, null);
        builder.setView(customLayout);
        EditText descriptionET = customLayout.findViewById(R.id.descriptionET);
        EditText moneyET = customLayout.findViewById(R.id.moneyET);
        EditText odometerET = customLayout.findViewById(R.id.odometerET);
        TextView dateTV = customLayout.findViewById(R.id.dateTV);
        EditText remarksET = customLayout.findViewById(R.id.remarksET);
        dateTV.setText(Utils.getCurrentDate());
        Utils.setDatePicker(act,dateTV);

        // add a button
        builder.setPositiveButton("OK", (dialog, which) -> {

            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            FirebaseUser user = mAuth.getCurrentUser();

            Map<String, Object> repairMap = new HashMap<>();
            repairMap.put(Repair.colNames.UID, user.getUid());
            repairMap.put(Repair.colNames.VEHICLE_ID, vehicleID);

            repairMap.put(Repair.colNames.DESCRIPTION, descriptionET.getText().toString());
            repairMap.put(Repair.colNames.MONEY, Utils.checkAndParseDouble(moneyET.getText().toString()));
            repairMap.put(Repair.colNames.ODOMETER, Utils.checkAndParseInteger(odometerET.getText().toString().trim()));
            repairMap.put(Repair.colNames.DATE,Utils.convertToTimestamp(dateTV.getText().toString()));
            repairMap.put(Repair.colNames.REMARKS, remarksET.getText().toString().trim());
            repairMap.put(Repair.colNames.CATEGORY_ID, Categories.REPAIR);


            db.collection("History").document()
                    .set(repairMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(act, R.string.successful_save, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(act, R.string.error_save, Toast.LENGTH_SHORT).show();
                        }
                    });

        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
