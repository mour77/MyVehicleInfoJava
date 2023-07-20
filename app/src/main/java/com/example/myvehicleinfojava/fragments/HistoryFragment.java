package com.example.myvehicleinfojava.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.myvehicleinfojava.HistoryRVAdapter;
import com.example.myvehicleinfojava.MainActivity;
import com.example.myvehicleinfojava.R;
import com.example.myvehicleinfojava.Utils;
import com.example.myvehicleinfojava.classes.Gas;
import com.example.myvehicleinfojava.classes.Vehicle;
import com.example.myvehicleinfojava.databinding.FragmentHistoryBinding;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class HistoryFragment extends Fragment {

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
        if (main != null)
            db = main.db;






    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        bd = FragmentHistoryBinding.inflate(inflater, container, false);
        View view = bd.getRoot();

        bd.rv.setLayoutManager(new LinearLayoutManager(main));

        getVehicleInfoRV();
      //  bd.autoCompleteTextView.setAdapter();
        return view;
    }




    public void getVehicleInfoRV() {

        Query query = FirebaseFirestore.getInstance()
                .collection("Gas")
                //.whereEqualTo("vehicleID", main.vehicleID)
                .whereEqualTo("vehicleID", main.vehicleID)
                //VL0VuEApbxZ092kuoGps
                //
                .orderBy("date")
                //.limit(50)
                ;

        FirestoreRecyclerOptions<Gas> options = new
                FirestoreRecyclerOptions.Builder<Gas>()
                .setQuery(query, Gas.class)
                .build();

      //  adapter.cl


        if (adapter == null) {
            adapter = new HistoryRVAdapter(options);
            bd.rv.setAdapter(adapter);
        }
        else {
            adapter.updateOptions(options);
            adapter.notifyDataSetChanged();
        }


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