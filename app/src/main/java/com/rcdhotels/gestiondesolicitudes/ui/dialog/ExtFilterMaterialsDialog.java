package com.rcdhotels.gestiondesolicitudes.ui.dialog;

import androidx.annotation.NonNull;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.rcdhotels.gestiondesolicitudes.R;
import com.rcdhotels.gestiondesolicitudes.model.MaterialType;
import com.rcdhotels.gestiondesolicitudes.model.Request;
import com.rcdhotels.gestiondesolicitudes.model.UtilsClass;
import com.rcdhotels.gestiondesolicitudes.model.Warehouse;
import com.rcdhotels.gestiondesolicitudes.ui.activities.ExtMaterialSelectionActivity;

import java.util.ArrayList;

import static com.rcdhotels.gestiondesolicitudes.database.WarehouseTableQuerys.findAllWarehousesByStgeLocType;
import static com.rcdhotels.gestiondesolicitudes.model.UtilsClass.user;

public class ExtFilterMaterialsDialog extends Dialog {

    private Context context;
    private ArrayList<Warehouse> warehouses;
    private Warehouse warehouseSelected;
    private MaterialType materialTypeSelected;
    private Spinner spinnerCategory;
    private Spinner spinnerWarehouses;
    private Spinner spinnerMaterialTypes;
    public ExtFilterMaterialsDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_ext_filter_materials);

        String[] category = context.getResources().getStringArray(R.array.category);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerWarehouses = findViewById(R.id.spinnerWarehouses);
        spinnerMaterialTypes = findViewById(R.id.spinnerMaterialType);
        spinnerCategory.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, category));
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                if (position == 0) {
                    Toast.makeText(context, R.string.category_messge, Toast.LENGTH_SHORT).show();
                    spinnerWarehouses.setEnabled(false);
                }
                else {
                    warehouses = new ArrayList<>();
                    Warehouse warehouse = new Warehouse();
                    warehouse.setStgeLoc("SELECT");
                    warehouse.setLgobe(context.getString(R.string.select));
                    warehouses.add(warehouse);
                    warehouses.addAll(findAllWarehousesByStgeLocType(position, context));
                    spinnerWarehouses.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, warehouses));
                    spinnerWarehouses.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        Warehouse select = new Warehouse();
        select.setStgeLoc("SELECT");
        select.setLgobe(context.getString(R.string.select));
        warehouses = new ArrayList<>();
        warehouses.add(select);
        spinnerWarehouses.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, warehouses));

        spinnerWarehouses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                if (position != 0) {
                    warehouseSelected = (Warehouse) spinnerWarehouses.getSelectedItem();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        ArrayList<MaterialType> materialTypes = new ArrayList<>();
        materialTypes.add(new MaterialType("SELECT",context.getString(R.string.select)));
        materialTypes.add(new MaterialType("ZALI",context.getString(R.string.foods)));
        materialTypes.add(new MaterialType("ZBEB",context.getString(R.string.drinks)));
        materialTypes.add(new MaterialType("ZCOR",context.getString(R.string.cuts)));
        materialTypes.add(new MaterialType("ZEQO",context.getString(R.string.operation_team)));
        materialTypes.add(new MaterialType("ZMTO",context.getString(R.string.maintenance)));
        materialTypes.add(new MaterialType("ZPAP",context.getString(R.string.stationery)));
        materialTypes.add(new MaterialType("ZSUM",context.getString(R.string.supplies)));
        materialTypes.add(new MaterialType("ZTDA",context.getString(R.string.shops)));

        spinnerMaterialTypes.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, materialTypes));
        spinnerMaterialTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                if (position != 0) {
                    materialTypeSelected = (MaterialType) spinnerMaterialTypes.getSelectedItem();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        Button buttonFilter = findViewById(R.id.buttonFilter);
        buttonFilter.setOnClickListener(v -> {
            if (spinnerCategory.getSelectedItemPosition() == 0){
                Toast.makeText(context, R.string.category_messge, Toast.LENGTH_SHORT).show();
            }
            else if (spinnerWarehouses.getSelectedItemPosition() == 0){
                Toast.makeText(context, R.string.warehouse_message, Toast.LENGTH_SHORT).show();
            }
            else if (spinnerMaterialTypes.getSelectedItemPosition() == 0){
                Toast.makeText(context, R.string.material_type_message, Toast.LENGTH_SHORT).show();
            }
            else{
                UtilsClass.currentRequest = new Request();
                UtilsClass.currentRequest.setMAT_TYPE(materialTypeSelected.getId());
                UtilsClass.currentRequest.setTYPE(1);
                UtilsClass.currentRequest.setSTATUS(1);
                UtilsClass.currentRequest.setREQ_USER(user.getUserName());
                UtilsClass.currentRequest.setPLANT(user.getHotel().getIdSociety());
                UtilsClass.currentRequest.setMOVE_STLOC(user.getWarehouse());
                UtilsClass.currentRequest.setSTGE_LOC(warehouseSelected.getStgeLoc());
                UtilsClass.currentRequest.setMaterials(new ArrayList<>());
                context.startActivity(new Intent(getContext(), ExtMaterialSelectionActivity.class).putExtra("WAREHOUSE", warehouseSelected.getStgeLoc()).putExtra("MATTYPE", materialTypeSelected.getId()));
                ((Activity)context).overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                dismiss();
            }
        });
    }
}