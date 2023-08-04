package com.example.myvehicleinfojava;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myvehicleinfojava.classes.Brands;
import com.example.myvehicleinfojava.classes.Vehicle;

import java.util.List;

public class CustomVehiclesViewAdapter extends ArrayAdapter<Vehicle> {

    Context ctx;


    public CustomVehiclesViewAdapter(Context ctx, int resourceId, List<Vehicle> items) {
        super(ctx, resourceId, items);
        this.ctx = ctx;

    }


    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        TextView txtTitle;
    }


    @SuppressLint("InflateParams")
    public View getView(int position, View convertView, ViewGroup parent) {
        CustomVehiclesViewAdapter.ViewHolder holder = null;
        Vehicle v = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) ctx
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.custom_array_adapter, null);

            holder = new CustomVehiclesViewAdapter.ViewHolder();
            holder.txtTitle = (TextView) convertView.findViewById(R.id.titleTV);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageIV);
            convertView.setTag(holder);

        } else
            holder = (CustomVehiclesViewAdapter.ViewHolder) convertView.getTag();


        holder.txtTitle.setText(v.brand + " " + v.model);
        holder.imageView.setImageDrawable(Utils.loadImageFromAssets(ctx, v.logoLocalPath));


        return convertView;
    }



}
