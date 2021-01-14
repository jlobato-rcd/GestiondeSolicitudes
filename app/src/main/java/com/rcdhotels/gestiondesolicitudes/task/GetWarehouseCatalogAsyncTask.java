package com.rcdhotels.gestiondesolicitudes.task;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.rcdhotels.gestiondesolicitudes.R;
import com.rcdhotels.gestiondesolicitudes.main.MainActivity;
import com.rcdhotels.gestiondesolicitudes.model.Warehouse;

import java.util.ArrayList;
import java.util.Collections;

import static com.rcdhotels.gestiondesolicitudes.database.WarehouseTableQuerys.SQL_DELETE_ALL_WAREHOUSES;
import static com.rcdhotels.gestiondesolicitudes.database.WarehouseTableQuerys.deleteAllWarehouses;
import static com.rcdhotels.gestiondesolicitudes.database.WarehouseTableQuerys.findWarehouseById;
import static com.rcdhotels.gestiondesolicitudes.database.WarehouseTableQuerys.insertWarehouses;
import static com.rcdhotels.gestiondesolicitudes.model.UtilsClass.user;
import static com.rcdhotels.gestiondesolicitudes.services.WebServices.GetWarehouseCatalog;

public class GetWarehouseCatalogAsyncTask extends AsyncTask<Void, Void, Void> {

    @SuppressLint("StaticFieldLeak")
    private Context context;
    private ArrayList<Warehouse> warehouses;

    public GetWarehouseCatalogAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        warehouses = GetWarehouseCatalog();
        if (warehouses != null){
            Collections.sort(warehouses,(p1, p2) -> p1.getLgobe().compareTo(p2.getLgobe()));
            deleteAllWarehouses(context);
            insertWarehouses(warehouses, context);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (warehouses == null) {
            Button buttonExit = ((Activity) context).findViewById(R.id.buttonExit);
            buttonExit.setVisibility(View.VISIBLE);
            ((Activity) context).runOnUiThread(() -> Toast.makeText(context, context.getResources().getString(R.string.warehouse_download_failed), Toast.LENGTH_LONG).show());
        }
        else {
            Toast.makeText(context, R.string.welcome,Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            ((Activity) context).finish();
        }
    }
}
