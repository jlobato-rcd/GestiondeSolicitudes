package com.rcdhotels.gestiondesolicitudes.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.rcdhotels.gestiondesolicitudes.R;
import com.rcdhotels.gestiondesolicitudes.adapters.ReqDetailsRecyclerViewAdapter;
import com.rcdhotels.gestiondesolicitudes.adapters.ReqMatRecyclerViewAdapter;
import com.rcdhotels.gestiondesolicitudes.model.Material;
import com.rcdhotels.gestiondesolicitudes.model.UtilsClass;
import com.rcdhotels.gestiondesolicitudes.model.Warehouse;
import com.rcdhotels.gestiondesolicitudes.task.AuthorizeOrRejectRequestAsyncTask;
import com.rcdhotels.gestiondesolicitudes.task.ProcessRequestAsyncTask;
import com.rcdhotels.gestiondesolicitudes.task.UpdateRequestMaterialsAsyncTask;
import com.rcdhotels.gestiondesolicitudes.utils.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.rcdhotels.gestiondesolicitudes.database.WarehouseTableQuerys.findWarehouseById;

public class ReqDetailsActivity extends AppCompatActivity {

    private ArrayList<Material> materials;
    private RecyclerView recyclerViewMaterials;
    private ReqDetailsRecyclerViewAdapter adapter;
    private String reqUser;
    private static DecimalFormat df = new DecimalFormat("0.00");
    private ActionModeCallback actionModeCallback;
    private ActionMode actionMode;

    @SuppressLint("SetTextI18n")
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

        TextView textViewError = findViewById(R.id.textViewError);
        recyclerViewMaterials = findViewById(R.id.recyclerViewMaterials);
        recyclerViewMaterials.setHasFixedSize(true);
        recyclerViewMaterials.setLayoutManager(new LinearLayoutManager(ReqDetailsActivity.this));

        switch (UtilsClass.user.getRole()){
            case "GS_SOLIC1":
            case "GS_SOLIC2":
                adapter = new ReqDetailsRecyclerViewAdapter(ReqDetailsActivity.this, UtilsClass.currentRequest.getMaterials());
                UtilsClass.enableMenu = false;
                break;
            case "GS_AUTOR1":
                if (UtilsClass.currentRequest.getSTATUS() == 1 && UtilsClass.currentRequest.getRELEASE() >= 1){
                    textViewReleased.setText(getString(R.string.authorized));
                    textViewReleased.setVisibility(View.VISIBLE);
                    UtilsClass.enableMenu = false;
                }
                else if (UtilsClass.currentRequest.getSTATUS() == 1 && UtilsClass.currentRequest.getRELEASE() == 0)
                    UtilsClass.enableMenu = true;
                else if (UtilsClass.currentRequest.getSTATUS() == 3){
                    textViewReleased.setText(getString(R.string.released));
                    textViewReleased.setVisibility(View.VISIBLE);
                }
                else if (UtilsClass.currentRequest.getSTATUS() == 2){
                    textViewReleased.setText(getString(R.string.rejected));
                    textViewReleased.setTextColor(Color.parseColor("#F44336"));
                    textViewReleased.setVisibility(View.VISIBLE);
                    textViewError.setText(UtilsClass.currentRequest.getTEXT());
                    textViewError.setVisibility(View.VISIBLE);
                }
                else if (UtilsClass.currentRequest.getSTATUS() == 6){
                    textViewReleased.setText(getString(R.string.reprocessing));
                    textViewReleased.setTextColor(Color.parseColor("#03A9F4"));
                    textViewReleased.setVisibility(View.VISIBLE);
                    textViewError.setText(UtilsClass.currentRequest.getTEXT());
                    textViewError.setVisibility(View.VISIBLE);
                }
                adapter = new ReqDetailsRecyclerViewAdapter(ReqDetailsActivity.this, UtilsClass.currentRequest.getMaterials());
                break;
            case "GS_AUTOR2":
                if (UtilsClass.currentRequest.getSTATUS() == 1 && UtilsClass.currentRequest.getRELEASE() >= 2){
                    textViewReleased.setText(getString(R.string.authorized));
                    textViewReleased.setVisibility(View.VISIBLE);
                    UtilsClass.enableMenu = false;
                }
                else if (UtilsClass.currentRequest.getSTATUS() == 1 && UtilsClass.currentRequest.getRELEASE() == 1)
                    UtilsClass.enableMenu = true;
                else if (UtilsClass.currentRequest.getSTATUS() == 3){
                    textViewReleased.setText(getString(R.string.released));
                    textViewReleased.setVisibility(View.VISIBLE);
                }
                else if (UtilsClass.currentRequest.getSTATUS() == 2){
                    textViewReleased.setText(getString(R.string.released));
                    textViewReleased.setTextColor(Color.parseColor("#F44336"));
                    textViewReleased.setVisibility(View.VISIBLE);
                    textViewError.setText(UtilsClass.currentRequest.getTEXT());
                    textViewError.setVisibility(View.VISIBLE);
                }
                else if (UtilsClass.currentRequest.getSTATUS() == 6){
                    textViewReleased.setText(getString(R.string.reprocessing));
                    textViewReleased.setTextColor(Color.parseColor("#03A9F4"));
                    textViewReleased.setVisibility(View.VISIBLE);
                    textViewError.setText(UtilsClass.currentRequest.getTEXT());
                    textViewError.setVisibility(View.VISIBLE);
                }
                adapter = new ReqDetailsRecyclerViewAdapter(ReqDetailsActivity.this, UtilsClass.currentRequest.getMaterials());
                break;
            case "GS_AUTOR3":
                if (UtilsClass.currentRequest.getSTATUS() == 1 && UtilsClass.currentRequest.getRELEASE() >= 3){
                    textViewReleased.setText(getString(R.string.authorized));
                    textViewReleased.setVisibility(View.VISIBLE);
                    UtilsClass.enableMenu = false;
                }
                else if (UtilsClass.currentRequest.getRELEASE() == 2)
                    UtilsClass.enableMenu = true;
                else if (UtilsClass.currentRequest.getSTATUS() == 3){
                    textViewReleased.setText(getString(R.string.released));
                    textViewReleased.setVisibility(View.VISIBLE);
                }
                else if (UtilsClass.currentRequest.getSTATUS() == 2){
                    textViewReleased.setText(getString(R.string.rejected));
                    textViewReleased.setTextColor(Color.parseColor("#F44336"));
                    textViewReleased.setVisibility(View.VISIBLE);
                    textViewError.setText(UtilsClass.currentRequest.getTEXT());
                    textViewError.setVisibility(View.VISIBLE);
                }
                else if (UtilsClass.currentRequest.getSTATUS() == 6){
                    textViewReleased.setText(getString(R.string.reprocessing));
                    textViewReleased.setTextColor(Color.parseColor("#03A9F4"));
                    textViewReleased.setVisibility(View.VISIBLE);
                    textViewError.setText(UtilsClass.currentRequest.getTEXT());
                    textViewError.setVisibility(View.VISIBLE);
                }
                adapter = new ReqDetailsRecyclerViewAdapter(ReqDetailsActivity.this, UtilsClass.currentRequest.getMaterials());
                break;
            case "GS_PROCE":
                if (UtilsClass.currentRequest.getSTATUS() == 2){
                    textViewReleased.setText(getString(R.string.rejected));
                    textViewReleased.setTextColor(Color.parseColor("#F44336"));
                    textViewReleased.setVisibility(View.VISIBLE);
                    textViewError.setText(UtilsClass.currentRequest.getTEXT());
                    textViewError.setVisibility(View.VISIBLE);
                    adapter = new ReqDetailsRecyclerViewAdapter(ReqDetailsActivity.this, UtilsClass.currentRequest.getMaterials());
                }
                else if (UtilsClass.currentRequest.getSTATUS() == 6){
                    textViewReleased.setText(getString(R.string.reprocessing));
                    textViewReleased.setTextColor(Color.parseColor("#03A9F4"));
                    textViewReleased.setVisibility(View.VISIBLE);
                    textViewError.setText(UtilsClass.currentRequest.getTEXT());
                    textViewError.setVisibility(View.VISIBLE);
                    adapter = new ReqDetailsRecyclerViewAdapter(ReqDetailsActivity.this, UtilsClass.currentRequest.getMaterials());
                }
                if (UtilsClass.currentRequest.getSTATUS() == 7){
                    textViewReleased.setText(getString(R.string.processed));
                    textViewReleased.setVisibility(View.VISIBLE);
                    UtilsClass.enableMenu = false;
                    adapter = new ReqDetailsRecyclerViewAdapter(ReqDetailsActivity.this, UtilsClass.currentRequest.getMaterials());
                }
                else if (UtilsClass.currentRequest.getSTATUS() != 7) {
                    UtilsClass.enableMenu = true;
                    materials = new ArrayList<>();
                    for (int i = 0; i < UtilsClass.currentRequest.getMaterials().size(); i++) {
                        if (UtilsClass.currentRequest.getCONF() == 1 || UtilsClass.currentRequest.getCONF() == 2){
                            if(UtilsClass.currentRequest.getMaterials().get(i).getSTATUS_CONF() == 0 && UtilsClass.currentRequest.getMaterials().get(i).getPROCESSED() == 0) {
                                materials.add(UtilsClass.currentRequest.getMaterials().get(i));
                            }
                        }
                        else if (UtilsClass.currentRequest.getMaterials().get(i).getPROCESSED() == 0){
                            materials.add(UtilsClass.currentRequest.getMaterials().get(i));
                        }
                    }
                    adapter = new ReqDetailsRecyclerViewAdapter(ReqDetailsActivity.this, materials);
                }
                else{
                    adapter = new ReqDetailsRecyclerViewAdapter(ReqDetailsActivity.this, UtilsClass.currentRequest.getMaterials());
                }

                break;
            case "GS_REPRO":
                if (UtilsClass.currentRequest.getSTATUS() == 2){
                    textViewReleased.setText(getString(R.string.rejected));
                    textViewReleased.setTextColor(Color.parseColor("#F44336"));
                    textViewReleased.setVisibility(View.VISIBLE);
                    textViewError.setText(UtilsClass.currentRequest.getTEXT());
                    textViewError.setVisibility(View.VISIBLE);
                    adapter = new ReqDetailsRecyclerViewAdapter(ReqDetailsActivity.this, UtilsClass.currentRequest.getMaterials());
                }
                else if (UtilsClass.currentRequest.getSTATUS() == 6){
                    textViewReleased.setText(getString(R.string.reprocessing));
                    textViewReleased.setTextColor(Color.parseColor("#03A9F4"));
                    textViewReleased.setVisibility(View.VISIBLE);
                    textViewError.setText(UtilsClass.currentRequest.getTEXT());
                    textViewError.setVisibility(View.VISIBLE);
                    adapter = new ReqDetailsRecyclerViewAdapter(ReqDetailsActivity.this, UtilsClass.currentRequest.getMaterials());
                }
                else if (UtilsClass.currentRequest.getSTATUS() == 7){
                    textViewReleased.setText(getString(R.string.processed));
                    textViewReleased.setVisibility(View.VISIBLE);
                    UtilsClass.enableMenu = false;
                    adapter = new ReqDetailsRecyclerViewAdapter(ReqDetailsActivity.this, UtilsClass.currentRequest.getMaterials());
                }
                else if (UtilsClass.currentRequest.getSTATUS() == 6) {
                    UtilsClass.enableMenu = true;
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
                break;
        }

        recyclerViewMaterials.setAdapter(adapter);
        if (UtilsClass.user.getRole().contains("GS_AUTOR") && UtilsClass.currentRequest.getSTATUS() == 1 && UtilsClass.currentRequest.getMaterials().size() > 0){
            adapter.setOnClickListener(new ReqDetailsRecyclerViewAdapter.OnClickListener() {
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

    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            Tools.setSystemBarColor(ReqDetailsActivity.this, R.color.colorPrimaryDark);
            mode.getMenuInflater().inflate(R.menu.menu_req_mat_sel, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            if (item.getItemId() == R.id.action_delete) {
                    ArrayList<Material> materials = getSelectedItems();
                if (UtilsClass.currentRequest.getMaterials().size() > 1 && UtilsClass.currentRequest.getMaterials().size() > materials.size()){
                    for (int i = 0; i < materials.size(); i++) {
                        UtilsClass.currentRequest.getMaterials().remove(materials.get(i));
                        adapter = new ReqDetailsRecyclerViewAdapter(ReqDetailsActivity.this, UtilsClass.currentRequest.getMaterials());
                        recyclerViewMaterials.setAdapter(adapter);
                        if (UtilsClass.currentRequest.getMaterials().size() > 0){
                            adapter.setOnClickListener(new ReqDetailsRecyclerViewAdapter.OnClickListener() {
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
                    UtilsClass.currentRequest.setTOTAL_VERPR(0);
                    for (int i = 0; i < UtilsClass.currentRequest.getMaterials().size(); i++) {
                        if (UtilsClass.currentRequest.getMaterials().get(i).getDELETE() != 1){
                            UtilsClass.currentRequest.setTOTAL_VERPR(UtilsClass.currentRequest.getTOTAL_VERPR() + UtilsClass.currentRequest.getMaterials().get(i).getVERPR() * UtilsClass.currentRequest.getMaterials().get(i).getREQ_QNT());
                        }
                    }
                    TextView textViewTotalVerpr = findViewById(R.id.textViewTotalVerpr);
                    textViewTotalVerpr.setText(getString(R.string.total) + "$" + df.format(UtilsClass.currentRequest.getTOTAL_VERPR()));
                }
                else{
                    Toast.makeText(ReqDetailsActivity.this, R.string.materials_delete_message, Toast.LENGTH_SHORT).show();
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
            Tools.setSystemBarColor(ReqDetailsActivity.this, R.color.colorPrimaryDark);
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

    private void toggleSelection(int position, ReqDetailsRecyclerViewAdapter adapter) {
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

    @SuppressLint("SetTextI18n")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        if (item.getItemId() == R.id.action_save){
            float amount = 0;
            for (int i = 0; i < UtilsClass.currentRequest.getMaterials().size(); i++) {
                amount += UtilsClass.currentRequest.getMaterials().get(i).getVERPR() * UtilsClass.currentRequest.getMaterials().get(i).getREQ_QNT();

                TextView textViewTotalVerpr = findViewById(R.id.textViewTotalVerpr);
                textViewTotalVerpr.setText(getString(R.string.total) + "$" + df.format(amount));
            }
            new UpdateRequestMaterialsAsyncTask(UtilsClass.currentRequest.getMaterials(), true,ReqDetailsActivity.this).execute();
        }
        if (item.getItemId() == R.id.action_release){
            new AuthorizeOrRejectRequestAsyncTask(true, "", ReqDetailsActivity.this).execute();
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
                        new AuthorizeOrRejectRequestAsyncTask(false, input.getText().toString(), ReqDetailsActivity.this).execute();
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