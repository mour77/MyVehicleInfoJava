package com.example.myvehicleinfojava;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myvehicleinfojava.classes.Notification;
import com.example.myvehicleinfojava.classes.TargetAndMovements;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class MovementsRVAdapter extends FirestoreRecyclerAdapter<TargetAndMovements, MovementsRVAdapter.ViewHolder>

    {


        public MovementsRVAdapter(@NonNull FirestoreRecyclerOptions<TargetAndMovements> options) {
            super(options);
        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movements_rv, parent, false);
            return new MovementsRVAdapter.ViewHolder(view);
        }

        @Override
        public int getItemViewType ( int position){
            return super.getItemViewType(position);

    }


        @Override
        protected void onBindViewHolder (@NonNull MovementsRVAdapter.ViewHolder holder,int position, @NonNull TargetAndMovements model){

            holder.titleTV.setText(model.title);
            holder.moneyTV.setText(String.valueOf(model.cost));
            holder.dateTV.setText(String.valueOf(model.getDate()));


    }


        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView titleTV;
            TextView dateTV, moneyTV;


            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                titleTV = itemView.findViewById(R.id.titleTV);
                moneyTV = itemView.findViewById(R.id.moneyTV);
                dateTV = itemView.findViewById(R.id.dateTV);

            }
        }

}
