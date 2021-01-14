package com.rcdhotels.gestiondesolicitudes.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.rcdhotels.gestiondesolicitudes.R;
import com.rcdhotels.gestiondesolicitudes.login.LoginActivity;
import com.rcdhotels.gestiondesolicitudes.model.Warehouse;
import com.rcdhotels.gestiondesolicitudes.task.UpdateWarehouseAsyncTask;

import java.util.ArrayList;

import static com.rcdhotels.gestiondesolicitudes.database.HotelsTableQuerys.getHotel;
import static com.rcdhotels.gestiondesolicitudes.database.UserTableQuerys.deleteUser;
import static com.rcdhotels.gestiondesolicitudes.database.UserTableQuerys.getUserLogged;
import static com.rcdhotels.gestiondesolicitudes.database.WarehouseTableQuerys.findAllWarehouses;
import static com.rcdhotels.gestiondesolicitudes.database.WarehouseTableQuerys.findWarehouseById;
import static com.rcdhotels.gestiondesolicitudes.model.UtilsClass.resetUtilsValues;
import static com.rcdhotels.gestiondesolicitudes.model.UtilsClass.user;

public class MainActivity extends AppCompatActivity{

    private AppBarConfiguration mAppBarConfiguration;
    private Context context;
    private boolean doubleBackToExitPressedOnce = false;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = MainActivity.this;
        if(user == null){
           getUserLogged(MainActivity.this);
           user.setHotel(getHotel(context, user.getHotel().getIdHotel()));
        }

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_extraction, R.id.nav_return, R.id.nav_warehouse).setDrawerLayout(drawer).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.textViewFullName);
        navUsername.setText(new StringBuilder().append(user.getFirstname()).append(" ").append(user.getSecondName()).append(" ").append(user.getSurName()).append(" ").append(user.getSecondSurName()).toString());
        Warehouse warehouse = findWarehouseById(user.getWarehouse(), context);

        TextView textViewWarehouse = headerView.findViewById(R.id.textViewWarehouse);
        if (!user.getRole().equalsIgnoreCase("GS_AUTOR2") && !user.getRole().equalsIgnoreCase("GS_AUTOR3") && !user.getRole().equalsIgnoreCase("GS_REPRO")) {
            textViewWarehouse.setText(warehouse.getStgeLoc() + " - " + warehouse.getLgobe());
        }
        else{
            textViewWarehouse.setText(user.getHotel().getNameHotel());
        }

        Menu menu = navigationView.getMenu();
        if (user.getRole().equalsIgnoreCase("GS_SOLIC2")){
            menu.findItem(R.id.nav_warehouse).setVisible(true);
            menu.findItem(R.id.nav_warehouse).setOnMenuItemClickListener(item -> {
                drawer.closeDrawer(GravityCompat.START);
                ArrayList<Warehouse> warehouses = findAllWarehouses(MainActivity.this);
                String[] array = new String[warehouses.size()];
                for (int i = 0; i < warehouses.size(); i++) {
                    array[i] = warehouses.get(i).getLgobe();
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setTitle(R.string.select_warehouse);
                builder.setItems(array, (dialog, which) -> {
                    user.setWarehouse(warehouses.get(which).getStgeLoc());
                    new UpdateWarehouseAsyncTask(MainActivity.this, textViewWarehouse, warehouses.get(which)).execute();
                });
                builder.show();
                return false;
            });
        }

        menu.findItem(R.id.nav_logout).setOnMenuItemClickListener(item -> {
            signOut();
            return false;
        });
    }

    public void signOut() {
        resetUtilsValues();
        deleteUser(MainActivity.this);
        SharedPreferences.Editor editor = context.getSharedPreferences("Preferences_GS", MODE_PRIVATE).edit();
        editor.putBoolean("isLogged", false);
        editor.apply();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
            //Close Drawer menu
            drawer.closeDrawer(GravityCompat.START);
        else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            } else {
                doubleBackToExitPressedOnce = true;
                View parentLayout = findViewById(android.R.id.content);
                Snackbar.make(parentLayout, R.string.exit_message, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
            }
        }
    }
}
