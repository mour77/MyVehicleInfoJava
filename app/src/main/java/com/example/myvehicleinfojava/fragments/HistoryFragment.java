package com.example.myvehicleinfojava.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myvehicleinfojava.Utils;
import com.example.myvehicleinfojava.classes.History;
import com.example.myvehicleinfojava.HistoryRVAdapter;
import com.example.myvehicleinfojava.MainActivity;
import com.example.myvehicleinfojava.classes.Gas;
import com.example.myvehicleinfojava.databinding.FragmentHistoryBinding;
import com.example.myvehicleinfojava.listeners.GeneralListener;
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


public class HistoryFragment extends Fragment implements GeneralListener , View.OnClickListener {

    MainActivity main;
    FragmentHistoryBinding bd ;
    FirebaseFirestore db;

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
        bd.rv.setLayoutManager(new LinearLayoutManager(main));

        Query query = getQueryHistory(main.vehicleID);
        updateAdapter(query);

        bd.rv.setAdapter(adapter);

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
                Query q = getQueryHistory(main.vehicleID);
                setQueryCompleteLsitenerAndUpdate(q);

            }
        });

        return view;
    }





    public Query getQueryHistory(String selectedVehicleID) {

        CollectionReference historyRef = db.collection("History");
        Query query = historyRef.where(Filter.equalTo(Gas.colNames.VEHICLE_ID , selectedVehicleID));//.whereEqualTo(Gas.colNames.VEHICLE_ID, selectedVehicleID);
        if (!bd.periodTV.getText().toString().isEmpty()){
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

        Query query = getQueryHistory(result);
        setQueryCompleteLsitenerAndUpdate(query);


    }

    public void setQueryCompleteLsitenerAndUpdate(Query query){

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


                    int currentSize = adapter.getItemCount();
                    //tell the recycler view that all the old items are gone
                    adapter.notifyItemRangeRemoved(0, currentSize);
                    //tell the recycler view how many new items we added
                    adapter.notifyItemRangeInserted(0, task.getResult().size());

                    updateAdapter(query);
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
            Query q = getQueryHistory(main.vehicleID)        ;
            setQueryCompleteLsitenerAndUpdate(q);
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
        Query q = getQueryHistory(main.vehicleID)        ;
        setQueryCompleteLsitenerAndUpdate(q);
    }

    @Override
    public void onStart() {
        super.onStart();

        //adapter.opt.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
        adapter=null;

    }


}