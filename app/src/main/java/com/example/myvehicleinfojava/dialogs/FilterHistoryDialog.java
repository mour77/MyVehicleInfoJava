package com.example.myvehicleinfojava.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.myvehicleinfojava.R;
import com.example.myvehicleinfojava.classes.Filters;
import com.example.myvehicleinfojava.listeners.GeneralListener;
import com.example.myvehicleinfojava.listeners.HistoryFilterListener;

import java.util.ArrayList;
import java.util.Arrays;

public class FilterHistoryDialog {

    public static void show(Activity act , HistoryFilterListener listener){


        ArrayList<Filters> filtersList = Filters.getHistoryFilterList();
        AlertDialog.Builder builder = new AlertDialog.Builder(act);
        builder.setTitle("Φίλτρα");


        // set the custom layout
        final View customLayout = act.getLayoutInflater().inflate(R.layout.filter_history_dialog, null);
        builder.setView(customLayout);
        ListView filterList = customLayout.findViewById(R.id.filterListLV);

        filterList.setAdapter(new CustomListViewAdapter(filtersList,act ));

        // add a button
        builder.setPositiveButton("OK", (dialog, which) -> {
            //String [] x = (String[]) Filters.getFilteredCategories(filtersList);
            Integer [] categoryIDs =  Filters.getFilteredCategories(filtersList);
            //String arrayIDs  = Arrays.toString(categoryIDs).trim();
            listener.sendFilteredCategoryIDs(categoryIDs);



        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }








    private static class CustomListViewAdapter extends ArrayAdapter<Filters> {

        private ArrayList<Filters> dataSet;
        Context mContext;

        // View lookup cache
        private static class ViewHolder {
            CheckedTextView txtName;


        }

        public CustomListViewAdapter(ArrayList<Filters> data, Context context) {
            super(context, R.layout.child_of_filter_list_dialog, data);
            this.dataSet = data;
            this.mContext=context;

        }



        private int lastPosition = -1;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Filters dataModel = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            ViewHolder viewHolder; // view lookup cache stored in tag

            final View result;

            if (convertView == null) {

                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.child_of_filter_list_dialog, parent, false);
                viewHolder.txtName = convertView.findViewById(R.id.check_box);

                result=convertView;

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
                result=convertView;
            }

            Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
            result.startAnimation(animation);
            lastPosition = position;

            viewHolder.txtName.setText(dataModel.name);
            viewHolder.txtName.setChecked(dataModel.isChecked);
            viewHolder.txtName.setTag(position);

            viewHolder.txtName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    viewHolder.txtName.setChecked(!viewHolder.txtName.isChecked());
                    int pos = (int) viewHolder.txtName.getTag();
                    dataSet.get(pos).isChecked = viewHolder.txtName.isChecked();

//                    Snackbar.make(v,   dataSet.get(pos).name + " " + dataSet.get(pos).isChecked, Snackbar.LENGTH_LONG)
//                            .setAction("No action", null).show();
                }
            });

            // Return the completed view to render on screen
            return convertView;
        }
    }






}
