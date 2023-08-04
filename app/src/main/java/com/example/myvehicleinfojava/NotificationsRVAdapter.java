package com.example.myvehicleinfojava;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myvehicleinfojava.classes.History;
import com.example.myvehicleinfojava.classes.Notification;
import com.example.myvehicleinfojava.firebaseClasses.Categories;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class NotificationsRVAdapter extends FirestoreRecyclerAdapter<Notification, NotificationsRVAdapter.ViewHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     */
    public NotificationsRVAdapter(@NonNull FirestoreRecyclerOptions<Notification> options) {
        super(options);
    }





    @Override
    public int getItemViewType(int position) {

        return super.getItemViewType(position);
       // return getItem(position).categoryID;

    }

    @NonNull
    @Override
    public NotificationsRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_rv, parent, false);

        return new NotificationsRVAdapter.ViewHolder(view);
    }


    @Override
    protected void onBindViewHolder(@NonNull NotificationsRVAdapter.ViewHolder holder, int position, @NonNull Notification model) {

        holder.descriptionTV.setText(String.valueOf(model.description));
        holder.dateTV.setText(String.valueOf(model.getDate()));


    }





    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView descriptionTV ;
        TextView dateTV , kmTV;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            descriptionTV = itemView.findViewById(R.id.descriptionTV);
            dateTV = itemView.findViewById(R.id.dateTV);

        }
    }
}
