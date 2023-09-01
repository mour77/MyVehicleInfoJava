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
import com.example.myvehicleinfojava.classes.TargetAndMovements;
import com.example.myvehicleinfojava.listeners.GeneralListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddMovementDialog {

    public static void show(Activity act , GeneralListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(act);
        builder.setTitle("Προσθήκη στόχου");

        // set the custom layout
        final View customLayout = act.getLayoutInflater().inflate(R.layout.add_target_dialog, null);
        builder.setView(customLayout);
        EditText titleET = customLayout.findViewById(R.id.titleET);
        EditText moneyET = customLayout.findViewById(R.id.moneyET);
        TextView dateTV = customLayout.findViewById(R.id.dateTV);
        dateTV.setText(Utils.getCurrentDate());
        Utils.setDatePicker(act,dateTV);

        // add a button
        builder.setPositiveButton("OK", (dialog, which) -> {

            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            FirebaseUser user = mAuth.getCurrentUser();

            Map<String, Object> targetMap = new HashMap<>();
            targetMap.put(TargetAndMovements.colNames.UID, user.getUid());
            targetMap.put(TargetAndMovements.colNames.TITLE, titleET.getText().toString().trim());
            targetMap.put(TargetAndMovements.colNames.TOTAL_COST, Utils.checkAndParseDouble(moneyET.getText().toString().trim()));
            targetMap.put(TargetAndMovements.colNames.DATE, Utils.convertToTimestamp(dateTV.getText().toString()));



            db.collection("Targets").document()
                    .set(targetMap)
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
