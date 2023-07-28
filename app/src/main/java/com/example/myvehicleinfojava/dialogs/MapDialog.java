package com.example.myvehicleinfojava.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;


import androidx.annotation.NonNull;

import com.example.myvehicleinfojava.R;

import com.example.myvehicleinfojava.listeners.GeneralListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapDialog implements OnMapReadyCallback{
    private GoogleMap mMap;
    private MapView mapView;
    private Marker marker;

    private static final LatLng ONE = new LatLng(32.882216, -117.222028);
    private static final LatLng TWO = new LatLng(32.872000, -117.232004);
    private static final LatLng THREE = new LatLng(32.880252, -117.233034);
    private static final LatLng FOUR = new LatLng(32.885200, -117.226003);
    private ArrayList<LatLng> coords = new ArrayList<LatLng>();
    private static final int POINTS = 4;

    public  void show(Activity act , GeneralListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(act);
        // builder.setTitle(Collections.REPAIRS.text);
        builder.setTitle("Map");

        // set the custom layout
        final View customLayout = act.getLayoutInflater().inflate(R.layout.show_map, null);
        builder.setView(customLayout);
        MapView mapView = customLayout.findViewById(R.id.mapView);


        mapView.getMapAsync(this);



        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                setUpMap(googleMap);
            }
        });
        // add a button
        builder.setPositiveButton("OK", (dialog, which) -> {



        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void setUpMap(GoogleMap map) {
        mMap = map;

        coords.add(ONE);
        coords.add(TWO);
        coords.add(THREE);
        coords.add(FOUR);
        for (int i = 0; i < POINTS; i++) {
            mMap.addMarker(new MarkerOptions()
                    .position(coords.get(i))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }
}
