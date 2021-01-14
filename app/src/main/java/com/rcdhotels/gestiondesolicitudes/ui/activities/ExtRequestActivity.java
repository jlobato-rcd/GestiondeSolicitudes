package com.rcdhotels.gestiondesolicitudes.ui.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.rcdhotels.gestiondesolicitudes.R;
import com.rcdhotels.gestiondesolicitudes.adapters.ReqMatRecyclerViewAdapter;
import com.rcdhotels.gestiondesolicitudes.model.Material;
import com.rcdhotels.gestiondesolicitudes.model.UtilsClass;
import com.rcdhotels.gestiondesolicitudes.model.Warehouse;
import com.rcdhotels.gestiondesolicitudes.task.CreateExtRequestAsyncTask;
import com.rcdhotels.gestiondesolicitudes.utils.Tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.rcdhotels.gestiondesolicitudes.database.WarehouseTableQuerys.findWarehouseById;

public class ExtRequestActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMaterials;
    private TextInputEditText textInputEditText;
    private ReqMatRecyclerViewAdapter adapter;
    private ActionModeCallback actionModeCallback;
    private ActionMode actionMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ext_request);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        textInputEditText = findViewById(R.id.textInputEditText);
        if((UtilsClass.currentRequest.getHEADER_TXT() != null) &&(!UtilsClass.currentRequest.getHEADER_TXT().isEmpty()))
            textInputEditText.setText(UtilsClass.currentRequest.getHEADER_TXT().toString());

        recyclerViewMaterials = findViewById(R.id.recyclerViewMaterials);
        recyclerViewMaterials.setHasFixedSize(true);
        recyclerViewMaterials.setLayoutManager(new LinearLayoutManager(ExtRequestActivity.this));
        adapter = new ReqMatRecyclerViewAdapter(ExtRequestActivity.this, UtilsClass.currentRequest.getMaterials());
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
            Tools.setSystemBarColor(ExtRequestActivity.this, R.color.colorPrimaryDark);
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
                    adapter = new ReqMatRecyclerViewAdapter(ExtRequestActivity.this, UtilsClass.currentRequest.getMaterials());
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
            Tools.setSystemBarColor(ExtRequestActivity.this, R.color.colorPrimaryDark);
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
        textInputEditText = findViewById(R.id.textInputEditText);
        UtilsClass.currentRequest.setHEADER_TXT(textInputEditText.getText().toString());
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
                Toast.makeText(ExtRequestActivity.this, R.string.request_text_message, Toast.LENGTH_SHORT).show();
            else {
                boolean error = false;
                recyclerViewMaterials = findViewById(R.id.recyclerViewMaterials);
                ReqMatRecyclerViewAdapter adapter = (ReqMatRecyclerViewAdapter) recyclerViewMaterials.getAdapter();
                Warehouse warehouse = findWarehouseById(adapter.getItem(0).getSTGE_LOC(), ExtRequestActivity.this);

                if (warehouse.getStock0() == null || warehouse.getStock0().isEmpty()){
                    for (int i = 0; i < adapter.getItemCount(); i++) {
                        if (adapter.getItem(i).getLABST() == 0){
                            Toast.makeText(ExtRequestActivity.this, R.string.stock_message, Toast.LENGTH_SHORT).show();
                            error = true;
                            break;
                        }
                    }
                }
                else{
                    for (int i = 0; i < adapter.getItemCount(); i++) {
                        if (adapter.getItem(i).getREQ_QNT() == 0){
                            Toast.makeText(ExtRequestActivity.this, R.string.req_qnt_message, Toast.LENGTH_SHORT).show();
                            error = true;
                            break;
                        }
                    }
                }
                if (!error){
                    new CreateExtRequestAsyncTask(textInputEditText.getText().toString(), ExtRequestActivity.this).execute();
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }
}