package com.rcdhotels.gestiondesolicitudes.task;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.rcdhotels.gestiondesolicitudes.R;
import com.rcdhotels.gestiondesolicitudes.model.Material;

import com.rcdhotels.gestiondesolicitudes.model.UtilsClass;
import com.rcdhotels.gestiondesolicitudes.model.Warehouse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.rcdhotels.gestiondesolicitudes.database.WarehouseTableQuerys.findWarehouseById;
import static com.rcdhotels.gestiondesolicitudes.services.XSJSServices.getEmailUserToNotify;
import static com.rcdhotels.gestiondesolicitudes.services.XSJSServices.updateMaterialList;
import static com.rcdhotels.gestiondesolicitudes.services.XSJSServices.updateRequest;
import static com.rcdhotels.gestiondesolicitudes.services.WebServices.processRequest;
import static com.rcdhotels.gestiondesolicitudes.utils.Tools.sendEmail;

public class ProcessRequestAsyncTask extends AsyncTask<Void, Void, Void> {

    @SuppressLint("StaticFieldLeak")
    private Context context;
    private ProgressDialog dialog;
    private ArrayList<Material> materials;

    public ProcessRequestAsyncTask(ArrayList<Material> materials, Context context) {
        this.materials = materials;
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
        Warehouse warehouse = findWarehouseById(UtilsClass.currentRequest.getSTGE_LOC(), context);
        if (!warehouse.getConf().isEmpty()){
            UtilsClass.currentRequest.setSTATUS(4);
            UtilsClass.currentRequest.setMaterials(materials);
            UtilsClass.currentRequest.setTOTAL_VERPR(0);
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < UtilsClass.currentRequest.getMaterials().size(); i++) {
                UtilsClass.currentRequest.setTOTAL_VERPR( UtilsClass.currentRequest.getTOTAL_VERPR() +  UtilsClass.currentRequest.getMaterials().get(i).getREQ_QNT() *  UtilsClass.currentRequest.getMaterials().get(i).getVERPR());
                UtilsClass.currentRequest.getMaterials().get(i).setQNT_TO_CONF(UtilsClass.currentRequest.getMaterials().get(i).getREQ_QNT());
                try {
                    JSONObject jsonObject = new JSONObject(UtilsClass.currentRequest.getMaterials().get(i).toString());
                    jsonArray.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            updateRequest(UtilsClass.currentRequest);
            updateMaterialList(jsonArray);
            sendEmail(getEmailUserToNotify(UtilsClass.currentRequest.getREQ_USER()), context.getString(R.string.app_name), context.getString(R.string.body_email3) + " " + UtilsClass.currentRequest.getIDREQUEST(), context);
        }
        else{
            UtilsClass.currentRequest.setMaterials(materials);
            processRequest(UtilsClass.currentRequest);
            UtilsClass.currentRequest.setTOTAL_VERPR(0);
            boolean found = false;
            for (int i = 0; i < UtilsClass.currentRequest.getMaterials().size(); i++) {
                UtilsClass.currentRequest.setTOTAL_VERPR( UtilsClass.currentRequest.getTOTAL_VERPR() +  UtilsClass.currentRequest.getMaterials().get(i).getREQ_QNT() *  UtilsClass.currentRequest.getMaterials().get(i).getVERPR());
                if (UtilsClass.currentRequest.getMaterials().get(i).getPROCESSED() == 2){
                    found = true;
                    break;
                }
            }
            if (found) {
                UtilsClass.currentRequest.setSTATUS(6);
                sendEmail(getEmailUserToNotify(UtilsClass.currentRequest.getREQ_USER()), context.getString(R.string.app_name), context.getString(R.string.body_email4) + " " + UtilsClass.currentRequest.getIDREQUEST(), context);
            }
            else {
                UtilsClass.currentRequest.setSTATUS(7);
                sendEmail(getEmailUserToNotify(UtilsClass.currentRequest.getREQ_USER()), context.getString(R.string.app_name), context.getString(R.string.body_email5) + " " + UtilsClass.currentRequest.getIDREQUEST(), context);
            }
            updateRequest(UtilsClass.currentRequest);
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < UtilsClass.currentRequest.getMaterials().size(); i++) {
                if (UtilsClass.currentRequest.getMaterials().get(i).getREQ_QNT() > 0){
                    try {
                        JSONObject jsonObject = new JSONObject(UtilsClass.currentRequest.getMaterials().get(i).toString());
                        jsonArray.put(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            updateMaterialList(jsonArray);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        dialog.dismiss();
        ((Activity)context).overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        ((Activity)context).finish();
    }
}
