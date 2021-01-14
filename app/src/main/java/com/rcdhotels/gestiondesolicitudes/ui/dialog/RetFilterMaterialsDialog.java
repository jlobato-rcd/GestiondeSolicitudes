package com.rcdhotels.gestiondesolicitudes.ui.dialog;

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

import androidx.annotation.NonNull;

import com.rcdhotels.gestiondesolicitudes.R;
import com.rcdhotels.gestiondesolicitudes.model.MaterialType;
import com.rcdhotels.gestiondesolicitudes.model.Request;
import com.rcdhotels.gestiondesolicitudes.model.UtilsClass;
import com.rcdhotels.gestiondesolicitudes.ui.activities.RetMaterialSelectionActivity;

import java.util.ArrayList;

import static com.rcdhotels.gestiondesolicitudes.model.UtilsClass.user;

public class RetFilterMaterialsDialog extends Dialog {

    private Context context;
    private MaterialType materialTypeSelected;
    private Spinner spinnerMaterialTypes;
    public RetFilterMaterialsDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_ret_filter_materials);


        spinnerMaterialTypes = findViewById(R.id.spinnerMaterialType);

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
            if (spinnerMaterialTypes.getSelectedItemPosition() == 0){
                Toast.makeText(context, R.string.material_type_message, Toast.LENGTH_SHORT).show();
            }
            else{
                UtilsClass.currentRequest = new Request();
                UtilsClass.currentRequest.setMAT_TYPE(materialTypeSelected.getId());
                UtilsClass.currentRequest.setTYPE(0);
                UtilsClass.currentRequest.setSTATUS(1);
                UtilsClass.currentRequest.setCONF(0);
                UtilsClass.currentRequest.setREQ_USER(user.getUserName());
                UtilsClass.currentRequest.setPLANT(user.getHotel().getIdSociety());
                UtilsClass.currentRequest.setSTGE_LOC(user.getWarehouse());
                UtilsClass.currentRequest.setMaterials(new ArrayList<>());
                context.startActivity(new Intent(getContext(), RetMaterialSelectionActivity.class).putExtra("MATTYPE", materialTypeSelected.getId()));
                ((Activity)context).overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                dismiss();
            }
        });
    }
}