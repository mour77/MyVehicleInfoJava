package com.example.myvehicleinfojava;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myvehicleinfojava.classes.Gas;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class TestActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HistoryRVAdapter itemAdapter;
    private FirebaseFirestore db;

    private Spinner spinnerFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
     //   spinnerFilter = findViewById(R.id.spinnerFilter);
        recyclerView = findViewById(R.id.recycler_view);
        db = FirebaseFirestore.getInstance();



    }

    @Override
    protected void onStart() {
        super.onStart();
        itemAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        itemAdapter.stopListening();
    }




    private Query getQueryBasedOnFilter(String selectedFilter) {
        // Implement your logic here to construct the query based on the selectedFilter
        if (selectedFilter.equals("All Items")) {
            return db.collection("Gas").orderBy("date");
        } else if (selectedFilter.equals("5pJpIs3010eHnw4ZVfDM")) {
            return db.collection("Gas").whereEqualTo("vehicleID", "5pJpIs3010eHnw4ZVfDM").orderBy("date");
        } else if (selectedFilter.equals("VL0VuEApbxZ092kuoGps")) {
            return db.collection("Gas").whereEqualTo("vehicleID", "VL0VuEApbxZ092kuoGps").orderBy("date");
        }  else {
            // Default query, in case the selectedFilter doesn't match any options
            return db.collection("items");
        }
    }

}