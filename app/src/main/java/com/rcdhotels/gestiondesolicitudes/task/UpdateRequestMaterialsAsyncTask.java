package com.rcdhotels.gestiondesolicitudes.task;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.rcdhotels.gestiondesolicitudes.R;
import com.rcdhotels.gestiondesolicitudes.model.Material;
import com.rcdhotels.gestiondesolicitudes.model.UtilsClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.rcdhotels.gestiondesolicitudes.services.XSJSServices.getResquestMaterialList;
import static com.rcdhotels.gestiondesolicitudes.services.XSJSServices.updateMaterialList;
import static com.rcdhotels.gestiondesolicitudes.services.XSJSServices.updateRequest;

public class UpdateRequestMaterialsAsyncTask extends AsyncTask<Void, Void, Void> {

    @SuppressLint("StaticFieldLeak")
    private Context context;
    private ArrayList<Material> items;
    private ProgressDialog dialog;
    private int error;
    private boolean closeActivity;

    public UpdateRequestMaterialsAsyncTask(ArrayList<Material> items, boolean closeActivity, Context context) {
        this.items = items;
        this.context = context;
        this.closeActivity = closeActivity;
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
        ArrayList<Material> materials = getResquestMaterialList(UtilsClass.currentRequest.getIDREQUEST());
        JSONArray jsonArray = new JSONArray();
        UtilsClass.currentRequest.setTOTAL_VERPR(0);
        for (int i = 0; i < materials.size(); i++) {
            Material mat = findMaterial(materials.get(i).getMATERIAL());
            if (mat != null) {
                materials.set(i, mat);
                UtilsClass.currentRequest.setTOTAL_VERPR(UtilsClass.currentRequest.getTOTAL_VERPR() + mat.getVERPR() * mat.getREQ_QNT());
            }
            else {
                materials.get(i).setDELETE(1);
            }
            try {
                JSONObject jsonObject = new JSONObject(materials.get(i).toString());
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        UtilsClass.currentRequest.setMaterials(materials);
        error = updateMaterialList(jsonArray);
        updateRequest(UtilsClass.currentRequest);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        dialog.dismiss();
        if(error == 1){
            Toast.makeText(context, R.string.error_while_updating_data, Toast.LENGTH_SHORT).show();
        }
        else if(closeActivity){
            ((Activity)context).overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
            ((Activity)context).finish();
        }
    }

    private Material findMaterial(int material){
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getMATERIAL() == material)
                return items.get(i);
        }
        return null;
    }
}
