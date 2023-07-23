package com.example.myvehicleinfojava.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.myvehicleinfojava.MainActivity;
import com.example.myvehicleinfojava.R;
import com.example.myvehicleinfojava.Utils;
import com.example.myvehicleinfojava.classes.Gas;
import com.example.myvehicleinfojava.classes.Vehicle;
import com.example.myvehicleinfojava.firebaseClasses.Categories;
import com.example.myvehicleinfojava.listeners.GeneralListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddVehicleDialog {


    public static void show(Activity act , GeneralListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(act);
        builder.setTitle("Προσθήκη οχήματος");

        // set the custom layout
        final View customLayout = act.getLayoutInflater().inflate(R.layout.add_gas_dialog, null);
        builder.setView(customLayout);
        EditText litresET = customLayout.findViewById(R.id.litresET);
        EditText moneyET = customLayout.findViewById(R.id.moneyET);
        TextView dateTV = customLayout.findViewById(R.id.dateTV);
        dateTV.setText(Utils.getCurrentDate());
        Utils.setDatePicker(act,dateTV);

        // add a button
        builder.setPositiveButton("OK", (dialog, which) -> {

            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            FirebaseUser user = mAuth.getCurrentUser();

            Map<String, Object> vehicleMap = new HashMap<>();
            vehicleMap.put(Vehicle.colNames.USER_ID, user.getUid());
            vehicleMap.put(Vehicle.colNames.BRAND, "testBrand");
            vehicleMap.put(Vehicle.colNames.MODEL, "testModel");
            vehicleMap.put(Vehicle.colNames.KIVIKA, 2000);
            vehicleMap.put(Vehicle.colNames.HP, 120);
            vehicleMap.put(Vehicle.colNames.YEAR, 2012);


            db.collection("Vehicles").document()
                    .set(vehicleMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(act, R.string.successful_save, Toast.LENGTH_SHORT).show();
                            if (act instanceof MainActivity)
                                ((MainActivity) act).getBrands();

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
