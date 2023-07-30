package com.example.myvehicleinfojava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.myvehicleinfojava.classes.Vehicle;
import com.example.myvehicleinfojava.databinding.ActivityMainBinding;
import com.example.myvehicleinfojava.dialogs.AddGasDialog;
import com.example.myvehicleinfojava.dialogs.AddRepairDialog;
import com.example.myvehicleinfojava.dialogs.AddVehicleDialog;
import com.example.myvehicleinfojava.dialogs.FilterHistoryDialog;
import com.example.myvehicleinfojava.fragments.HistoryFragment;
import com.example.myvehicleinfojava.fragments.ProfileFragment;
import com.example.myvehicleinfojava.fragments.SettingsFragment;
import com.example.myvehicleinfojava.fragments.TestFragment;
import com.example.myvehicleinfojava.listeners.GeneralListener;
import com.example.myvehicleinfojava.listeners.HistoryFilterListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final HistoryFragment historyFragment = new HistoryFragment();
    final ProfileFragment profileFragment = new ProfileFragment();
    final SettingsFragment settingsFragment = new SettingsFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = historyFragment;

    public ActionBar actionBar;

    public String resultFromDialog;

    private ActivityMainBinding bd;
    private boolean isAllFabsVisible;
    public FirebaseFirestore db;
    public String uid;
    public String vehicleID = "";
    public Integer[] maincategoryIDs;

    ActionMenuItemView filterMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = ActivityMainBinding.inflate(getLayoutInflater());
        View view = bd.getRoot();
        setContentView(view);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        uid = mAuth.getUid();
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            // providing title for the ActionBar
            actionBar.setTitle("History");
            // methods to display the icon in the ActionBar
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        getBrands();

        manageFabs();

        manageFragments();


        bd.addVehicleBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddVehicleDialog.show(MainActivity.this,  result -> {resultFromDialog = result;});


            }
        });




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected( @NonNull MenuItem item ) {

        if (item.getItemId() == R.id.filter){
            FilterHistoryDialog.show(this, new HistoryFilterListener() {

                @Override
                public void sendFilteredCategoryIDs(Integer[] categoryIDs) {
                   // String [] idsStr = result.replace("[","").replace("]","").split(",");
                    maincategoryIDs = categoryIDs;
                    if (active instanceof HistoryFragment){
                        Query query = historyFragment.getQueryHistory(vehicleID);
                        historyFragment.setQueryCompleteLsitenerAndUpdate(query);

                        //  historyFragment.setFilteredHistoryAdapter(maincategoryIDs);
                    }

                }


            });
        }

        else if (item.getItemId() == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private void manageFragments() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container,active).commit();
        bd.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemID =  item.getItemId();


                if (itemID == R.id.history) {
                    changeFragment(historyFragment, null);
                   // fm.beginTransaction().add(R.id.mainLayout, historyFragment, HistoryFragment.class.getSimpleName()).hide(active).commit();
                    active = historyFragment;
                   // getSupportFragmentManager().beginTransaction().replace(R.id.container, historyFragment).commit();
                    showMainActivitiesChilds();
                    return true;
                }

                else if (itemID == R.id.person){
                    changeFragment(profileFragment, null);

                   // fm.beginTransaction().add(R.id.mainLayout, profileFragment, ProfileFragment.class.getSimpleName()).hide(active).commit();

                    active = profileFragment;
                    //getSupportFragmentManager().beginTransaction().replace(R.id.container,profileFragment).commit();
                    return true;
                }
                else if (itemID == R.id.settings){

                    changeFragment(settingsFragment, null);

                  //  fm.beginTransaction().add(R.id.mainLayout, settingsFragment, SettingsFragment.class.getSimpleName()).hide(active).commit();

                    active = settingsFragment;
                    //getSupportFragmentManager().beginTransaction().replace(R.id.container,settingsFragment).commit();
                    hideMainActivitiesChilds();
                    return true;
                }

                return false;
            }
        });
    }


    private void changeFragment(Fragment fragment, Bundle bundle) {
        fm.beginTransaction()
                .hide(active)
                .show(fragment)
                .commit();
        active = fragment;
        active.setArguments(bundle);
    }


    private void manageFabs() {
        bd.addGasFab.setVisibility(View.GONE);
        bd.addRepairFab.setVisibility(View.GONE);
        bd.addGasActionText.setVisibility(View.GONE);
        bd.addRepairActionText.setVisibility(View.GONE);
        // action name texts and all the sub FABs are
        // invisible
        isAllFabsVisible = false;        // Set the Extended floating action button to
        // shrinked state initially
        bd.fab.shrink();        // We will make all the FABs and action name texts

        bd.fab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!isAllFabsVisible) {                            // when isAllFabsVisible becomes
                            // true make all the action name
                            // texts and FABs VISIBLE.
                            bd.addGasFab.show();
                            bd.addRepairFab.show();
                            bd.addGasActionText
                                    .setVisibility(View.VISIBLE);
                            bd.addRepairActionText
                                    .setVisibility(View.VISIBLE);                            // Now extend the parent FAB, as
                            // user clicks on the shrinked
                            // parent FAB
                            bd.fab.extend();                            // make the boolean variable true as
                            // we have set the sub FABs
                            // visibility to GONE
                            isAllFabsVisible = true;
                        } else {                            // when isAllFabsVisible becomes
                            // true make all the action name
                            // texts and FABs GONE.
                            bd.addGasFab.hide();
                            bd.addRepairFab.hide();
                            bd.addGasActionText
                                    .setVisibility(View.GONE);
                            bd.addRepairActionText
                                    .setVisibility(View.GONE);                            // Set the FAB to shrink after user
                            // closes all the sub FABs
                            bd.fab.shrink();                            // make the boolean variable false
                            // as we have set the sub FABs
                            // visibility to GONE
                            isAllFabsVisible = false;
                        }
                    }
                });


        bd.addGasFab.setOnClickListener(v -> {
            if (!isVehicleIDEmptyAndToast())
                AddGasDialog.show(MainActivity.this, vehicleID, result -> {resultFromDialog = result;});
        });


        bd.addRepairFab.setOnClickListener(v ->{
            if (!isVehicleIDEmptyAndToast())
                AddRepairDialog.show(MainActivity.this, vehicleID, result -> {resultFromDialog = result;});
        });
    }






    public void getBrands() {

        db.collection("Vehicles").whereEqualTo("userID", uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Vehicle> vehicles = new ArrayList<>();
                    List<String> brandsModels = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        Vehicle v = document.toObject(Vehicle.class);
                        v.vehicleID = document.getId();
                        vehicles.add(v);
                        brandsModels.add(v.brand + " " + v.model);
                    }
                    addListToDropDown(vehicles , brandsModels);
                } else {
                }
            }
        });
    }




    private void addListToDropDown(List<Vehicle> vehicles, List<String> brandsModels){


        bd.autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                for (Vehicle v: vehicles){
                    String brandModel = v.brand + " " + v.model;
                    if (brandModel.equals(s.toString())){
                        vehicleID = v.vehicleID;
                        break;
                    }
                }


                if (active instanceof HistoryFragment){
                    GeneralListener x = (GeneralListener) active;
                    x.sendResult(vehicleID);
                }

            }
        });

        //253603757
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.dropdown_item, brandsModels);
        bd.autoCompleteTextView.setAdapter(arrayAdapter);
        if (brandsModels.size() > 0) {
            bd.autoCompleteTextView.setText(brandsModels.get(0) , false);
            vehicleID = vehicles.get(0).vehicleID;
        }
    }

    private boolean isVehicleIDEmptyAndToast(){
        if (vehicleID.isEmpty()){
            Toast.makeText(this, "Δεν υπάρχει επιλεγμένο αυτοκίνητο", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    private void hideMainActivitiesChilds(){
        bd.autoCompleteTextView.setVisibility(View.GONE);
        bd.textInputLayout.setVisibility(View.GONE);
        bd.addVehicleBT.setVisibility(View.GONE);
        filterMenuItem = findViewById(R.id.filter);

        filterMenuItem.setVisibility(View.GONE);

    }

    private void showMainActivitiesChilds(){
        bd.autoCompleteTextView.setVisibility(View.VISIBLE);
        bd.textInputLayout.setVisibility(View.VISIBLE);

        bd.addVehicleBT.setVisibility(View.VISIBLE);
        filterMenuItem = findViewById(R.id.filter);

        filterMenuItem.setVisibility(View.VISIBLE);

    }
}