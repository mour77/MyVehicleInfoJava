package com.example.myvehicleinfojava;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.myvehicleinfojava.classes.Brands;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CustomListViewAdapter extends ArrayAdapter<Brands> {

    Context ctx;
    private String selectedBrand;
    private Drawable selectedLogo;

    public CustomListViewAdapter(Context ctx, int resourceId, //resourceId=your layout
                                 List<Brands> items) {
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
        ViewHolder holder = null;
        Brands brandItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) ctx
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.custom_array_adapter, null);

            holder = new ViewHolder();
            holder.txtTitle = (TextView) convertView.findViewById(R.id.titleTV);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageIV);
            convertView.setTag(holder);

        } else
            holder = (ViewHolder) convertView.getTag();


        holder.txtTitle.setText(brandItem.name);
        holder.imageView.setImageDrawable(Utils.loadImageFromAssets(ctx, brandItem.logoPathStr));


        return convertView;
    }


}