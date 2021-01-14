package com.rcdhotels.gestiondesolicitudes.task;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rcdhotels.gestiondesolicitudes.R;
import com.rcdhotels.gestiondesolicitudes.adapters.MatSelRecyclerViewAdapter;
import com.rcdhotels.gestiondesolicitudes.model.Material;
import com.rcdhotels.gestiondesolicitudes.model.UtilsClass;

import java.util.ArrayList;

import static com.rcdhotels.gestiondesolicitudes.services.WebServices.GetMaterialsCatalog;

public class GetExtMaterialsToSelectAsyncTask extends AsyncTask<Void, Void, Void> {

    @SuppressLint("StaticFieldLeak")
    private Context context;
    private ArrayList<Material> items;
    private String warehouse;
    private String matType;
    private ProgressDialog dialog;

    public GetExtMaterialsToSelectAsyncTask(String warehouse, String matType, Context context) {
        this.warehouse = warehouse;
        this.matType = matType;
        this.context = context;
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
        items = GetMaterialsCatalog(warehouse, matType);
        UtilsClass.materialsArrayList = items;
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (items == null)
            ((Activity)context).runOnUiThread(() -> Toast.makeText(context,  context.getResources().getString(R.string.connection_failed), Toast.LENGTH_LONG).show());
        else{
            RecyclerView recyclerViewMaterials = ((Activity) context).findViewById(R.id.recyclerViewMaterials);
            recyclerViewMaterials.setHasFixedSize(true);
            recyclerViewMaterials.setLayoutManager(new LinearLayoutManager(context));
            MatSelRecyclerViewAdapter adapter = new MatSelRecyclerViewAdapter((Activity) context, items);
            recyclerViewMaterials.setAdapter(adapter);
            recyclerViewMaterials.setVisibility(View.VISIBLE);

        }
        dialog.dismiss();
    }
}
