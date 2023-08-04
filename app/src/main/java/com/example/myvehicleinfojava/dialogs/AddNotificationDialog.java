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
import com.example.myvehicleinfojava.classes.Notification;
import com.example.myvehicleinfojava.listeners.GeneralListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddNotificationDialog {

    public static void show(Activity act , GeneralListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(act);
        builder.setTitle("Προσθήκη ειδοποίησης");

        // set the custom layout
        final View customLayout = act.getLayoutInflater().inflate(R.layout.add_notification_dialog, null);
        builder.setView(customLayout);
        EditText titleET = customLayout.findViewById(R.id.titleET);
        EditText descriptionET = customLayout.findViewById(R.id.descriptionET);
        TextView dateTV = customLayout.findViewById(R.id.dateTV);
        dateTV.setText(Utils.getCurrentDate());
        Utils.setDatePicker(act,dateTV);

        // add a button
        builder.setPositiveButton("OK", (dialog, which) -> {

            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            FirebaseUser user = mAuth.getCurrentUser();

            Map<String, Object> notificationMap = new HashMap<>();
            notificationMap.put(Notification.colNames.UID, user.getUid());
            notificationMap.put(Notification.colNames.TITLE, titleET.getText().toString().trim());
            notificationMap.put(Notification.colNames.DESCRIPTION, descriptionET.getText().toString().trim());
            notificationMap.put(Notification.colNames.DATE, Utils.convertToTimestamp(dateTV.getText().toString()));
            notificationMap.put(Notification.colNames.IS_SENT, false);



            db.collection("Notifications").document()
                    .set(notificationMap)
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
