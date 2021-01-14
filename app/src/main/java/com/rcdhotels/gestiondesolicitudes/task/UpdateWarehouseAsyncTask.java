package com.rcdhotels.gestiondesolicitudes.task;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import com.rcdhotels.gestiondesolicitudes.R;
import com.rcdhotels.gestiondesolicitudes.model.Material;
import com.rcdhotels.gestiondesolicitudes.model.UtilsClass;
import com.rcdhotels.gestiondesolicitudes.model.Warehouse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.rcdhotels.gestiondesolicitudes.database.UserTableQuerys.UpdateUserWarehouse;
import static com.rcdhotels.gestiondesolicitudes.services.XSJSServices.updateMaterialList;
import static com.rcdhotels.gestiondesolicitudes.services.XSJSServices.updateRequest;
import static com.rcdhotels.gestiondesolicitudes.services.XSJSServices.updateWarehouse;

public class UpdateWarehouseAsyncTask extends AsyncTask<Void, Void, Void> {


    @SuppressLint("StaticFieldLeak")
    private Context context;
    private ProgressDialog dialog;
    private int updated;
    private TextView textViewWarehouse;
    private Warehouse warehouse;

    public UpdateWarehouseAsyncTask(Context context, TextView textViewWarehouse, Warehouse warehouse) {
        this.context = context;
        this.textViewWarehouse = textViewWarehouse;
        this.warehouse = warehouse;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(context);
        dialog.setMessage(context.getString(R.string.loading));
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        updated = updateWarehouse();
        if (updated > 0){
            UpdateUserWarehouse(context);
        }
        return null;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        dialog.dismiss();
        if(updated == 0){
            Toast.makeText(context, R.string.error_while_updating_data, Toast.LENGTH_SHORT).show();
        }
        else{
            textViewWarehouse.setText(warehouse.getStgeLoc() + " - " + warehouse.getLgobe());
        }
    }
}
