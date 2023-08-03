package com.example.myvehicleinfojava.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myvehicleinfojava.HistoryRVAdapter;
import com.example.myvehicleinfojava.MainActivity;
import com.example.myvehicleinfojava.NotificationsRVAdapter;
import com.example.myvehicleinfojava.R;
import com.example.myvehicleinfojava.Utils;
import com.example.myvehicleinfojava.classes.Gas;
import com.example.myvehicleinfojava.classes.History;
import com.example.myvehicleinfojava.classes.Notification;
import com.example.myvehicleinfojava.databinding.FragmentHistoryBinding;
import com.example.myvehicleinfojava.databinding.FragmentNotificationBinding;
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


public class NotificationFragment extends Fragment {

    MainActivity main;
    FragmentNotificationBinding bd ;
    FirebaseFirestore db;

    NotificationsRVAdapter adapter;

    Query query;

    public NotificationFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main = (MainActivity) getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        bd = FragmentNotificationBinding.inflate(inflater, container, false);
        View view = bd.getRoot();


        db = FirebaseFirestore.getInstance();
        bd.rv.setLayoutManager(new LinearLayoutManager(main));


        query = getQueryNotifications();
        updateAdapter(query);
        //setQueryCompleteLsitenerAndUpdate(query);


        return view;
    }



    private Query getQueryNotifications() {
        CollectionReference historyRef = db.collection("Notifications");
        Query query = historyRef.whereEqualTo("uid",main.uid);//.whereEqualTo(Gas.colNames.VEHICLE_ID, selectedVehicleID);
//        if (!bd.periodTV.getText().toString().isEmpty()){
//            String [] splitDates = bd.periodTV.getText().toString().split("-");
//
//            Timestamp dateFrom = Utils.convertToTimestamp(splitDates[0].trim());
//            Timestamp dateTo = Utils.convertToTimestamp(splitDates[1].trim());
//            query = query.whereGreaterThanOrEqualTo("date", dateFrom);
//            query = query.whereLessThanOrEqualTo("date", dateTo);
//        }


        query = query.orderBy(Notification.colNames.DATE);

        return query;
    }


    private void updateAdapter(Query query) {
        FirestoreRecyclerOptions<Notification> newOptions = new FirestoreRecyclerOptions.Builder<Notification>()
                .setQuery(query, Notification.class)
                .build();

        if (adapter == null) {
            adapter = new NotificationsRVAdapter(newOptions);
            bd.rv.setAdapter(adapter);
        }

        else
            adapter.updateOptions(newOptions);

    }


    public void setQueryCompleteLsitenerAndUpdate(Query query){


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