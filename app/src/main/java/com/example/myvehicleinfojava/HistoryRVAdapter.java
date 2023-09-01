package com.example.myvehicleinfojava;

import android.app.Activity;
import android.media.Image;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myvehicleinfojava.classes.Gas;
import com.example.myvehicleinfojava.classes.History;
import com.example.myvehicleinfojava.dialogs.AddGasDialog;
import com.example.myvehicleinfojava.dialogs.AddRepairDialog;
import com.example.myvehicleinfojava.firebaseClasses.Categories;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Field;

public class HistoryRVAdapter extends FirestoreRecyclerAdapter<History, HistoryRVAdapter.ViewHolder> {

    private final Activity act;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     */

    private FirebaseFirestore db;
    public HistoryRVAdapter(@NonNull FirestoreRecyclerOptions<History> options,  Activity act) {
        super(options);
        db = FirebaseFirestore.getInstance();
        this.act = act;

    }





    @Override
    public int getItemViewType(int position) {

      //  return super.getItemViewType(position);
        return getItem(position).categoryID; // για να γινει ο διαχωρισμος των layout ε βαση το categoryID

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
            if (model.nextRepairOdometer > 0) {
                holder.nextRepairOdometerET.setVisibility(View.VISIBLE);
                holder.nextRepairOdometerET.setText(String.valueOf(model.nextRepairOdometer));
            }
            else
                holder.nextRepairOdometerET.setVisibility(View.GONE);

            holder.remarksTV.setText(String.valueOf(model.remarks));


        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {


                PopupMenu popup = new PopupMenu(v.getContext(), v);
                popup.getMenuInflater().inflate(R.menu.rv_menu,popup.getMenu());
                popup.setGravity(Gravity.END);


                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.edit){
                            if (model.categoryID == Categories.REPAIR)
                                AddRepairDialog.show(act, model.vehicleID, model);

                            else if (model.categoryID == Categories.GAS)
                                AddGasDialog.show(act, model.vehicleID, model);

                        }

                        if (item.getItemId() == R.id.delete){
                            db.collection("History").document(model.documentID)
                                    .delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(v.getContext(), R.string.successful_save, Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(v.getContext(), R.string.error_save, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            return true;
                        }
                        return false;
                    }
                });



                popup.show();

                return false;
            }
        });


    }





    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView litresTV, moneyTV ,descriptionTV ,remarksTV;
        TextView dateTV , kmTV ;

        EditText nextRepairOdometerET;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            moneyTV = itemView.findViewById(R.id.moneyTV);
            dateTV = itemView.findViewById(R.id.dateTV);

            litresTV = itemView.findViewById(R.id.litresTV);
            descriptionTV = itemView.findViewById(R.id.descriptionTV);
            remarksTV = itemView.findViewById(R.id.remarksTV);
            kmTV = itemView.findViewById(R.id.kmTV);
            nextRepairOdometerET = itemView.findViewById(R.id.nextRepairET);





        }
    }



}
