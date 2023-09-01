package com.example.myvehicleinfojava.fragments;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myvehicleinfojava.CustomVehiclesViewAdapter;
import com.example.myvehicleinfojava.R;
import com.example.myvehicleinfojava.Utils;
import com.example.myvehicleinfojava.WrapContentLinearLayoutManager;
import com.example.myvehicleinfojava.classes.Brands;
import com.example.myvehicleinfojava.classes.History;
import com.example.myvehicleinfojava.HistoryRVAdapter;
import com.example.myvehicleinfojava.MainActivity;
import com.example.myvehicleinfojava.classes.Gas;
import com.example.myvehicleinfojava.classes.Vehicle;
import com.example.myvehicleinfojava.databinding.FragmentHistoryBinding;
import com.example.myvehicleinfojava.dialogs.AddVehicleDialog;
import com.example.myvehicleinfojava.listeners.GeneralListener;
import com.example.myvehicleinfojava.listeners.HistoryFilterListener;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class HistoryFragment extends Fragment implements HistoryFilterListener, View.OnClickListener {

    MainActivity main;
    FragmentHistoryBinding bd ;
    FirebaseFirestore db;
    Query query;
    public String resultFromDialog;
    public String uid = "";

    HistoryRVAdapter adapter;
    Fragment.SavedState savedState;

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

        if (savedInstanceState != null) {
            // Restore the data from the bundle
            bd.periodTV.setText(savedInstanceState.getString("selectedPeriod"));
            bd.totalCostTV.setText(savedInstanceState.getString("totalCost"));
        }

        db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getUid();

        //updateAdapter(getQueryHistory(""));
        getQueryHistory(main.vehicleID);
        initializeRVAdapter();
        setQueryCompleteLsitenerAndUpdate();


        bd.rv.setLayoutManager(new WrapContentLinearLayoutManager(main));
       // bd.rv.setLayoutManager(new LinearLayoutManager(main));




        bd.clearIBT.setOnClickListener(this);
        bd.periodTV.setOnClickListener(this);
        bd.periodTV.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                getQueryHistory(main.vehicleID);
                setQueryCompleteLsitenerAndUpdate();

            }
        });




        getBrands();
        bd.addVehicleBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddVehicleDialog.show(main, result -> {resultFromDialog = result;});
            }
        });



        return view;
    }



    private void initializeRVAdapter(){
        FirestoreRecyclerOptions<History> newOptions = new FirestoreRecyclerOptions.Builder<History>()
                .setQuery(query, History.class)
                .build();

        adapter = new HistoryRVAdapter(newOptions ,main);
        bd.rv.setAdapter(adapter);
    }




    public void getBrands() {

        db.collection("Vehicles").whereEqualTo("userID", uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Vehicle> vehicles = new ArrayList<>();
                            List<String> brandsModels = new ArrayList<>();
                            List<Brands> brands = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Vehicle v = document.toObject(Vehicle.class);
                                v.vehicleID = document.getId();
                                vehicles.add(v);
                                brandsModels.add(v.brand + " " + v.model);


                                Brands b = new Brands();
                                b.name = v.brand;
                                b.setLogoPathStr(main);
                                brands.add(b);

                            }
                            addListToDropDown(vehicles , brandsModels , brands);
                        } else {
                        }
                    }
                });
    }


    @SuppressLint("SetTextI18n")
    private void addListToDropDown(List<Vehicle> vehicles, List<String> brandsModels, List<Brands> brands){


        bd.autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {


                for (Vehicle v: vehicles){
                    String brandModel = v.brand + " " + v.model;
                    if (brandModel.equals(s.toString())){
                        main.vehicleID = v.vehicleID;
                        Drawable dr = Utils.loadImageFromAssets(main, v.logoLocalPath);
                        bd.autoCompleteTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(dr, null, null, null);

                        getQueryHistory(main.vehicleID);
                        //updateAdapter(query);
                        setQueryCompleteLsitenerAndUpdate();

                        break;
                    }
                }

            }
        });


        CustomVehiclesViewAdapter arrayAdapter = new CustomVehiclesViewAdapter(main, R.layout.dropdown_item, vehicles);
        bd.autoCompleteTextView.setAdapter(arrayAdapter);
        if (vehicles.size() > 0) {
            bd.autoCompleteTextView.setText(vehicles.get(0).brand + " " + vehicles.get(0).model, false);
            main.vehicleID = vehicles.get(0).vehicleID;
            Drawable dr = Utils.loadImageFromAssets(main, vehicles.get(0).logoLocalPath);
            bd.autoCompleteTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(dr, null, null, null);

            query = getQueryHistory(main.vehicleID);

        }
    }








    public Query getQueryHistory(String selectedVehicleID) {


        CollectionReference historyRef = db.collection("History");
        query = historyRef.where(Filter.equalTo(Gas.colNames.VEHICLE_ID , selectedVehicleID));//.whereEqualTo(Gas.colNames.VEHICLE_ID, selectedVehicleID);
        if (!bd.periodTV.getText().toString().isEmpty()){
       // if (!bd.periodTV.getText().toString().equals(getString(R.string.select_period))){
            String xaxa = bd.periodTV.getText().toString();
            String [] splitDates = bd.periodTV.getText().toString().split("-");

            Timestamp dateFrom = Utils.convertToTimestamp(splitDates[0].trim());
            Timestamp dateTo = Utils.convertToTimestamp(splitDates[1].trim());
            query = query.whereGreaterThanOrEqualTo("date", dateFrom);
            query = query.whereLessThanOrEqualTo("date", dateTo);
        }


        if (main.maincategoryIDs != null && main.maincategoryIDs.length > 0) {
            Filter[] filters = new Filter[main.maincategoryIDs.length];
            for (int i = 0; i<main.maincategoryIDs.length; i++) {
                int categoryID = main.maincategoryIDs[i];
                filters[i] = Filter.equalTo("categoryID", categoryID);
            }
            query = query.where(Filter.or(filters));
        }
        query = query.orderBy(Gas.colNames.DATE);

        return query;
    }

    private void updateAdapter(){

        FirestoreRecyclerOptions<History> newOptions = new FirestoreRecyclerOptions.Builder<History>()
                .setQuery(query, History.class)
                .build();

        if (adapter == null) {
            adapter = new HistoryRVAdapter(newOptions, main);
            bd.rv.setAdapter(adapter);
        }
        else
            adapter.updateOptions(newOptions);

    }






    public void setQueryCompleteLsitenerAndUpdate(){


        /*
        ΤΟ ΧΡΗΣΙΜΟΠΟΙΩ ΜΕ ΑΥΤΟΝ ΤΡΟΠΟ ΕΠΕΙΔΗ ΟΤΑΝ ΓΙΝΕΤΑΙ ΦΙΛΤΡΑΡΙΣΜΑ ΚΑΙ ΚΑΛΕΣΜΑ ΑΠΟ ΑΛΛΟ ΑΚΤΙΒΙΤΥ Η ΦΡΑΓΚΕΜΝΤ ΧΤΥΠΑΕΙ
        ΕΝΩ ΜΕ ΑΥΤΟΝ ΤΟΝ ΤΡΟΠΟ ΕΙΝΑΙ ΟΚ
         */
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    double total = 0;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        double itemCost = document.getDouble("money");
                        total += itemCost;
                    }


                    if (adapter != null) {
                        int currentSize = adapter.getItemCount();
                        //tell the recycler view that all the old items are gone
                        adapter.notifyItemRangeRemoved(0, currentSize);
                        //tell the recycler view how many new items we added
                        adapter.notifyItemRangeInserted(0, task.getResult().size());

                    }
                    updateAdapter();
                    bd.totalCostTV.setText(String.valueOf(total));

                } else {
                    Toast.makeText(main, "Ανεπιτυχής διαδικασία δεδομένων", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }






    @Override
    public void onClick(View v) {
        if (v.getId() == bd.clearIBT.getId()){
            if (bd.periodTV.getText().toString().isEmpty())
                return;
            bd.periodTV.setText("");
            getQueryHistory(main.vehicleID);
           // setQueryCompleteLsitenerAndUpdate();
        }


        else if (v.getId() == bd.periodTV.getId()){
            Utils.setPeriodPicker(main.getSupportFragmentManager(), bd.periodTV);

        }
        bd.periodTV.setOnClickListener(v1 -> Utils.setPeriodPicker(main.getSupportFragmentManager(), bd.periodTV));
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("selectedPeriod", bd.periodTV.getText().toString());
        outState.putString("totalCost", bd.totalCostTV.getText().toString());
    }



    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStart() {
        super.onStart();

        if (adapter != null)
            adapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null)
            adapter.stopListening();


    }


    @Override
    public void sendFilteredCategoryIDs(Integer[] categoryIDs) {
        getQueryHistory(main.vehicleID);
        setQueryCompleteLsitenerAndUpdate();

    }
}