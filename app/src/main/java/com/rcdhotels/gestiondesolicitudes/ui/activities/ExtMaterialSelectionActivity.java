package com.rcdhotels.gestiondesolicitudes.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.rcdhotels.gestiondesolicitudes.R;
import com.rcdhotels.gestiondesolicitudes.adapters.MatSelRecyclerViewAdapter;
import com.rcdhotels.gestiondesolicitudes.model.Material;
import com.rcdhotels.gestiondesolicitudes.model.UtilsClass;
import com.rcdhotels.gestiondesolicitudes.model.Warehouse;
import com.rcdhotels.gestiondesolicitudes.task.GetExtMaterialsToSelectAsyncTask;
import com.rcdhotels.gestiondesolicitudes.utils.Tools;

import java.util.ArrayList;
import java.util.Objects;

import static com.rcdhotels.gestiondesolicitudes.database.WarehouseTableQuerys.findAllWarehouses;

public class ExtMaterialSelectionActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMaterials;
    private MatSelRecyclerViewAdapter matSelRecyclerViewAdapter;
    private String StgeLoc = "";
    private boolean stock0 = true;
    private int LAUNCH_MATERIALS_ACTIVITY = 1;
    private Button buttonGoToRequest;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ext_materials_selection);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        new GetExtMaterialsToSelectAsyncTask(getIntent().getStringExtra("WAREHOUSE"), getIntent().getStringExtra("MATTYPE"), ExtMaterialSelectionActivity.this).execute();

        buttonGoToRequest = findViewById(R.id.buttonGoToRequest);
        buttonGoToRequest.setOnClickListener(v -> {
            if (UtilsClass.currentRequest.getMaterials() != null && !UtilsClass.currentRequest.getMaterials().isEmpty()){
                startActivityForResult(new Intent(ExtMaterialSelectionActivity.this, ExtRequestActivity.class), LAUNCH_MATERIALS_ACTIVITY);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
            else{
                Toast.makeText(ExtMaterialSelectionActivity.this, R.string.select_materials, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {

        getMenuInflater().inflate(R.menu.menu_ext_mat_sel, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        recyclerViewMaterials = findViewById(R.id.recyclerViewMaterials);
        matSelRecyclerViewAdapter = (MatSelRecyclerViewAdapter) recyclerViewMaterials.getAdapter();
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (recyclerViewMaterials == null)
                    recyclerViewMaterials = findViewById(R.id.recyclerViewMaterials);
                else if (matSelRecyclerViewAdapter == null)
                    matSelRecyclerViewAdapter = (MatSelRecyclerViewAdapter) recyclerViewMaterials.getAdapter();
                else {
                    matSelRecyclerViewAdapter.getFilter().filter(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (recyclerViewMaterials == null)
                    recyclerViewMaterials = findViewById(R.id.recyclerViewMaterials);
                else if (matSelRecyclerViewAdapter == null)
                    matSelRecyclerViewAdapter = (MatSelRecyclerViewAdapter) recyclerViewMaterials.getAdapter();
                else {
                    matSelRecyclerViewAdapter.getFilter().filter(newText);
                }
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        recyclerViewMaterials = findViewById(R.id.recyclerViewMaterials);
        matSelRecyclerViewAdapter = (MatSelRecyclerViewAdapter) recyclerViewMaterials.getAdapter();

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        if (item.getItemId() == R.id.action_stock_empty){
            if (!searchView.isIconified()) {
                searchView.setQuery("", true);
                searchView.setIconified(true);
                Tools.hideKeyboard(ExtMaterialSelectionActivity.this);
            }
            if (item.isChecked()){
                item.setChecked(false);
                stock0 = false;
            }
            else{
                item.setChecked(true);
                stock0 = true;
            }
            ArrayList<Material> temp = new ArrayList<>();
            for (int i = 0; i < UtilsClass.materialsArrayList.size(); i++) {
                if (stock0) {
                    if (!StgeLoc.equalsIgnoreCase("")) {
                        if (UtilsClass.materialsArrayList.get(i).getSTGE_LOC().equalsIgnoreCase(StgeLoc)) {
                            temp.add(UtilsClass.materialsArrayList.get(i));
                        }
                    } else
                        temp.add(UtilsClass.materialsArrayList.get(i));
                }
                else {
                    if (UtilsClass.materialsArrayList.get(i).getLABST() > 0) {
                        if (!StgeLoc.equalsIgnoreCase("")) {
                            if (UtilsClass.materialsArrayList.get(i).getSTGE_LOC().equalsIgnoreCase(StgeLoc)) {
                                temp.add(UtilsClass.materialsArrayList.get(i));
                            }
                        } else
                            temp.add(UtilsClass.materialsArrayList.get(i));
                    }
                }
            }
            matSelRecyclerViewAdapter = new MatSelRecyclerViewAdapter(ExtMaterialSelectionActivity.this, temp);
            recyclerViewMaterials.setHasFixedSize(true);
            recyclerViewMaterials.setLayoutManager(new LinearLayoutManager(ExtMaterialSelectionActivity.this));
            recyclerViewMaterials.setAdapter(matSelRecyclerViewAdapter);
            recyclerViewMaterials.setVisibility(View.VISIBLE);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_MATERIALS_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK){
                onBackPressed();
            }
        }
    }
}