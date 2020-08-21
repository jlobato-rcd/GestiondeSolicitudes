package com.rcdhotels.gestiondesolicitudes.ui.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.rcdhotels.gestiondesolicitudes.R;
import com.rcdhotels.gestiondesolicitudes.adapters.ExtReqMatRecyclerViewAdapter;
import com.rcdhotels.gestiondesolicitudes.model.UtilsClass;
import com.rcdhotels.gestiondesolicitudes.model.Warehouse;
import com.rcdhotels.gestiondesolicitudes.task.CreateExtRequestAsyncTask;

import java.util.Objects;

import static com.rcdhotels.gestiondesolicitudes.database.WarehouseTableQuerys.findWarehouseById;

public class ExtRequestActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMaterials;
    private TextInputEditText textInputEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ext_request);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerViewMaterials = findViewById(R.id.recyclerViewMaterials);
        recyclerViewMaterials.setHasFixedSize(true);
        recyclerViewMaterials.setLayoutManager(new LinearLayoutManager(ExtRequestActivity.this));
        ExtReqMatRecyclerViewAdapter adapter = new ExtReqMatRecyclerViewAdapter(ExtRequestActivity.this, UtilsClass.materialsToProcess);
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
                Toast.makeText(ExtRequestActivity.this, R.string.request_text_message, Toast.LENGTH_SHORT).show();
            else {
                boolean error = false;
                recyclerViewMaterials = findViewById(R.id.recyclerViewMaterials);
                ExtReqMatRecyclerViewAdapter adapter = (ExtReqMatRecyclerViewAdapter) recyclerViewMaterials.getAdapter();
                Warehouse warehouse = findWarehouseById(adapter.getItem(0).getSTGE_LOC(), ExtRequestActivity.this);

                if (warehouse.getStock0() != null && !warehouse.getStock0().isEmpty()){
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
                    new CreateExtRequestAsyncTask(UtilsClass.materialsToProcess, textInputEditText.getText().toString(), ExtRequestActivity.this).execute();
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }
}