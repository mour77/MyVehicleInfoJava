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
import com.example.myvehicleinfojava.classes.Gas;
import com.example.myvehicleinfojava.classes.History;
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

public class AddGasDialog  {

    public static void show(Activity act , String vehicleID ) {
        showDialog(act,vehicleID, null);
    }

    public static void show(Activity act , String vehicleID , History history ) {
        showDialog(act,vehicleID, history);
    }


    private static void showDialog(Activity act , String vehicleID , History gas ){
        AlertDialog.Builder builder = new AlertDialog.Builder(act);
        builder.setTitle("Καύσιμο");

        // set the custom layout
        final View customLayout = act.getLayoutInflater().inflate(R.layout.add_gas_dialog, null);
        builder.setView(customLayout);
        EditText litresET = customLayout.findViewById(R.id.litresET);
        EditText moneyET = customLayout.findViewById(R.id.moneyET);
        TextView dateTV = customLayout.findViewById(R.id.dateTV);
        dateTV.setText(Utils.getCurrentDate());
        Utils.setDatePicker(act,dateTV);

        if (gas != null){
            litresET.setText(String.valueOf(gas.litres));
            moneyET.setText(String.valueOf(gas.money));
            dateTV.setText(gas.getDate());
        }

        // add a button
        builder.setPositiveButton("OK", (dialog, which) -> {

            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            FirebaseUser user = mAuth.getCurrentUser();

            Map<String, Object> gasMap = new HashMap<>();
            gasMap.put("uid", user.getUid());
            gasMap.put("vehicleID", vehicleID);
            gasMap.put("litres", Utils.checkAndParseDouble(litresET.getText().toString()));
            gasMap.put("money", Utils.checkAndParseDouble(moneyET.getText().toString()));
            gasMap.put("date",Utils.convertToTimestamp(dateTV.getText().toString()));
            gasMap.put(Gas.colNames.CATEGORY_ID, Categories.GAS);


            db.collection("History").document()
                    .set(gasMap)
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
