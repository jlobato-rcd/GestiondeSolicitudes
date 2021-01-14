package com.rcdhotels.gestiondesolicitudes.task;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.rcdhotels.gestiondesolicitudes.R;
import com.rcdhotels.gestiondesolicitudes.adapters.MatSelRecyclerViewAdapter;
import com.rcdhotels.gestiondesolicitudes.model.Material;
import com.rcdhotels.gestiondesolicitudes.model.UtilsClass;

import java.util.ArrayList;

import static com.rcdhotels.gestiondesolicitudes.services.WebServices.GetRetMaterialsOnStock;

public class GetRetMaterialsToSelectAsyncTask extends AsyncTask<Void, Void, Void> {

    @SuppressLint("StaticFieldLeak")
    private Context context;
    private ArrayList<Material> items = new ArrayList<>();
    @SuppressLint("StaticFieldLeak")
    private SwipeRefreshLayout swipeLayout;
    @SuppressLint("StaticFieldLeak")
    private RecyclerView recyclerViewMaterials;
    private String Mtart;
    private ProgressDialog dialog;

    public GetRetMaterialsToSelectAsyncTask(String Mtart, Context context) {
        this.Mtart = Mtart;
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
        items = GetRetMaterialsOnStock(Mtart);
        UtilsClass.materialsArrayList = items;
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (items == null)
            ((Activity)context).runOnUiThread(() -> Toast.makeText(context,  context.getResources().getString(R.string.connection_failed), Toast.LENGTH_LONG).show());
        else{
            recyclerViewMaterials = ((Activity)context).findViewById(R.id.recyclerViewMaterials);
            recyclerViewMaterials.setHasFixedSize(true);
            recyclerViewMaterials.setLayoutManager(new LinearLayoutManager(context));
            MatSelRecyclerViewAdapter adapter = new MatSelRecyclerViewAdapter((Activity) context, items);
            recyclerViewMaterials.setAdapter(adapter);
            recyclerViewMaterials.setVisibility(View.VISIBLE);
        }
        dialog.dismiss();
    }
}
