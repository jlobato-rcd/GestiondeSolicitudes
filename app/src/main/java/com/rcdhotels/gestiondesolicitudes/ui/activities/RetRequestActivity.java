package com.rcdhotels.gestiondesolicitudes.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.rcdhotels.gestiondesolicitudes.R;
import com.rcdhotels.gestiondesolicitudes.adapters.ExtReqMatRecyclerViewAdapter;
import com.rcdhotels.gestiondesolicitudes.model.UtilsClass;
import com.rcdhotels.gestiondesolicitudes.model.Warehouse;
import com.rcdhotels.gestiondesolicitudes.task.CreateRetRequestAsyncTask;

import java.util.ArrayList;
import java.util.Objects;

import static com.rcdhotels.gestiondesolicitudes.database.WarehouseTableQuerys.findAllWarehousesByZsumi;
import static com.rcdhotels.gestiondesolicitudes.utils.Tools.hideKeyboard;

public class RetRequestActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMaterials;
    private TextInputEditText textInputEditText;
    private Warehouse warehouse;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ret_request);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ArrayList<Warehouse> warehouses = findAllWarehousesByZsumi(RetRequestActivity.this);
        Spinner spinnerWarehouses = findViewById(R.id.spinnerWarehouses);
        if(!warehouses.isEmpty()) {
            warehouse = new Warehouse();
            warehouse.setStgeLoc("SELC");
            warehouse.setLgobe(getString(R.string.select_warehouse));
            warehouses.add(0, warehouse);
            spinnerWarehouses.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, warehouses));
            spinnerWarehouses.setOnTouchListener((v, event) -> {
                hideKeyboard(RetRequestActivity.this);
                return false;
            }) ;

            spinnerWarehouses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                    // TODO Auto-generated method stub
                    warehouse = (Warehouse) spinnerWarehouses.getItemAtPosition(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                }
            });
        }

        recyclerViewMaterials = findViewById(R.id.recyclerViewMaterials);
        recyclerViewMaterials.setHasFixedSize(true);
        recyclerViewMaterials.setLayoutManager(new LinearLayoutManager(RetRequestActivity.this));
        ExtReqMatRecyclerViewAdapter adapter = new ExtReqMatRecyclerViewAdapter(RetRequestActivity.this, UtilsClass.materialsToProcess);
        recyclerViewMaterials.setAdapter(adapter);
        recyclerViewMaterials.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        if (item.getItemId() == R.id.action_save){
            textInputEditText = findViewById(R.id.textInputEditText);
            if (textInputEditText.getText().toString().isEmpty())
                Toast.makeText(RetRequestActivity.this, R.string.request_text_message, Toast.LENGTH_SHORT).show();
            else if (warehouse.getStgeLoc().equalsIgnoreCase("SELC"))
                Toast.makeText(RetRequestActivity.this, R.string.warehouse_message, Toast.LENGTH_SHORT).show();
            else {
                boolean error = false;
                recyclerViewMaterials = findViewById(R.id.recyclerViewMaterials);
                ExtReqMatRecyclerViewAdapter adapter = (ExtReqMatRecyclerViewAdapter) recyclerViewMaterials.getAdapter();

                for (int i = 0; i < adapter.getItemCount(); i++) {
                    if (adapter.getItem(i).getREQ_QNT() == 0){
                        Toast.makeText(RetRequestActivity.this, R.string.req_qnt_message, Toast.LENGTH_SHORT).show();
                        error = true;
                        break;
                    }
                    else if(adapter.getItem(i).getREQ_QNT() > adapter.getItem(i).getLABST()){
                        Toast.makeText(RetRequestActivity.this, getString(R.string.req_qnt_exceeded1) +" "+ adapter.getItem(i).getMAKTX() +" "+ getString(R.string.req_qnt_exceeded2) +" "+ adapter.getItem(i).getLABST(), Toast.LENGTH_SHORT).show();
                        error = true;
                        break;
                    }
                }

                if (!error){
                    new CreateRetRequestAsyncTask(UtilsClass.materialsToProcess, textInputEditText.getText().toString(), warehouse.getStgeLoc(), RetRequestActivity.this).execute();
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }
}