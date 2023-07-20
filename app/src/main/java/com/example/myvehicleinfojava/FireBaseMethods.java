package com.example.myvehicleinfojava;

import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.MemoryCacheSettings;
import com.google.firebase.firestore.PersistentCacheSettings;

import java.util.HashMap;
import java.util.Map;

public class FireBaseMethods {

    public static void addUser(GoogleSignInAccount googleSignInAccount){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String uid = mAuth.getUid();
        FirebaseUser user = mAuth.getCurrentUser();

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", googleSignInAccount.getId());
        userMap.put("idToken", googleSignInAccount.getIdToken());
        userMap.put("account", googleSignInAccount.getAccount());
        userMap.put("displayName", googleSignInAccount.getDisplayName());
        userMap.put("email", googleSignInAccount.getEmail());
        userMap.put("givenName", googleSignInAccount.getGivenName());
        userMap.put("familyName", googleSignInAccount.getFamilyName());
        userMap.put("isExpired", googleSignInAccount.isExpired() );
        userMap.put("photoUrl", googleSignInAccount.getPhotoUrl());
        userMap.put("serverAuthCode", googleSignInAccount.getServerAuthCode());

        db.collection("Users").document(uid)
                .set(userMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Toast.makeText(LoginActivity.this, "Δεν έγινε η καταγραφή στοιχείων", Toast.LENGTH_SHORT).show();
                    }
                });

    }



    public static void setSettings(FirebaseFirestore db){
        //ΑΥΤΗ Η ΜΕΘΟΔΟΣ ΠΡΕΠΕΙ ΝΑ ΚΑΛΕΣΤΕΙ ΜΟΝΟ ΜΙΑ ΦΟΡΑ ΟΤΑΝ ΑΝΟΙΓΕΙ Η ΕΦΑΡΜΟΓΗ
        FirebaseFirestoreSettings settings =
                new FirebaseFirestoreSettings.Builder(db.getFirestoreSettings())
                        // Use memory-only cache
                        .setLocalCacheSettings(MemoryCacheSettings.newBuilder().build())
                        // Use persistent disk cache (default)
                        .setLocalCacheSettings(PersistentCacheSettings.newBuilder().build())
                        .build();

        db.setFirestoreSettings(settings);
    }
}
