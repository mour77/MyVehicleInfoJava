package com.example.myvehicleinfojava.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.myvehicleinfojava.CustomBrandsViewAdapter;
import com.example.myvehicleinfojava.Utils;
import com.example.myvehicleinfojava.classes.GasTypes;
import com.example.myvehicleinfojava.MainActivity;
import com.example.myvehicleinfojava.R;
import com.example.myvehicleinfojava.classes.Brands;
import com.example.myvehicleinfojava.classes.Vehicle;
import com.example.myvehicleinfojava.listeners.GeneralListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddVehicleDialog {

    private static FirebaseFirestore db;
    private static int brandID = 0 ;
    private static String brandIDStr = "" ;
    private static String brandName = "" ;
    private static int gasTypeID = 0 ;
    private static String gasTypeIDStr = ""  ;
    private AutoCompleteTextView brandsATV;
    static Brands selectedItem ;

    public static void show(Activity act , GeneralListener listener){
        db = FirebaseFirestore.getInstance();

        AlertDialog.Builder builder = new AlertDialog.Builder(act);
        builder.setTitle("Προσθήκη οχήματος");

        // set the custom layout
        final View customLayout = act.getLayoutInflater().inflate(R.layout.add_vehicle_dialog, null);
        builder.setView(customLayout);
        AutoCompleteTextView brandsATV = customLayout.findViewById(R.id.brandsATV);

        Drawable newDrawable = ContextCompat.getDrawable(act, R.drawable.add_car_24);
        brandsATV.setCompoundDrawablesRelativeWithIntrinsicBounds(newDrawable, null, null, null);

        EditText modelET = customLayout.findViewById(R.id.modelET);
        EditText kivikaET = customLayout.findViewById(R.id.kivikaET);
        EditText hpET = customLayout.findViewById(R.id.hpET);
        EditText yearET = customLayout.findViewById(R.id.yearET);
        EditText plateET = customLayout.findViewById(R.id.plateET);
        AutoCompleteTextView typeGasATV = customLayout.findViewById(R.id.typeGasATV);
        EditText capacityET = customLayout.findViewById(R.id.capacityET);
        EditText arKikloforiasET = customLayout.findViewById(R.id.arKikloforiasET);
        EditText vinET = customLayout.findViewById(R.id.vinET);

        try{
            getBrands(act, brandsATV);
            getGasTypes(typeGasATV);
        }
        catch (Exception e){
            e.printStackTrace();
        }




        // add a button
        builder.setPositiveButton("OK", (dialog, which) -> {

            if (!areNeccesaryTextsFilled(brandsATV, modelET)){
                Toast.makeText(act, "Τα πεδία Μάρκα και Μοντέλο είναι υποχρεωτικά", Toast.LENGTH_SHORT).show();
                return;
            }

            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            FirebaseUser user = mAuth.getCurrentUser();

            Map<String, Object> vehicleMap = new HashMap<>();
            vehicleMap.put(Vehicle.colNames.USER_ID, user.getUid());

            vehicleMap.put(Vehicle.colNames.BRAND, brandName);
            //vehicleMap.put("brandIDStr", brandIDStr);

            vehicleMap.put(Vehicle.colNames.MODEL, modelET.getText().toString().trim());
            vehicleMap.put(Vehicle.colNames.KIVIKA, Utils.checkAndParseInteger(kivikaET.getText().toString().trim()));
            vehicleMap.put(Vehicle.colNames.HP,  Utils.checkAndParseInteger(hpET.getText().toString().trim()));
            vehicleMap.put(Vehicle.colNames.YEAR, Utils.checkAndParseInteger(yearET.getText().toString().trim()));

            vehicleMap.put(Vehicle.colNames.GAS_TYPE_ID_STR, gasTypeIDStr);
            vehicleMap.put(Vehicle.colNames.GAS_TYPE_ID_INT, gasTypeID);

            vehicleMap.put(Vehicle.colNames.CAPACITY, Utils.checkAndParseInteger(capacityET.getText().toString().trim()));

            vehicleMap.put(Vehicle.colNames.PLATE_ID,plateET.getText().toString().trim());
            vehicleMap.put(Vehicle.colNames.ARITHMOS_KIKLOFORIAS, arKikloforiasET.getText().toString().trim());
            vehicleMap.put(Vehicle.colNames.VIN,vinET.getText().toString().trim());

            vehicleMap.put(Vehicle.colNames.LOGO_LOCAL_PATH , Utils.getLogoPathStrByBrandName(act, brandName));





            db.collection("Vehicles").document()
                    .set(vehicleMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(act, R.string.successful_save, Toast.LENGTH_SHORT).show();
                            if (act instanceof MainActivity)
                                ((MainActivity) act).getBrands();

                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(act, R.string.error_save, Toast.LENGTH_SHORT).show());



        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }



    private static boolean areNeccesaryTextsFilled(AutoCompleteTextView brandsATV, EditText modelET){
        return !brandsATV.getText().toString().isEmpty() && !modelET.getText().toString().isEmpty();

    }


    private static void getBrands(Activity act, AutoCompleteTextView brandsATV) throws JSONException {

        String jsonStr = Utils.loadJSONFromAsset(act, "dataManufacturesLogos.json");
        JSONArray info = new JSONArray(jsonStr);

        List<Brands> brands = new ArrayList<>();
        for (int i=0; i<info.length(); i++){
            Brands b = new Brands();
            String name = info.getJSONObject(i).getString("name");
            String logoThumbStr = info.getJSONObject(i).getJSONObject("image").getString("localThumb");
            b.name = name;
            b.logoPathStr = logoThumbStr;
            brands.add(b);
        }

        addListToBrandsDropDown(brandsATV , brands);


    }





    private static void addListToBrandsDropDown(AutoCompleteTextView atv, List<Brands> brands){


        atv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Brands x = (Brands) parent.getItemAtPosition(position);
                Drawable dr = Utils.loadImageFromAssets(atv.getContext(), x.logoPathStr);
                atv.setCompoundDrawablesRelativeWithIntrinsicBounds(dr, null, null, null);
            }
        });




        CustomBrandsViewAdapter adapter = new CustomBrandsViewAdapter(atv.getContext(), R.layout.custom_array_adapter, brands);
        atv.setAdapter(adapter);

        //listView.setOnItemClickListener(this);
        //253603757

        //ArrayAdapter arrayAdapter = new ArrayAdapter(atv.getContext(), R.layout.dropdown_item, Brands.convertListToNameList(brands));
       // atv.setAdapter(arrayAdapter);

    }





    private static void getGasTypes(AutoCompleteTextView gasATV) {



        db.collection("GasTypes").orderBy("Name")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<GasTypes> gasTypes = new ArrayList<>();
                            //List<String> brandsModels = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                GasTypes gt = document.toObject(GasTypes.class);
                                gt.ID_str = document.getId();
                                gasTypes.add(gt);
                                //brandsModels.add(b.name);
                            }
                            addListToGasTypesDropDown(gasATV , gasTypes);
                        }
                    }


                    private void addListToGasTypesDropDown(AutoCompleteTextView gasATV, List<GasTypes> gasTypes) {

                        gasATV.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                                for (GasTypes x: gasTypes){
                                    String gasTypeName = x.Name;
                                    if (gasTypeName.equals(s.toString())){
                                        gasTypeID = x.ID;
                                        gasTypeIDStr = x.ID_str;
                                        break;
                                    }
                                }
                                Toast.makeText(gasATV.getContext(), gasTypeIDStr, Toast.LENGTH_SHORT).show();



                            }
                        });

                        //253603757
                        ArrayAdapter arrayAdapter = new ArrayAdapter(gasATV.getContext(), R.layout.dropdown_item, GasTypes.convertListToNameList(gasTypes));
                        gasATV.setAdapter(arrayAdapter);

                    }
                });
    }








}
