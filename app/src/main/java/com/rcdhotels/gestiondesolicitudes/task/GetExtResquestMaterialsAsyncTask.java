package com.rcdhotels.gestiondesolicitudes.task;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rcdhotels.gestiondesolicitudes.R;
import com.rcdhotels.gestiondesolicitudes.adapters.ConfExtReqMatRecyclerViewAdapter;
import com.rcdhotels.gestiondesolicitudes.adapters.ReqAgainMatRecyclerViewAdapter;
import com.rcdhotels.gestiondesolicitudes.adapters.UpdExtReqMatRecyclerViewAdapter;
import com.rcdhotels.gestiondesolicitudes.model.Material;

import java.util.ArrayList;

import static com.rcdhotels.gestiondesolicitudes.services.XSJSServices.getResquestMaterialList;

public class GetExtResquestMaterialsAsyncTask extends AsyncTask<Void, Void, Void> {

    @SuppressLint("StaticFieldLeak")
    private Context context;
    private ArrayList<Material> items = new ArrayList<>();
    @SuppressLint("StaticFieldLeak")
    private ProgressDialog dialog;
    @SuppressLint("StaticFieldLeak")
    private RecyclerView recyclerViewMaterials;
    private int idRequest;
    private String process;

    public GetExtResquestMaterialsAsyncTask(int idRequest, String process, Context context) {
        this.idRequest = idRequest;
        this.process = process;
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
        items = getResquestMaterialList(idRequest);
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
            switch (process){
                case "RequestAgain":
                    ReqAgainMatRecyclerViewAdapter adapterRequestAgain = new ReqAgainMatRecyclerViewAdapter((Activity) context, items);
                    recyclerViewMaterials.setAdapter(adapterRequestAgain);
                    break;
                case "UpdateRequest":
                    UpdExtReqMatRecyclerViewAdapter adapterUpdateRequest = new UpdExtReqMatRecyclerViewAdapter((Activity) context, items);
                    recyclerViewMaterials.setAdapter(adapterUpdateRequest);
                    break;
                case "ConfirmMaterials":
                    ArrayList<Material> materials = new ArrayList<>();
                    for (int i = 0; i < items.size(); i++) {
                        if (items.get(i).getSTATUS_CONF() == 0){
                            materials.add(items.get(i));
                        }
                    }
                    ConfExtReqMatRecyclerViewAdapter adapterConfirmMaterials = new ConfExtReqMatRecyclerViewAdapter((Activity) context, materials);
                    recyclerViewMaterials.setAdapter(adapterConfirmMaterials);

                    CheckBox checkBoxCheckAll = ((Activity)context).findViewById(R.id.checkBoxCheckAll);
                    checkBoxCheckAll.setVisibility(View.VISIBLE);
                    break;
            }
            recyclerViewMaterials.setVisibility(View.VISIBLE);
        }
    }
}
