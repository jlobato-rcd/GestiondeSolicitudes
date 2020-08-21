package com.rcdhotels.gestiondesolicitudes.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.rcdhotels.gestiondesolicitudes.R;
import com.rcdhotels.gestiondesolicitudes.adapters.ConfExtReqMatRecyclerViewAdapter;
import com.rcdhotels.gestiondesolicitudes.adapters.UpdExtReqMatRecyclerViewAdapter;
import com.rcdhotels.gestiondesolicitudes.model.Material;
import com.rcdhotels.gestiondesolicitudes.model.UtilsClass;
import com.rcdhotels.gestiondesolicitudes.task.ConfirmRequestMaterialsAsyncTask;
import com.rcdhotels.gestiondesolicitudes.task.GetExtResquestMaterialsAsyncTask;
import com.rcdhotels.gestiondesolicitudes.task.UpdateRequestMaterialsAsyncTask;

import java.util.ArrayList;
import java.util.Objects;

public class ExtConfirmMaterialsActivity extends AppCompatActivity {

    private ArrayList<Material> materials;
    private RecyclerView recyclerViewMaterials;
    private ConfExtReqMatRecyclerViewAdapter adapter;
    private CheckBox checkBoxCheckAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ext_confirm_materials);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle(getString(R.string.request) + " NO. " + UtilsClass.currentRequest.getIDREQUEST());

        new GetExtResquestMaterialsAsyncTask(UtilsClass.currentRequest.getIDREQUEST(), "ConfirmMaterials", ExtConfirmMaterialsActivity.this).execute();

        checkBoxCheckAll = findViewById(R.id.checkBoxCheckAll);
        checkBoxCheckAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
            recyclerViewMaterials = findViewById(R.id.recyclerViewMaterials);
            adapter = (ConfExtReqMatRecyclerViewAdapter) recyclerViewMaterials.getAdapter();
            if (isChecked){
                for (int i = 0; i < adapter.getItemCount(); i++) {
                    View view = recyclerViewMaterials.getChildAt(i);
                    CheckBox checkBox = view.findViewById(R.id.checkBox);
                    checkBox.setChecked(true);
                }
            }
            else {
                for (int i = 0; i < adapter.getItemCount(); i++) {
                    View view = recyclerViewMaterials.getChildAt(i);
                    CheckBox checkBox = view.findViewById(R.id.checkBox);
                    checkBox.setChecked(false);
                }
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
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }

        if (item.getItemId() == R.id.action_save){
            recyclerViewMaterials = findViewById(R.id.recyclerViewMaterials);
            adapter = (ConfExtReqMatRecyclerViewAdapter) recyclerViewMaterials.getAdapter();
            materials = new ArrayList<>();
            for (int i = 0; i < adapter.getItemCount(); i++) {
                materials.add(adapter.getItem(i));
            }
            new ConfirmRequestMaterialsAsyncTask(materials, ExtConfirmMaterialsActivity.this).execute();
        }
        return super.onOptionsItemSelected(item);
    }
}