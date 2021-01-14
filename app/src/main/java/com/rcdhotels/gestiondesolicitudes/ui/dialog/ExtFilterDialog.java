package com.rcdhotels.gestiondesolicitudes.ui.dialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.rcdhotels.gestiondesolicitudes.R;
import com.rcdhotels.gestiondesolicitudes.model.MaterialType;
import com.rcdhotels.gestiondesolicitudes.model.Warehouse;
import com.rcdhotels.gestiondesolicitudes.task.GetExtRequestAsyncTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static com.rcdhotels.gestiondesolicitudes.database.WarehouseTableQuerys.findAllWarehouses;
import static com.rcdhotels.gestiondesolicitudes.model.UtilsClass.user;

public class ExtFilterDialog extends Dialog {

    private String dateFrom = "";
    private String dateTo = "";
    private Context context;
    private Spinner spinnerStatus;
    private ArrayList<String> statusList;
    private int status;

    private ArrayList<Warehouse> warehouses;
    private Spinner spinnerRequestingWarehouse;
    private Spinner spinnerSupplierWarehouse;
    private Warehouse requestingSelected;
    private Warehouse supplierSelected;
    private EditText editTextDateFrom;
    private EditText editTextDateTo;
    private MaterialType materialTypeSelected;
    private Spinner spinnerMaterialTypes;

    public ExtFilterDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_req_filter);

        editTextDateFrom = findViewById(R.id.editTextDateFrom);
        editTextDateTo = findViewById(R.id.editTextDateTo);
        editTextDateTo.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        editTextDateFrom.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar);
            DatePicker picker = new DatePicker(getContext());
            picker.setMaxDate(new Date().getTime());
            picker.setCalendarViewShown(false);
            builder.setView(picker);
            builder.setNegativeButton(R.string.cancel, (dialog, which) -> {
                builder.create().dismiss();
            });
            builder.setPositiveButton(R.string.menu_confirm, (dialog, which) -> {
                int day = picker.getDayOfMonth();
                String dd;
                if (day < 10)
                    dd = "0" + day;
                else
                    dd = String.valueOf(day);
                int month = picker.getMonth() + 1;
                String mm;
                if (month < 10)
                    mm = "0" + month;
                else
                    mm = String.valueOf(month);
                int year = picker.getYear();
                dateFrom = year+"-"+mm+"-"+dd;
                editTextDateFrom.setText(dateFrom);
                dateTo = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                editTextDateTo.setEnabled(true);

            });
            builder.show();
        });


        editTextDateTo.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar);
            DatePicker picker = new DatePicker(getContext());
            picker.setMaxDate(new Date().getTime());
            picker.setCalendarViewShown(false);
            builder.setView(picker);
            builder.setNegativeButton(R.string.cancel, (dialog, which) -> {
                builder.create().dismiss();
            });
            builder.setPositiveButton(R.string.menu_confirm, (dialog, which) -> {
                int day = picker.getDayOfMonth();
                String dd;
                if (day < 10)
                    dd = "0" + day;
                else
                    dd = String.valueOf(day);
                int month = picker.getMonth() + 1;
                String mm;
                if (month < 10)
                    mm = "0" + month;
                else
                    mm = String.valueOf(month);
                int year = picker.getYear();
                dateTo = year+"-"+mm+"-"+dd;
                editTextDateTo.setText(dateTo);

            });
            builder.show();
        });

        String[] array = context.getResources().getStringArray(R.array.status);
        statusList = new ArrayList<>();
        statusList.add(context.getString(R.string.all));
        statusList.addAll(Arrays.asList(array));
        spinnerStatus = findViewById(R.id.spinnerStatus);
        spinnerStatus.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, statusList));
        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                status = spinnerStatus.getSelectedItemPosition() - 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        if (user.getRole().equalsIgnoreCase("GS_REPRO")){
            spinnerStatus.setSelection(7);
            spinnerStatus.setEnabled(false);
        }

        spinnerRequestingWarehouse = findViewById(R.id.spinnerRequestingWarehouse);
        Warehouse select = new Warehouse();
        select.setStgeLoc("ALL");
        select.setLgobe(context.getString(R.string.all));
        requestingSelected = select;
        supplierSelected = select;
        warehouses = new ArrayList<>();
        warehouses.add(select);
        warehouses.addAll(findAllWarehouses(context));
        spinnerRequestingWarehouse.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, warehouses));
        spinnerRequestingWarehouse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                requestingSelected = (Warehouse) spinnerRequestingWarehouse.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        if (user.getRole().equalsIgnoreCase("GS_SOLIC1") || user.getRole().equalsIgnoreCase("GS_SOLIC2") || user.getRole().equalsIgnoreCase("GS_AUTOR1")) {
            for (int i = 0; i < warehouses.size(); i++) {
                if (warehouses.get(i).getStgeLoc().equalsIgnoreCase(user.getWarehouse())) {
                    spinnerRequestingWarehouse.setSelection(i);
                    break;
                }
            }
            spinnerRequestingWarehouse.setEnabled(false);
        }

        spinnerSupplierWarehouse = findViewById(R.id.spinnerSupplierWarehouse);
        spinnerSupplierWarehouse.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, warehouses));
        spinnerSupplierWarehouse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                supplierSelected = (Warehouse) spinnerSupplierWarehouse.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        if (user.getRole().equalsIgnoreCase("GS_PROCE")) {
            for (int i = 0; i < warehouses.size(); i++) {
                if (warehouses.get(i).getStgeLoc().equalsIgnoreCase(user.getWarehouse())) {
                    spinnerSupplierWarehouse.setSelection(i);
                    break;
                }
            }
            spinnerSupplierWarehouse.setEnabled(false);
        }

        ArrayList<MaterialType> materialTypes = new ArrayList<>();
        materialTypes.add(new MaterialType("ALL",context.getString(R.string.all)));
        materialTypes.add(new MaterialType("ZALI",context.getString(R.string.foods)));
        materialTypes.add(new MaterialType("ZBEB",context.getString(R.string.drinks)));
        materialTypes.add(new MaterialType("ZCOR",context.getString(R.string.cuts)));
        materialTypes.add(new MaterialType("ZEQO",context.getString(R.string.operation_team)));
        materialTypes.add(new MaterialType("ZMTO",context.getString(R.string.maintenance)));
        materialTypes.add(new MaterialType("ZPAP",context.getString(R.string.stationery)));
        materialTypes.add(new MaterialType("ZSUM",context.getString(R.string.supplies)));
        materialTypes.add(new MaterialType("ZTDA",context.getString(R.string.shops)));

        materialTypeSelected = new MaterialType("ALL",context.getString(R.string.all));
        spinnerMaterialTypes = findViewById(R.id.spinnerMaterialType);
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
            String stgeloc = "";
            String movestloc = "";
            String matType = "";

            if (!materialTypeSelected.getId().equalsIgnoreCase("ALL"))
                matType = materialTypeSelected.getId();

            switch (user.getRole()){
                case "GS_SOLIC1":
                case "GS_SOLIC2":
                case "GS_AUTOR1":
                    if(!requestingSelected.getStgeLoc().equalsIgnoreCase("ALL"))
                        movestloc = requestingSelected.getStgeLoc();
                    else
                        movestloc = user.getWarehouse();

                    if(!supplierSelected.getStgeLoc().equalsIgnoreCase("ALL"))
                        stgeloc = supplierSelected.getStgeLoc();

                    new GetExtRequestAsyncTask(context, dateFrom, dateTo, status, stgeloc, movestloc, matType).execute();
                    break;
                case "GS_AUTOR2":
                case "GS_AUTOR3":
                    if(!requestingSelected.getStgeLoc().equalsIgnoreCase("ALL"))
                        movestloc = requestingSelected.getStgeLoc();

                    if(!supplierSelected.getStgeLoc().equalsIgnoreCase("ALL"))
                        stgeloc = supplierSelected.getStgeLoc();

                    new GetExtRequestAsyncTask(context, dateFrom, dateTo, status, stgeloc, movestloc, matType).execute();
                    break;
                case "GS_PROCE":
                    if(!requestingSelected.getStgeLoc().equalsIgnoreCase("ALL"))
                        movestloc = requestingSelected.getStgeLoc();

                    if(!supplierSelected.getStgeLoc().equalsIgnoreCase("ALL"))
                        stgeloc = supplierSelected.getStgeLoc();
                    else
                        stgeloc = user.getWarehouse();

                    new GetExtRequestAsyncTask(context, dateFrom, dateTo, status, movestloc, stgeloc, matType).execute();
                    break;
                case "GS_REPRO":
                    if(!requestingSelected.getStgeLoc().equalsIgnoreCase("ALL"))
                        movestloc = requestingSelected.getStgeLoc();

                    if(!supplierSelected.getStgeLoc().equalsIgnoreCase("ALL"))
                        stgeloc = supplierSelected.getStgeLoc();

                    new GetExtRequestAsyncTask(context, dateFrom, dateTo, status, movestloc, stgeloc, matType).execute();
                    break;
            }
            dismiss();
        });
    }
}