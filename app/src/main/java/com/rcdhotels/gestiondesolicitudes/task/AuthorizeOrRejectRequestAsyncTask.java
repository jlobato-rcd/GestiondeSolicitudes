package com.rcdhotels.gestiondesolicitudes.task;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.rcdhotels.gestiondesolicitudes.R;
import com.rcdhotels.gestiondesolicitudes.model.Authorization;
import com.rcdhotels.gestiondesolicitudes.model.Material;
import com.rcdhotels.gestiondesolicitudes.model.Request;
import com.rcdhotels.gestiondesolicitudes.model.UtilsClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.rcdhotels.gestiondesolicitudes.model.UtilsClass.findMaterial;
import static com.rcdhotels.gestiondesolicitudes.services.XSJSServices.authorizeRequest;
import static com.rcdhotels.gestiondesolicitudes.services.XSJSServices.getAuthorizationLevel;
import static com.rcdhotels.gestiondesolicitudes.services.XSJSServices.getEmailUserToNotify;
import static com.rcdhotels.gestiondesolicitudes.services.XSJSServices.getEmailsToNotify;
import static com.rcdhotels.gestiondesolicitudes.services.XSJSServices.getRequestById;
import static com.rcdhotels.gestiondesolicitudes.services.XSJSServices.getResquestMaterialList;
import static com.rcdhotels.gestiondesolicitudes.services.XSJSServices.rejectRequest;
import static com.rcdhotels.gestiondesolicitudes.services.XSJSServices.updateMaterialList;
import static com.rcdhotels.gestiondesolicitudes.utils.Tools.sendEmail;

public class AuthorizeOrRejectRequestAsyncTask extends AsyncTask<Void, Void, Void> {

    private boolean release;
    private String reasons;
    private int affectedRow;
    @SuppressLint("StaticFieldLeak")
    private Context context;
    @SuppressLint("StaticFieldLeak")
    private ProgressDialog dialog;

    public AuthorizeOrRejectRequestAsyncTask(boolean release, String reasons, Context context) {
        this.release = release;
        this.reasons = reasons;
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
        ArrayList<Material> materials = getResquestMaterialList(UtilsClass.currentRequest.getIDREQUEST());
        UtilsClass.currentRequest.setTOTAL_VERPR(0);
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < materials.size(); i++) {
            Material mat = findMaterial(materials.get(i).getMATERIAL());
            if (mat != null){
                materials.set(i, mat);
                UtilsClass.currentRequest.setTOTAL_VERPR(UtilsClass.currentRequest.getTOTAL_VERPR() + mat.getVERPR() * mat.getREQ_QNT());
            }
            else
                materials.get(i).setDELETE(1);
            try {
                JSONObject jsonObject = new JSONObject(materials.get(i).toString());
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(updateMaterialList(jsonArray) == 0) {
            UtilsClass.currentRequest.setMaterials(materials);
            if (UtilsClass.currentRequest.getTYPE() == 1) {
                if (release) {
                    Authorization authorization;
                    switch (UtilsClass.user.getRole()) {
                        case "GS_AUTOR1":
                            authorization = getAuthorizationLevel(1);
                            if (UtilsClass.currentRequest.getTOTAL_VERPR() > authorization.getAmount() && (UtilsClass.currentRequest.getSTGE_LOC().equalsIgnoreCase("AC01") || UtilsClass.currentRequest.getSTGE_LOC().equalsIgnoreCase("AC02") || UtilsClass.currentRequest.getSTGE_LOC().equalsIgnoreCase("ACGC"))) {
                                affectedRow = authorizeRequest(UtilsClass.currentRequest.getTOTAL_VERPR(), 0, 1, UtilsClass.currentRequest.getIDREQUEST());
                                sendEmail(getEmailsToNotify(UtilsClass.currentRequest.getSTGE_LOC(), "GS_AUTOR2"), context.getString(R.string.app_name), context.getString(R.string.body_email) + " " + UtilsClass.currentRequest.getIDREQUEST(), context);
                            }
                            else {
                                affectedRow = authorizeRequest(UtilsClass.currentRequest.getTOTAL_VERPR(), 3, 3, UtilsClass.currentRequest.getIDREQUEST());
                                sendEmail(getEmailUserToNotify(UtilsClass.currentRequest.getREQ_USER()), context.getString(R.string.app_name), context.getString(R.string.body_email6) + " " + UtilsClass.currentRequest.getIDREQUEST(), context);
                            }
                            break;
                        case "GS_AUTOR2":
                            authorization = getAuthorizationLevel(2);
                            if (UtilsClass.currentRequest.getTOTAL_VERPR() > authorization.getAmount() && (UtilsClass.currentRequest.getSTGE_LOC().equalsIgnoreCase("AC01") || UtilsClass.currentRequest.getSTGE_LOC().equalsIgnoreCase("AC02") || UtilsClass.currentRequest.getSTGE_LOC().equalsIgnoreCase("ACGC"))) {
                                affectedRow = authorizeRequest(UtilsClass.currentRequest.getTOTAL_VERPR(), 0, 2, UtilsClass.currentRequest.getIDREQUEST());
                                sendEmail(getEmailsToNotify(UtilsClass.currentRequest.getSTGE_LOC(), "GS_AUTOR3"), context.getString(R.string.app_name), context.getString(R.string.body_email) + " " + UtilsClass.currentRequest.getIDREQUEST(), context);
                            } else {
                                affectedRow = authorizeRequest(UtilsClass.currentRequest.getTOTAL_VERPR(), 3, 3, UtilsClass.currentRequest.getIDREQUEST());
                                sendEmail(getEmailUserToNotify(UtilsClass.currentRequest.getREQ_USER()), context.getString(R.string.app_name), context.getString(R.string.body_email6) + " " + UtilsClass.currentRequest.getIDREQUEST(), context);
                            }
                            break;
                        case "GS_AUTOR3":
                            authorization = getAuthorizationLevel(3);
                            affectedRow = authorizeRequest(UtilsClass.currentRequest.getTOTAL_VERPR(), 3, 3, UtilsClass.currentRequest.getIDREQUEST());
                            sendEmail(getEmailsToNotify(UtilsClass.currentRequest.getSTGE_LOC(), "GS_PROCE"), context.getString(R.string.app_name), context.getString(R.string.body_email6) + " " + UtilsClass.currentRequest.getIDREQUEST(), context);
                            sendEmail(getEmailUserToNotify(UtilsClass.currentRequest.getREQ_USER()), context.getString(R.string.app_name), context.getString(R.string.body_email6) + " " + UtilsClass.currentRequest.getIDREQUEST(), context);
                            break;
                    }
                } else {
                    affectedRow = rejectRequest(2, reasons, UtilsClass.currentRequest.getIDREQUEST());
                    sendEmail(getEmailUserToNotify(UtilsClass.currentRequest.getREQ_USER()), context.getString(R.string.app_name), context.getString(R.string.body_email) + " " + UtilsClass.currentRequest.getIDREQUEST(), context);
                }
            }
            else {
                if (release) {
                    switch (UtilsClass.user.getRole()) {
                        case "GS_AUTOR1":
                            affectedRow = authorizeRequest(UtilsClass.currentRequest.getTOTAL_VERPR(), 3, 3, UtilsClass.currentRequest.getIDREQUEST());
                            sendEmail(getEmailsToNotify(UtilsClass.currentRequest.getSTGE_LOC(), "GS_PROCE"), context.getString(R.string.app_name), context.getString(R.string.body_email6) + " " + UtilsClass.currentRequest.getIDREQUEST(), context);
                            sendEmail(getEmailUserToNotify(UtilsClass.currentRequest.getREQ_USER()), context.getString(R.string.app_name), context.getString(R.string.body_email6) + " " + UtilsClass.currentRequest.getIDREQUEST(), context);
                            break;
                    }
                } else {
                    affectedRow = rejectRequest(2, reasons, UtilsClass.currentRequest.getIDREQUEST());
                    sendEmail(getEmailUserToNotify(UtilsClass.currentRequest.getREQ_USER()), context.getString(R.string.app_name), context.getString(R.string.body_email) + " " + UtilsClass.currentRequest.getIDREQUEST(), context);
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute (Void aVoid){
        super.onPostExecute(aVoid);
        dialog.dismiss();
        if (affectedRow == 0)
            Toast.makeText(context, R.string.request_authorize_messge, Toast.LENGTH_SHORT).show();
        ((Activity)context).overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        ((Activity)context).finish();
    }

    private Material findMaterial(int material){
        for (int i = 0; i < UtilsClass.currentRequest.getMaterials().size(); i++) {
            if (UtilsClass.currentRequest.getMaterials().get(i).getMATERIAL() == material)
                return UtilsClass.currentRequest.getMaterials().get(i);
        }
        return null;
    }
}
