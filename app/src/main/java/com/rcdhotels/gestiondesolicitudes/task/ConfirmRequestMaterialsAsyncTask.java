package com.rcdhotels.gestiondesolicitudes.task;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.rcdhotels.gestiondesolicitudes.R;
import com.rcdhotels.gestiondesolicitudes.model.Material;
import com.rcdhotels.gestiondesolicitudes.model.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.rcdhotels.gestiondesolicitudes.services.XSJSServices.getEmailUserToNotify;
import static com.rcdhotels.gestiondesolicitudes.services.XSJSServices.getEmailsToNotify;
import static com.rcdhotels.gestiondesolicitudes.services.XSJSServices.getRequestById;
import static com.rcdhotels.gestiondesolicitudes.services.XSJSServices.updateMaterialList;
import static com.rcdhotels.gestiondesolicitudes.services.XSJSServices.updateRequest;
import static com.rcdhotels.gestiondesolicitudes.services.WebServices.processRequest;
import static com.rcdhotels.gestiondesolicitudes.utils.Tools.sendEmail;

public class ConfirmRequestMaterialsAsyncTask extends AsyncTask<Void, Void, Void> {

    @SuppressLint("StaticFieldLeak")
    private Context context;
    private ArrayList<Material> materials;
    private ProgressDialog dialog;
    private int error;

    public ConfirmRequestMaterialsAsyncTask(ArrayList<Material> items, Context context) {
        this.materials = items;
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
        Request request = getRequestById(materials.get(0).getIDREQUEST());
        request.setMaterials(materials);
        boolean canBeProcess = false;
        for (int i = 0; i < request.getMaterials().size(); i++) {
            if (request.getMaterials().get(i).getSTATUS_CONF() == 1 && request.getMaterials().get(i).getREQ_QNT() == request.getMaterials().get(i).getQNT_TO_CONF()){
                canBeProcess = true;
                break;
            }
        }
        if (canBeProcess)
            request = processRequest(request);

        for (int i = 0; i < request.getMaterials().size(); i++) {
            if (request.getMaterials().get(i).getSTATUS_CONF() == 0 || request.getMaterials().get(i).getREQ_QNT() != request.getMaterials().get(i).getREQ_QNT()) {
                request.setCONF(2);
                request.setSTATUS(5);
                sendEmail(getEmailsToNotify(request.getSTGE_LOC(), "GS_PROCE"), context.getString(R.string.app_name), context.getString(R.string.body_email7) + " " + request.getIDREQUEST(), context);
                break;
            }
        }

        if (request.getSTATUS() != 5){
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
                request.setCONF(1);
                request.setSTATUS(7);
                sendEmail(getEmailUserToNotify(request.getREQ_USER()), context.getString(R.string.app_name), context.getString(R.string.body_email5) + " " + request.getIDREQUEST(), context);
            }
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
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        dialog.dismiss();
        if(error == 1){
            Toast.makeText(context, R.string.error_while_updating_data, Toast.LENGTH_SHORT).show();
        }
        else{
            ((Activity)context).overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
            ((Activity)context).finish();
        }
    }
}
