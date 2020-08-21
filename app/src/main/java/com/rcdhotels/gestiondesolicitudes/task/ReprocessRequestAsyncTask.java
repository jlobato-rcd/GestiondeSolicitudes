package com.rcdhotels.gestiondesolicitudes.task;

import android.annotation.SuppressLint;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.rcdhotels.gestiondesolicitudes.database.WarehouseTableQuerys.findWarehouseById;
import static com.rcdhotels.gestiondesolicitudes.services.XSJSServices.getEmailUserToNotify;
import static com.rcdhotels.gestiondesolicitudes.services.XSJSServices.getResquestMaterialList;
import static com.rcdhotels.gestiondesolicitudes.services.XSJSServices.updateMaterialList;
import static com.rcdhotels.gestiondesolicitudes.services.XSJSServices.updateRequest;
import static com.rcdhotels.gestiondesolicitudes.services.WebServices.processRequest;
import static com.rcdhotels.gestiondesolicitudes.utils.Tools.sendEmail;

public class ReprocessRequestAsyncTask extends AsyncTask<Void, Void, Void> {

    @SuppressLint("StaticFieldLeak")
    private Context context;
    private ProgressDialog dialog;
    private ArrayList<Material> materials;

    public ReprocessRequestAsyncTask(Context context) {
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
        materials = getResquestMaterialList(UtilsClass.currentRequest.getIDREQUEST());
        Warehouse warehouse = findWarehouseById(UtilsClass.currentRequest.getSTGE_LOC(), context);
        if (!warehouse.getConf().isEmpty()){
            updateRequest(UtilsClass.currentRequest);
            UtilsClass.currentRequest.setMaterials(materials);
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < UtilsClass.currentRequest.getMaterials().size(); i++) {
                UtilsClass.currentRequest.getMaterials().get(i).setSTATUS_CONF(1);
                UtilsClass.currentRequest.getMaterials().get(i).setQNT_TO_CONF(UtilsClass.currentRequest.getMaterials().get(i).getREQ_QNT());
                try {
                    JSONObject jsonObject = new JSONObject(UtilsClass.currentRequest.getMaterials().get(i).toString());
                    jsonArray.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            updateMaterialList(jsonArray);
        }
        UtilsClass.currentRequest.setMaterials(materials);
        processRequest(UtilsClass.currentRequest);
        boolean found = false;
        for (int i = 0; i < UtilsClass.currentRequest.getMaterials().size(); i++) {
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
            if (UtilsClass.currentRequest.getMaterials().get(i).getREQ_QNT() > 0) {
                try {
                    JSONObject jsonObject = new JSONObject(UtilsClass.currentRequest.getMaterials().get(i).toString());
                    jsonArray.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        updateMaterialList(jsonArray);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        dialog.dismiss();
        @SuppressLint("SimpleDateFormat")
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        if (UtilsClass.currentFragment.equalsIgnoreCase("Extraction"))
            new GetExtRequestAsyncTask(context, date, -1).execute();
        else
            new GetRetRequestAsyncTask(context, date, -1).execute();
    }
}
