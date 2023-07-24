package com.example.myvehicleinfojava;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myvehicleinfojava.classes.Gas;
import com.example.myvehicleinfojava.classes.History;
import com.example.myvehicleinfojava.firebaseClasses.Categories;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class HistoryRVAdapter extends FirestoreRecyclerAdapter<History, HistoryRVAdapter.ViewHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     */
    public HistoryRVAdapter(@NonNull FirestoreRecyclerOptions<History> options) {
        super(options);
    }





    @Override
    public int getItemViewType(int position) {

//        return super.getItemViewType(position);
        return getItem(position).categoryID;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view ;
        if (viewType == Categories.GAS)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gas_rv, parent, false);
        else if (viewType == Categories.REPAIR)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.repair_rv, parent, false);

        else
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gas_rv, parent, false);


        return new ViewHolder(view);
    }


    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull History model) {

        holder.moneyTV.setText(String.valueOf(model.money));
        holder.dateTV.setText(String.valueOf(model.getDate()));

        if (model.categoryID == Categories.GAS){
            holder.litresTV.setText(String.valueOf(model.litres));
            holder.kmTV.setText(String.valueOf(model.odometer));
        }

        else if (model.categoryID == Categories.REPAIR){
            holder.descriptionTV.setText(model.description);
            holder.kmTV.setText(String.valueOf(model.odometer));
            holder.remarksTV.setText(String.valueOf(model.remarks));
        }

    }





    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView litresTV, moneyTV ,descriptionTV ,remarksTV;
        TextView dateTV , kmTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            moneyTV = itemView.findViewById(R.id.moneyTV);
            dateTV = itemView.findViewById(R.id.dateTV);

            litresTV = itemView.findViewById(R.id.litresTV);
            descriptionTV = itemView.findViewById(R.id.descriptionTV);
            remarksTV = itemView.findViewById(R.id.remarksTV);
            kmTV = itemView.findViewById(R.id.kmTV);


        }
    }
}
