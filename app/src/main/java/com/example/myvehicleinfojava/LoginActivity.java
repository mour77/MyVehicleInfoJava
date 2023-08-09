package com.example.myvehicleinfojava;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.myvehicleinfojava.firebaseClasses.FireBaseMethods;
import com.example.myvehicleinfojava.databinding.ActivityLoginBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    GoogleSignInClient googleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mAuth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();
        FireBaseMethods.setSettings(db);


        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("861107820797-9d0jm07mitg68jns6p1rqvv93777jf5v.apps.googleusercontent.com")
                .requestEmail()
                .build();

        // Initialize sign in client
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);



        binding.gmailLoginBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(LoginActivity.this, "eleos", Toast.LENGTH_SHORT).show();
                Intent intent = googleSignInClient.getSignInIntent();
                resultLauncher.launch(intent);


                }
        });
    }


    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                   // Toast.makeText(LoginActivity.this, String.valueOf(result.getResultCode()), Toast.LENGTH_SHORT).show();

                    if (result.getResultCode() == Activity.RESULT_OK) {


                        Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                        // check condition
                        if (signInAccountTask.isSuccessful()) {
                            // When google sign in successful initialize string
                            String s = "Google sign in successful";
                            // Display Toast
                            // Initialize sign in account
                            try {
                                // Initialize sign in account
                                GoogleSignInAccount googleSignInAccount = signInAccountTask.getResult(ApiException.class);
                                // Check condition
                                if (googleSignInAccount != null) {
                                    // When sign in account is not equal to null initialize auth credential
                                    AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
                                    // Check credential
                                    mAuth.signInWithCredential(authCredential).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            // Check condition
                                            if (task.isSuccessful()) {

                                                FirebaseAuth mAuth = FirebaseAuth.getInstance();

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

                                                FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<String> task) {
                                                        if (!task.isSuccessful()) {
                                                            Log.w("eleos", "Fetching FCM registration token failed", task.getException());
                                                            return;
                                                        }
                                                        String token = task.getResult();
                                                        Map<String, Object> map = new HashMap<>();
                                                        map.put("token", token);
                                                        db.collection("Users").document(uid)
                                                                .update(map);
                                                    }
                                                });

                                                db.collection("Users").document(uid)
                                                        .update(userMap)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                //Toast.makeText(LoginActivity.this, "Δεν έγινε η καταγραφή στοιχείων", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            } else {
                                                Toast.makeText(LoginActivity.this, "Δεν συνδεθήκατε μέσω gmail", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            } catch (ApiException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

    }

}