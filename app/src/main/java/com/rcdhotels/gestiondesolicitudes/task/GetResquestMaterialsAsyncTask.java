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
import com.rcdhotels.gestiondesolicitudes.adapters.UpdExtReqMatRecyclerViewAdapter;
import com.rcdhotels.gestiondesolicitudes.model.Material;
import com.rcdhotels.gestiondesolicitudes.model.UtilsClass;

import java.util.ArrayList;

import static com.rcdhotels.gestiondesolicitudes.services.XSJSServices.getResquestMaterialList;

public class GetResquestMaterialsAsyncTask extends AsyncTask<Void, Void, Void> {

    @SuppressLint("StaticFieldLeak")
    private Context context;
    private ArrayList<Material> items = new ArrayList<>();
    @SuppressLint("StaticFieldLeak")
    private ProgressDialog dialog;
    @SuppressLint("StaticFieldLeak")
    private RecyclerView recyclerViewMaterials;
    private int idRequest;

    public GetResquestMaterialsAsyncTask(int idRequest, Context context) {
        this.idRequest = idRequest;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(context);
        dialog.setMessage(context.getString(R.string.loading));
        dialog.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        UtilsClass.materialsArrayList = null;
        items = getResquestMaterialList(idRequest);
        UtilsClass.materialsArrayList = items;
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        dialog.dismiss();
        if (items == null)
            ((Activity)context).runOnUiThread(() -> Toast.makeText(context,  context.getResources().getString(R.string.connection_failed), Toast.LENGTH_LONG).show());
        else{
            recyclerViewMaterials = ((Activity)context).findViewById(R.id.recyclerViewMaterials);
            recyclerViewMaterials.setHasFixedSize(true);
            recyclerViewMaterials.setLayoutManager(new LinearLayoutManager(context));
            UpdExtReqMatRecyclerViewAdapter adapter = new UpdExtReqMatRecyclerViewAdapter((Activity) context, items);
            recyclerViewMaterials.setAdapter(adapter);
            recyclerViewMaterials.setVisibility(View.VISIBLE);
        }
    }
}
