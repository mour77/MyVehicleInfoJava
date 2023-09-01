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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class AddTargetDialog {

    public static void show(Activity act , GeneralListener listener ){
        show(act, listener , null);
    }

    public static void show(Activity act , GeneralListener listener , TargetAndMovements target){
        AlertDialog.Builder builder = new AlertDialog.Builder(act);
        builder.setTitle("Προσθήκη στόχου");

        boolean hasTargetID = target != null && target.documentID != null && !target.documentID.isEmpty();
        // set the custom layout
        final View customLayout = act.getLayoutInflater().inflate(R.layout.add_target_dialog, null);
        builder.setView(customLayout);
        EditText titleET = customLayout.findViewById(R.id.titleET);
        EditText totalCostET = customLayout.findViewById(R.id.totalCostET);
        EditText costET = customLayout.findViewById(R.id.costET);
        TextView dateTV = customLayout.findViewById(R.id.dateTV);
        dateTV.setText(Utils.getCurrentDate());
        Utils.setDatePicker(act,dateTV);


        if (hasTargetID)
            totalCostET.setVisibility(View.GONE);
        else
            costET.setVisibility(View.GONE);

            // add a button
        builder.setPositiveButton("OK", (dialog, which) -> {

            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            FirebaseUser user = mAuth.getCurrentUser();

            Map<String, Object> targetMap = new HashMap<>();
            targetMap.put(TargetAndMovements.colNames.UID, user.getUid());

            targetMap.put(TargetAndMovements.colNames.TITLE, titleET.getText().toString().trim());

            if (hasTargetID)
                targetMap.put(TargetAndMovements.colNames.COST, Utils.checkAndParseDouble(costET.getText().toString().trim()));
            else {
                targetMap.put(TargetAndMovements.colNames.TOTAL_COST, Utils.checkAndParseDouble(totalCostET.getText().toString().trim()));
                targetMap.put(TargetAndMovements.colNames.REMAINING_COST, Utils.checkAndParseDouble(totalCostET.getText().toString().trim()));
            }
            targetMap.put(TargetAndMovements.colNames.DATE, Utils.convertToTimestamp(dateTV.getText().toString()));


            DocumentReference ref;
            if (hasTargetID){
                ref = db.collection("Targets").document(target.documentID).collection("movements").document();
            }
            else {
                ref = db.collection("Targets").document();
            }


            ref.set(targetMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(act, R.string.successful_save, Toast.LENGTH_SHORT).show();
                    if (hasTargetID) {
                        updateRemainingCost(target, Utils.checkAndParseDouble(costET.getText().toString().trim()), listener );
                    }

                    dialog.dismiss();
                }


            }).addOnFailureListener(new OnFailureListener() {
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


    private static  void updateRemainingCost(TargetAndMovements target, double cost, GeneralListener listener) {
        double remainingCost = target.remaining_cost - cost;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Targets").document(target.documentID).update(TargetAndMovements.colNames.REMAINING_COST, remainingCost);
        listener.sendResult(String.valueOf(remainingCost));
    }
}
