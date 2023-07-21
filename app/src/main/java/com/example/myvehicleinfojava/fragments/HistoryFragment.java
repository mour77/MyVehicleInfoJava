package com.example.myvehicleinfojava.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myvehicleinfojava.classes.History;
import com.example.myvehicleinfojava.classes.Repair;
import com.example.myvehicleinfojava.firebaseClasses.Collections;
import com.example.myvehicleinfojava.HistoryRVAdapter;
import com.example.myvehicleinfojava.MainActivity;
import com.example.myvehicleinfojava.classes.Gas;
import com.example.myvehicleinfojava.databinding.FragmentHistoryBinding;
import com.example.myvehicleinfojava.listeners.GeneralListener;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;


public class HistoryFragment extends Fragment implements GeneralListener {

    MainActivity main;
    FragmentHistoryBinding bd ;
    FirebaseFirestore db;

    HistoryRVAdapter adapter;


    public HistoryFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        main = (MainActivity) getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        bd = FragmentHistoryBinding.inflate(inflater, container, false);
        View view = bd.getRoot();

        db = FirebaseFirestore.getInstance();
        bd.rv.setLayoutManager(new LinearLayoutManager(main));

        Query query = getQueryBasedOnFilter(main.vehicleID);
        updateAdapter(query);

        bd.rv.setAdapter(adapter);

        return view;
    }






    private Query getQueryBasedOnFilter(String selectedVehicleID) {

//        Task<QuerySnapshot> firstTask = db.collection("History").whereEqualTo(Gas.colNames.VEHICLE_ID, selectedVehicleID).orderBy(Gas.colNames.DATE).get();
//        Task<QuerySnapshot> secondTask = db.collection("History").whereEqualTo(Repair.colNames.VEHICLE_ID, selectedVehicleID).orderBy(Gas.colNames.DATE).get();
//
//        Task<List<Object>> combinedTask = Tasks.whenAllSuccess(firstTask, secondTask).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
//            @Override
//            public void onSuccess(List<Object> list) {
//                //Do what you need to do with your list
//                Toast.makeText(main, String.valueOf(list.size()), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//

        return db.collection("History").whereEqualTo(Gas.colNames.VEHICLE_ID, selectedVehicleID).orderBy(Gas.colNames.DATE);
    }

    private void updateAdapter(Query query){
        FirestoreRecyclerOptions<History> newOptions = new FirestoreRecyclerOptions.Builder<History>()
                .setQuery(query, History.class)
                .build();

        if (adapter == null)
            adapter = new HistoryRVAdapter(newOptions);
        else
            adapter.updateOptions(newOptions);

    }





    @Override
    public void sendResult(String result) {

        Query query = getQueryBasedOnFilter(result);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    int currentSize = adapter.getItemCount();
                    //tell the recycler view that all the old items are gone
                    adapter.notifyItemRangeRemoved(0, currentSize);
                    //tell the recycler view how many new items we added
                    adapter.notifyItemRangeInserted(0, task.getResult().size());
                    updateAdapter(query);

                } else {
                    Toast.makeText(main, "Ανεπιτυχής διαδικασία δεδομένων", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();

    }


}