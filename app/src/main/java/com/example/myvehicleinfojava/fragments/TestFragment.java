package com.example.myvehicleinfojava.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myvehicleinfojava.HistoryRVAdapter;
import com.example.myvehicleinfojava.R;
import com.example.myvehicleinfojava.WrapContentLinearLayoutManager;
import com.example.myvehicleinfojava.classes.Gas;
import com.example.myvehicleinfojava.listeners.GeneralListener;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;


public class TestFragment extends Fragment implements GeneralListener {

    private RecyclerView recyclerView;
    private HistoryRVAdapter itemAdapter;
    private FirebaseFirestore db;

    private Spinner spinnerFilter;
    public TestFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_test, container, false);
        // Inflate the layout for this fragment
        spinnerFilter = rootView.findViewById(R.id.spinnerFilter);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        db = FirebaseFirestore.getInstance();


//        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
//                getContext(), new Array[]{}, android.R.layout.simple_spinner_item
//        );
//        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerFilter.setAdapter(spinnerAdapter);


        String selectedFilter = spinnerFilter.getSelectedItem().toString();
        // Query to get the data from Firestore

        Query query = getQueryBasedOnFilter(selectedFilter);
        updateAdapter(query);


        // Configure the adapter with FirestoreRecyclerOptions


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getContext()));
        recyclerView.setAdapter(itemAdapter);



        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedFilter = parent.getItemAtPosition(position).toString();
                Query query = getQueryBasedOnFilter(selectedFilter);
                updateAdapter(query);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();
        itemAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        itemAdapter.stopListening();
    }




    private Query getQueryBasedOnFilter(String selectedFilter) {
        // Implement your logic here to construct the query based on the selectedFilter
        if (selectedFilter.equals("All Items")) {
            return db.collection("Gas").orderBy("date");
        }
        else {
         return db.collection("Gas").whereEqualTo("vehicleID", selectedFilter).orderBy("date");

        }

    }


    private void updateAdapter(Query query){
//        FirestoreRecyclerOptions<Gas> newOptions = new FirestoreRecyclerOptions.Builder<Gas>()
//                .setQuery(query, Gas.class)
//                .build();
//
//        if (itemAdapter == null)
//            itemAdapter = new HistoryRVAdapter(newOptions);
//        else
//            itemAdapter.updateOptions(newOptions);

    }

    @Override
    public void sendResult(String result) {
        Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
        itemAdapter.startListening();
        Query query = getQueryBasedOnFilter(result);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    int currentSize = itemAdapter.getItemCount();

                    //tell the recycler view that all the old items are gone
                    itemAdapter.notifyItemRangeRemoved(0, currentSize);
                    //tell the recycler view how many new items we added
                    itemAdapter.notifyItemRangeInserted(0, task.getResult().size());
                    updateAdapter(query);

                    Log.d("eleos", "xaxaxaxa");
                } else {
                    Log.d("eleos", "Error getting documents: ", task.getException());
                }
            }
        });


       // updateAdapter(query);



    }
}