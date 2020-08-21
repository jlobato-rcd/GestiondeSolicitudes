package com.rcdhotels.gestiondesolicitudes.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.rcdhotels.gestiondesolicitudes.R;
import com.rcdhotels.gestiondesolicitudes.adapters.ReqDetailsRecyclerViewAdapter;
import com.rcdhotels.gestiondesolicitudes.model.Material;
import com.rcdhotels.gestiondesolicitudes.model.UtilsClass;
import com.rcdhotels.gestiondesolicitudes.model.Warehouse;
import com.rcdhotels.gestiondesolicitudes.task.AuthorizeOrRejectRequestAsyncTask;
import com.rcdhotels.gestiondesolicitudes.task.ProcessRequestAsyncTask;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

import static com.rcdhotels.gestiondesolicitudes.database.WarehouseTableQuerys.findWarehouseById;

public class ReqDetailsActivity extends AppCompatActivity {

    private ArrayList<Material> materials;
    private RecyclerView recyclerViewMaterials;
    private ReqDetailsRecyclerViewAdapter adapter;
    private String reqUser;
    private static DecimalFormat df = new DecimalFormat("0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_req_details);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle(getString(R.string.request) + " NO. " + UtilsClass.currentRequest.getIDREQUEST());
        df.setRoundingMode(RoundingMode.UP);
        TextView textViewReleased = findViewById(R.id.textViewReleased);
        TextInputEditText textInputEditText = findViewById(R.id.textInputEditText);
        textInputEditText.setText(UtilsClass.currentRequest.getHEADER_TXT());
        TextView textViewDate = findViewById(R.id.textViewDate);
        Warehouse warehouseFrom = findWarehouseById(UtilsClass.currentRequest.getSTGE_LOC(), ReqDetailsActivity.this);
        Warehouse warehouseTo = findWarehouseById(UtilsClass.currentRequest.getMOVE_STLOC(), ReqDetailsActivity.this);
        textViewDate.setText(getString(R.string.date) + UtilsClass.currentRequest.getCREATED_DATE().substring(0, 10));
        TextView textViewFromTo = findViewById(R.id.textViewFromTo);
        textViewFromTo.setText(getString(R.string.from) + warehouseFrom.getLgobe() + getString(R.string.to) + warehouseTo.getLgobe());
        TextView textViewTotalVerpr = findViewById(R.id.textViewTotalVerpr);
        textViewTotalVerpr.setText(getString(R.string.total) + "$" + df.format(UtilsClass.currentRequest.getTOTAL_VERPR()));
        recyclerViewMaterials = findViewById(R.id.recyclerViewMaterials);
        recyclerViewMaterials.setHasFixedSize(true);
        recyclerViewMaterials.setLayoutManager(new LinearLayoutManager(ReqDetailsActivity.this));

        if (UtilsClass.user.getRole().equalsIgnoreCase("GS_AUTOR1") && UtilsClass.currentRequest.getRELEASE() == 1){
            textViewReleased.setText(getString(R.string.authorized));
            textViewReleased.setVisibility(View.VISIBLE);
            UtilsClass.enableMenu = false;
        }
        else if (UtilsClass.user.getRole().equalsIgnoreCase("GS_AUTOR1") && UtilsClass.currentRequest.getRELEASE() == 0)
            UtilsClass.enableMenu = true;

        if (UtilsClass.user.getRole().equalsIgnoreCase("GS_AUTOR2") && UtilsClass.currentRequest.getRELEASE() == 2){
            textViewReleased.setText(getString(R.string.authorized));
            textViewReleased.setVisibility(View.VISIBLE);
            UtilsClass.enableMenu = false;
        }
        else if (UtilsClass.user.getRole().equalsIgnoreCase("GS_AUTOR2") && UtilsClass.currentRequest.getRELEASE() == 1)
            UtilsClass.enableMenu = true;

        if (UtilsClass.user.getRole().equalsIgnoreCase("GS_AUTOR3") && UtilsClass.currentRequest.getRELEASE() == 3){
            textViewReleased.setText(getString(R.string.authorized));
            textViewReleased.setVisibility(View.VISIBLE);
            UtilsClass.enableMenu = false;
        }
        else if (UtilsClass.user.getRole().equalsIgnoreCase("GS_AUTOR3") && UtilsClass.currentRequest.getRELEASE() == 2)
            UtilsClass.enableMenu = true;

        if (UtilsClass.user.getRole().equalsIgnoreCase("GS_PROCE") && UtilsClass.currentRequest.getSTATUS() == 7){
            textViewReleased.setText(getString(R.string.processed));
            textViewReleased.setVisibility(View.VISIBLE);
            UtilsClass.enableMenu = false;
        }
        else if (UtilsClass.user.getRole().equalsIgnoreCase("GS_PROCE") && UtilsClass.currentRequest.getSTATUS() != 7)
            UtilsClass.enableMenu = true;

        if (UtilsClass.user.getRole().equalsIgnoreCase("GS_PROCE")){
            materials = new ArrayList<>();
            for (int i = 0; i < UtilsClass.currentRequest.getMaterials().size(); i++) {
                if (UtilsClass.currentRequest.getMaterials().get(i).getPROCESSED() == 0){
                    materials.add(UtilsClass.currentRequest.getMaterials().get(i));
                }
            }
            adapter = new ReqDetailsRecyclerViewAdapter(ReqDetailsActivity.this, materials);
        }
        else{
            adapter = new ReqDetailsRecyclerViewAdapter(ReqDetailsActivity.this, UtilsClass.currentRequest.getMaterials());
        }
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
        if (UtilsClass.user.getRole().equalsIgnoreCase("GS_AUTOR1") && UtilsClass.currentRequest.getRELEASE() == 0)
            getMenuInflater().inflate(R.menu.menu_details_autor, menu);
        else if (UtilsClass.user.getRole().equalsIgnoreCase("GS_AUTOR2") && UtilsClass.currentRequest.getRELEASE() == 1 && UtilsClass.currentRequest.getTYPE() == 1)
            getMenuInflater().inflate(R.menu.menu_details_autor, menu);
        else if (UtilsClass.user.getRole().equalsIgnoreCase("GS_AUTOR3") && UtilsClass.currentRequest.getRELEASE() == 2 && UtilsClass.currentRequest.getTYPE() == 1)
            getMenuInflater().inflate(R.menu.menu_details_autor, menu);
        else if (UtilsClass.user.getRole().equalsIgnoreCase("GS_PROCE") && UtilsClass.currentRequest.getSTATUS() == 3 || UtilsClass.currentRequest.getSTATUS() == 5 || UtilsClass.currentRequest.getSTATUS() == 6)
            getMenuInflater().inflate(R.menu.menu_details_process, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        if (item.getItemId() == R.id.action_release){
            TextView textViewTotalVerpr = findViewById(R.id.textViewTotalVerpr);
            String total = textViewTotalVerpr.getText().toString().replace(getString(R.string.total), ""). replace("$", "");
            new AuthorizeOrRejectRequestAsyncTask(UtilsClass.currentRequest.getIDREQUEST(), reqUser, Float.parseFloat(total), true, "", ReqDetailsActivity.this).execute();
        }
        if (item.getItemId() == R.id.action_reject){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(ReqDetailsActivity.this);
            alertDialog.setTitle(R.string.rejection_text);
            final EditText input = new EditText(ReqDetailsActivity.this);
            input.setSingleLine();
            FrameLayout container = new FrameLayout(ReqDetailsActivity.this);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(16, 0, 16, 0);
            input.setLayoutParams(params);
            container.addView(input);
            alertDialog.setView(container);
            alertDialog.setPositiveButton(getString(R.string.accept),
                    (dialog, which) -> {
                        new AuthorizeOrRejectRequestAsyncTask(UtilsClass.currentRequest.getIDREQUEST(), reqUser, 0, false, input.getText().toString(), ReqDetailsActivity.this).execute();
                    });
            alertDialog.setNegativeButton(getString(R.string.cancel),
                    (dialog, which) -> dialog.cancel());
            alertDialog.show();
        }
        if (item.getItemId() == R.id.action_process){
            recyclerViewMaterials = findViewById(R.id.recyclerViewMaterials);
            adapter = (ReqDetailsRecyclerViewAdapter) recyclerViewMaterials.getAdapter();
            for (int i = 0; i < adapter.getItemCount(); i++) {
                Material material = adapter.getItem(i);
                for (int j = 0; j < UtilsClass.currentRequest.getMaterials().size(); j++) {
                    if (UtilsClass.currentRequest.getMaterials().get(j).getMATERIAL() == material.getMATERIAL()){
                        UtilsClass.currentRequest.getMaterials().set(j, material);
                    }
                }
            }
            new ProcessRequestAsyncTask(UtilsClass.currentRequest.getMaterials(), ReqDetailsActivity.this).execute();
        }
        return super.onOptionsItemSelected(item);
    }
}