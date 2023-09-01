package com.example.myvehicleinfojava.fragments;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.myvehicleinfojava.CustomVehiclesViewAdapter;
import com.example.myvehicleinfojava.MainActivity;
import com.example.myvehicleinfojava.MovementsRVAdapter;
import com.example.myvehicleinfojava.NotificationsRVAdapter;
import com.example.myvehicleinfojava.R;
import com.example.myvehicleinfojava.Utils;
import com.example.myvehicleinfojava.classes.Brands;
import com.example.myvehicleinfojava.classes.Gas;
import com.example.myvehicleinfojava.classes.Notification;
import com.example.myvehicleinfojava.classes.TargetAndMovements;
import com.example.myvehicleinfojava.classes.Vehicle;
import com.example.myvehicleinfojava.databinding.FragmentHistoryBinding;
import com.example.myvehicleinfojava.databinding.FragmentMyTargetsBinding;
import com.example.myvehicleinfojava.databinding.FragmentNotificationBinding;
import com.example.myvehicleinfojava.dialogs.AddTargetDialog;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MyTargetsFragment extends Fragment {


    MainActivity main;
    FragmentMyTargetsBinding bd;
    FirebaseFirestore db;

    MovementsRVAdapter adapter;
    String targetID = "";
    String resultFromDialog;

    Query queryTargets;
    Query queryMovements;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main = (MainActivity) getActivity();


    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        bd = FragmentMyTargetsBinding.inflate(inflater, container, false);
        View view = bd.getRoot();

        db = FirebaseFirestore.getInstance();
        bd.targetRV.setLayoutManager(new LinearLayoutManager(main));

        getTargets();



        bd.addMovementBT.setOnClickListener(v -> AddTargetDialog.show(main, result -> {resultFromDialog = result;} , targetID));

        return view;
    }



    private void getTargets (){
        db.collection("Targets")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<TargetAndMovements> targets = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                TargetAndMovements v = document.toObject(TargetAndMovements.class);
                                v.id = document.getId();
                                targets.add(v);



                            }
                            addListToDropDown(targets );
                        } else {
                        }
                    }
                });
    }


    @SuppressLint("SetTextI18n")
    private void addListToDropDown(List<TargetAndMovements> targets ){

        String [] targetsArray = new String[targets.size()];
        int pos = 0;
        for (TargetAndMovements x: targets) {
            targetsArray[pos] = x.title;
            pos++;
        }
        bd.autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {


                for (TargetAndMovements x: targets){
                    String target = x.title;
                    if (target.equals(s.toString())){

                        targetID = x.documentID;

                        ;
                        //updateAdapter(getQueryMovements());
                        queryMovements = getQueryMovements();
                        setQueryCompleteLsitenerAndUpdate();

                        bd.totalCostTV.setText(String.valueOf(x.total_cost));
                        bd.remainingCostTV.setText(String.valueOf(x.remaining_cost));

                        break;
                    }
                }

            }
        });


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(main, R.layout.dropdown_item, targetsArray);
        bd.autoCompleteTextView.setAdapter(arrayAdapter);
        if (targets.size() > 0) {
            bd.autoCompleteTextView.setText(targets.get(0).title , false);
            targetID = targets.get(0).documentID;

            queryMovements = getQueryMovements();
            setQueryCompleteLsitenerAndUpdate();

            updateAdapter(queryMovements);
            adapter.startListening();

        }
    }





    private Query getQueryMovements(){

        return  db.collection("Targets").document(targetID).collection("movements");

    }








    private void updateAdapter(Query query) {
        FirestoreRecyclerOptions<TargetAndMovements> newOptions = new FirestoreRecyclerOptions.Builder<TargetAndMovements>()
                .setQuery(query, TargetAndMovements.class)
                .build();

        if (adapter == null) {
            adapter = new MovementsRVAdapter(newOptions);
            bd.targetRV.setAdapter(adapter);
        }

        else
            adapter.updateOptions(newOptions);

    }




    public void setQueryCompleteLsitenerAndUpdate(){


        queryMovements.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    int currentSize = adapter.getItemCount();
                    //tell the recycler view that all the old items are gone
                    adapter.notifyItemRangeRemoved(0, currentSize);
                    //tell the recycler view how many new items we added
                    adapter.notifyItemRangeInserted(0, task.getResult().size());
                    updateAdapter(queryMovements);

                } else {
                    Toast.makeText(main, "Ανεπιτυχής διαδικασία δεδομένων", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }






    @Override
    public void onStart() {
        super.onStart();
//        adapter.startListening();
    }


    @Override
    public void onStop() {
        super.onStop();
        if (adapter !=null)
            adapter.stopListening();
    }
}