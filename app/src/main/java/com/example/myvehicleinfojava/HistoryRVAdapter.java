package com.example.myvehicleinfojava;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myvehicleinfojava.classes.Gas;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class HistoryRVAdapter extends FirestoreRecyclerAdapter<Gas, HistoryRVAdapter.ViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     */
    public HistoryRVAdapter(@NonNull FirestoreRecyclerOptions<Gas> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Gas model) {

        holder.moneyET.setText(String.valueOf(model.money));
        holder.litresET.setText(String.valueOf(model.litres));
        holder.dateET.setText(String.valueOf(model.getDate()));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gas_rv, parent, false);
        return new ViewHolder(view);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView litresET, moneyET;
        TextView dateET;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            litresET = itemView.findViewById(R.id.litresTV);
            moneyET = itemView.findViewById(R.id.moneyTV);
            dateET = itemView.findViewById(R.id.dateTV);

        }
    }
}
