package com.rcdhotels.gestiondesolicitudes.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
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
import com.rcdhotels.gestiondesolicitudes.adapters.ReqMatRecyclerViewAdapter;
import com.rcdhotels.gestiondesolicitudes.model.Material;
import com.rcdhotels.gestiondesolicitudes.model.UtilsClass;
import com.rcdhotels.gestiondesolicitudes.model.Warehouse;
import com.rcdhotels.gestiondesolicitudes.task.CreateRetRequestAsyncTask;
import com.rcdhotels.gestiondesolicitudes.utils.Tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.rcdhotels.gestiondesolicitudes.database.WarehouseTableQuerys.findAllWarehousesByZsumi;
import static com.rcdhotels.gestiondesolicitudes.utils.Tools.hideKeyboard;

public class RetRequestActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMaterials;
    private TextInputEditText textInputEditText;
    private Warehouse warehouse;
    private ReqMatRecyclerViewAdapter adapter;
    private ActionModeCallback actionModeCallback;
    private ActionMode actionMode;

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
            warehouse.setStgeLoc("SELEC");
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
                    UtilsClass.currentRequest.setMOVE_STLOC(warehouse.getStgeLoc());
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
        adapter = new ReqMatRecyclerViewAdapter(RetRequestActivity.this, UtilsClass.currentRequest.getMaterials());
        recyclerViewMaterials.setAdapter(adapter);
        adapter.setOnClickListener(new ReqMatRecyclerViewAdapter.OnClickListener() {
            @Override
            public void onItemClick(View view, Material material, int pos) {
                if (adapter.getSelectedItemCount() > 0)
                    enableActionMode(pos);
            }
            @Override
            public void onItemLongClick(View view, Material material, int pos) {
                enableActionMode(pos);
            }
        });
        actionModeCallback = new ActionModeCallback();
        recyclerViewMaterials.setVisibility(View.VISIBLE);
    }

    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            Tools.setSystemBarColor(RetRequestActivity.this, R.color.colorPrimaryDark);
            mode.getMenuInflater().inflate(R.menu.menu_req_mat_sel, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            if (item.getItemId() == R.id.action_delete) {
                ArrayList<Material> materials = getSelectedItems();
                for (int i = 0; i < materials.size(); i++) {
                    UtilsClass.currentRequest.getMaterials().remove(materials.get(i));
                    adapter = new ReqMatRecyclerViewAdapter(RetRequestActivity.this, UtilsClass.currentRequest.getMaterials());
                    recyclerViewMaterials.setAdapter(adapter);
                    if (UtilsClass.currentRequest.getMaterials().size() > 0) {
                        adapter.setOnClickListener(new ReqMatRecyclerViewAdapter.OnClickListener() {
                            @Override
                            public void onItemClick(View view, Material material, int pos) {
                                if (adapter.getSelectedItemCount() > 0)
                                    enableActionMode(pos);
                            }

                            @Override
                            public void onItemLongClick(View view, Material material, int pos) {
                                enableActionMode(pos);
                            }
                        });
                        actionModeCallback = new ActionModeCallback();
                    }
                    recyclerViewMaterials.setVisibility(View.VISIBLE);
                }
                mode.finish();
                return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            adapter.clearSelections();
            actionMode = null;
            Tools.setSystemBarColor(RetRequestActivity.this, R.color.colorPrimaryDark);
        }
    }

    private ArrayList<Material> getSelectedItems() {
        List<Integer> selectedItemPositions = adapter.getSelectedItems();
        ArrayList<Material> materials = new ArrayList<>();
        for (int i = 0; i < selectedItemPositions.size(); i++) {
            materials.add(adapter.getItem(selectedItemPositions.get(i)));
        }
        return materials;
    }

    private void enableActionMode(int position) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
        }
        toggleSelection(position, adapter);
    }

    private void toggleSelection(int position, ReqMatRecyclerViewAdapter adapter) {
        adapter.toggleSelection(position);
        int count = adapter.getSelectedItemCount();
        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(count + " " + getString(R.string.selected));
            actionMode.invalidate();
        }
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
                ReqMatRecyclerViewAdapter adapter = (ReqMatRecyclerViewAdapter) recyclerViewMaterials.getAdapter();

                for (int i = 0; i < adapter.getItemCount(); i++) {
                    if (adapter.getItem(i).getREQ_QNT() == 0){
                        Toast.makeText(RetRequestActivity.this, R.string.req_qnt_message, Toast.LENGTH_SHORT).show();
                        error = true;
                        break;
                    }
                    else if(adapter.getItem(i).getREQ_QNT() > adapter.getItem(i).getLABST()){
                        Toast.makeText(RetRequestActivity.this, getString(R.string.req_qnt_exceeded1) +" "+ adapter.getItem(i).getMAKTX() +" "+ getString(R.string.req_qnt_exceeded2) +" "+ adapter.getItem(i).getLABST( ), Toast.LENGTH_SHORT).show();
                        error = true;
                        break;
                    }
                }

                if (!error){
                    new CreateRetRequestAsyncTask(textInputEditText.getText().toString(), RetRequestActivity.this).execute();
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }
}