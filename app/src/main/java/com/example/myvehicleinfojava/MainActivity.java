package com.example.myvehicleinfojava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.example.myvehicleinfojava.databinding.ActivityMainBinding;
import com.example.myvehicleinfojava.dialogs.AddGasDialog;
import com.example.myvehicleinfojava.dialogs.AddNotificationDialog;
import com.example.myvehicleinfojava.dialogs.AddRepairDialog;
import com.example.myvehicleinfojava.dialogs.AddTargetDialog;
import com.example.myvehicleinfojava.dialogs.FilterHistoryDialog;
import com.example.myvehicleinfojava.fragments.HistoryFragment;
import com.example.myvehicleinfojava.fragments.MyTargetsFragment;
import com.example.myvehicleinfojava.fragments.NotificationFragment;
import com.example.myvehicleinfojava.fragments.SettingsFragment;
import com.example.myvehicleinfojava.listeners.GeneralListener;
import com.example.myvehicleinfojava.listeners.HistoryFilterListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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
    final NotificationFragment notificationFragment = new NotificationFragment();
    final MyTargetsFragment myTargetsFragment = new MyTargetsFragment();
    final SettingsFragment settingsFragment = new SettingsFragment();
    final FragmentManager fm = getSupportFragmentManager();
    public Fragment active = historyFragment;

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
            actionBar.setTitle("Ιστορικό");
            // methods to display the icon in the ActionBar
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

       // getBrands();

        manageFabs();
//        bd.bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//        fm.beginTransaction().add(R.id.container,settingsFragment, "3").hide(settingsFragment).commit();
//        fm.beginTransaction().add(R.id.container,notificationFragment, "2").hide(notificationFragment).commit();
//        fm.beginTransaction().add(R.id.container,historyFragment, "1").commit();
        manageFragments();






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
                         historyFragment.getQueryHistory(vehicleID);
                         historyFragment.setQueryCompleteLsitenerAndUpdate();

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
        //getSupportFragmentManager().beginTransaction().replace(R.id.container,active).commit();
        fm.beginTransaction().add(R.id.container,settingsFragment, "4").hide(settingsFragment).commit();
        fm.beginTransaction().add(R.id.container,myTargetsFragment, "3").hide(myTargetsFragment).commit();
        fm.beginTransaction().add(R.id.container,notificationFragment, "2").hide(notificationFragment).commit();
        fm.beginTransaction().add(R.id.container,historyFragment, "1").commit();
        bd.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemID =  item.getItemId();


                if (itemID == R.id.history) {
                    fm.beginTransaction().hide(active).show(historyFragment).commit();

                    active = historyFragment;
                  //  getSupportFragmentManager().beginTransaction().replace(R.id.container, historyFragment).commit();
                    showMainActivitiesChilds();
                    actionBar.setTitle("Ιστορικό");

                    return true;
                }

                else if (itemID == R.id.notifications){
                    fm.beginTransaction().hide(active).show(notificationFragment).commit();

                    active = notificationFragment;
                   // getSupportFragmentManager().beginTransaction().replace(R.id.container,notificationFragment).commit();
                    hideMainActivitiesChilds();
                    actionBar.setTitle("Ειδοποιήσεις");

                    return true;
                }

                else if (itemID == R.id.targets){
                    fm.beginTransaction().hide(active).show(myTargetsFragment).commit();

                    active = myTargetsFragment;
                   // getSupportFragmentManager().beginTransaction().replace(R.id.container,notificationFragment).commit();
                    hideMainActivitiesChilds();
                    actionBar.setTitle("Οι στόχοι μου");

                    return true;
                }
                else if (itemID == R.id.settings){

                    fm.beginTransaction().hide(active).show(settingsFragment).commit();

                    active = settingsFragment;
                    //getSupportFragmentManager().beginTransaction().replace(R.id.container,settingsFragment).commit();
                    hideMainActivitiesChilds();
                    actionBar.setTitle("Ρυθμίσεις");

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
        bd.addTargetFab.setVisibility(View.GONE);
        bd.addGasActionText.setVisibility(View.GONE);
        bd.addRepairActionText.setVisibility(View.GONE);
        bd.addTargetActionText.setVisibility(View.GONE);
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
                            showFabs();
                        } else {                            // when isAllFabsVisible becomes
                            hideFabs();
                        }
                    }
                });




        bd.addGasFab.setOnClickListener(v -> {
            if (!isVehicleIDEmptyAndToast())
                AddGasDialog.show(MainActivity.this, vehicleID);
            hideFabs();

        });


        bd.addRepairFab.setOnClickListener(v ->{
            if (!isVehicleIDEmptyAndToast())
                AddRepairDialog.show(MainActivity.this, vehicleID);
            hideFabs();

        });


//        bd.addNotificationFab.setOnClickListener(v ->{
//            if (!isVehicleIDEmptyAndToast())
//                AddNotificationDialog.show(MainActivity.this, result -> {resultFromDialog = result;});
//            hideFabs();
//
//        });
        bd.addTargetFab.setOnClickListener(v ->{
            AddTargetDialog.show(MainActivity.this, result -> {resultFromDialog = result;});
            hideFabs();

        });




    }


    private void showFabs(){
        // true make all the action name
        // texts and FABs VISIBLE.
        bd.addGasFab.show();
        bd.addRepairFab.show();
        bd.addTargetFab.show();

        bd.addGasActionText.setVisibility(View.VISIBLE);
        bd.addRepairActionText.setVisibility(View.VISIBLE);                            // Now extend the parent FAB, as
        bd.addTargetActionText.setVisibility(View.VISIBLE);                            // Now extend the parent FAB, as
        // user clicks on the shrinked
        // parent FAB
        bd.fab.extend();                            // make the boolean variable true as
        // we have set the sub FABs
        // visibility to GONE
        isAllFabsVisible = true;
    }

    private void hideFabs(){
        // true make all the action name
        // texts and FABs GONE.
        bd.addGasFab.hide();
        bd.addRepairFab.hide();
        bd.addTargetFab.hide();

        bd.addGasActionText.setVisibility(View.GONE);
        bd.addRepairActionText.setVisibility(View.GONE);                            // Set the FAB to shrink after user
        bd.addTargetActionText.setVisibility(View.GONE);                            // Set the FAB to shrink after user
        // closes all the sub FABs
        bd.fab.shrink();                            // make the boolean variable false
        // as we have set the sub FABs
        // visibility to GONE
        isAllFabsVisible = false;
    }



    private boolean isVehicleIDEmptyAndToast(){
        if (vehicleID.isEmpty()){
            Toast.makeText(this, "Δεν υπάρχει επιλεγμένο αυτοκίνητο", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }





    private void hideMainActivitiesChilds(){
//        bd.autoCompleteTextView.setVisibility(View.GONE);
//        bd.textInputLayout.setVisibility(View.GONE);
//        bd.addVehicleBT.setVisibility(View.GONE);
        filterMenuItem = findViewById(R.id.filter);

        filterMenuItem.setVisibility(View.GONE);

    }

    private void showMainActivitiesChilds(){
//        bd.autoCompleteTextView.setVisibility(View.VISIBLE);
//        bd.textInputLayout.setVisibility(View.VISIBLE);
//
//        bd.addVehicleBT.setVisibility(View.VISIBLE);
        filterMenuItem = findViewById(R.id.filter);

        filterMenuItem.setVisibility(View.VISIBLE);

    }
}