package com.rcdhotels.gestiondesolicitudes.task;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.rcdhotels.gestiondesolicitudes.R;
import com.rcdhotels.gestiondesolicitudes.model.Request;
import com.rcdhotels.gestiondesolicitudes.model.UtilsClass;
import com.rcdhotels.gestiondesolicitudes.model.Warehouse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.rcdhotels.gestiondesolicitudes.database.WarehouseTableQuerys.findWarehouseById;
import static com.rcdhotels.gestiondesolicitudes.model.UtilsClass.user;
import static com.rcdhotels.gestiondesolicitudes.services.WebServices.processRequest;
import static com.rcdhotels.gestiondesolicitudes.services.XSJSServices.getEmailUserToNotify;
import static com.rcdhotels.gestiondesolicitudes.services.XSJSServices.getResquestMaterialList;
import static com.rcdhotels.gestiondesolicitudes.services.XSJSServices.updateMaterialList;
import static com.rcdhotels.gestiondesolicitudes.services.XSJSServices.updateRequest;
import static com.rcdhotels.gestiondesolicitudes.utils.Tools.sendEmail;

public class ReprocessRequestsListAsyncTask extends AsyncTask<Void, Void, Void> {

    private ArrayList<Request> requests;
    private int affectedRow;
    @SuppressLint("StaticFieldLeak")
    private Context context;
    @SuppressLint("StaticFieldLeak")
    private ProgressDialog dialog;

    public ReprocessRequestsListAsyncTask(ArrayList<Request> requests, Context context) {
        this.requests = requests;
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
        for (int i = 0; i < requests.size(); i++) {
            reprocessRequestByRequest(requests.get(i));
        }
        return null;
    }
    @Override
    protected void onPostExecute (Void aVoid){
        super.onPostExecute(aVoid);
        int status = -1;
        if (user.getRole().equalsIgnoreCase("GS_REPRO")){
            status = 6;
        }
        if (UtilsClass.currentFragment.equalsIgnoreCase("Extraction"))
            new GetExtRequestAsyncTask(context, "", "", status, "", "", "").execute();
        else
            new GetRetRequestAsyncTask(context, "", "", status, "", "", "").execute();
        dialog.dismiss();
    }

    public void reprocessRequestByRequest(Request request) {
        request.setMaterials(getResquestMaterialList(request.getIDREQUEST()));
        Warehouse warehouse = findWarehouseById(request.getSTGE_LOC(), context);
        if (!warehouse.getConf().isEmpty()){
            updateRequest(request);
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < request.getMaterials().size(); i++) {
                request.getMaterials().get(i).setSTATUS_CONF(1);
                request.getMaterials().get(i).setQNT_TO_CONF(request.getMaterials().get(i).getREQ_QNT());
                try {
                    JSONObject jsonObject = new JSONObject(request.getMaterials().get(i).toString());
                    jsonArray.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            updateMaterialList(jsonArray);
        }
        processRequest(request);
        boolean found = false;
        for (int i = 0; i < request.getMaterials().size(); i++) {
            if (request.getMaterials().get(i).getPROCESSED() == 2){
                found = true;
                break;
            }
        }
        if (found) {
            request.setSTATUS(6);
            sendEmail(getEmailUserToNotify(request.getREQ_USER()), context.getString(R.string.app_name), context.getString(R.string.body_email4) + " " + request.getIDREQUEST(), context);
        }
        else {
            request.setSTATUS(7);
            sendEmail(getEmailUserToNotify(request.getREQ_USER()), context.getString(R.string.app_name), context.getString(R.string.body_email5) + " " + request.getIDREQUEST(), context);
        }
        updateRequest(request);
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < request.getMaterials().size(); i++) {
            if (request.getMaterials().get(i).getREQ_QNT() > 0) {
                try {
                    JSONObject jsonObject = new JSONObject(request.getMaterials().get(i).toString());
                    jsonArray.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        updateMaterialList(jsonArray);
    }
}
