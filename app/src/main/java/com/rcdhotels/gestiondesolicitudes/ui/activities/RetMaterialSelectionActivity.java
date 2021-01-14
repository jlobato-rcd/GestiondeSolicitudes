package com.rcdhotels.gestiondesolicitudes.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.rcdhotels.gestiondesolicitudes.R;
import com.rcdhotels.gestiondesolicitudes.adapters.MatSelRecyclerViewAdapter;
import com.rcdhotels.gestiondesolicitudes.model.UtilsClass;
import com.rcdhotels.gestiondesolicitudes.task.GetRetMaterialsToSelectAsyncTask;

public class RetMaterialSelectionActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMaterials;
    private MatSelRecyclerViewAdapter matSelRecyclerViewAdapter;
    private int LAUNCH_MATERIALS_ACTIVITY = 1;
    private Button buttonGoToRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ret_material_selection);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        new GetRetMaterialsToSelectAsyncTask(getIntent().getStringExtra("MATTYPE"), RetMaterialSelectionActivity.this).execute();

        buttonGoToRequest = findViewById(R.id.buttonGoToRequest);
        buttonGoToRequest.setOnClickListener(v -> {
            if (UtilsClass.currentRequest.getMaterials() != null && !UtilsClass.currentRequest.getMaterials().isEmpty()){
                startActivityForResult(new Intent(this, RetRequestActivity.class), LAUNCH_MATERIALS_ACTIVITY);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
            else{
                Toast.makeText(RetMaterialSelectionActivity.this, R.string.select_materials, Toast.LENGTH_SHORT).show();
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

        getMenuInflater().inflate(R.menu.menu_ret_mat_sel, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        recyclerViewMaterials = findViewById(R.id.recyclerViewMaterials);
        matSelRecyclerViewAdapter = (MatSelRecyclerViewAdapter) recyclerViewMaterials.getAdapter();
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
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