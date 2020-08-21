package com.rcdhotels.gestiondesolicitudes.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.rcdhotels.gestiondesolicitudes.R;
import com.rcdhotels.gestiondesolicitudes.adapters.UpdExtReqMatRecyclerViewAdapter;
import com.rcdhotels.gestiondesolicitudes.model.Material;
import com.rcdhotels.gestiondesolicitudes.model.UtilsClass;
import com.rcdhotels.gestiondesolicitudes.task.CreateExtRequestAgainAsyncTask;
import com.rcdhotels.gestiondesolicitudes.task.GetExtResquestMaterialsAsyncTask;

import java.util.ArrayList;
import java.util.Objects;

public class ExtRequestAgainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ext_request_again);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle(getString(R.string.applyfor) + " NO. " + UtilsClass.currentRequest.getIDREQUEST());

        new GetExtResquestMaterialsAsyncTask(UtilsClass.currentRequest.getIDREQUEST(), "RequestAgain", ExtRequestAgainActivity.this).execute();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_request_released_reprocessing, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }

        if (item.getItemId() == R.id.action_request_again){
            new CreateExtRequestAgainAsyncTask(UtilsClass.currentRequest.getIDREQUEST(), ExtRequestAgainActivity.this).execute();
        }
        return super.onOptionsItemSelected(item);
    }
}