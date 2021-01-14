package com.rcdhotels.gestiondesolicitudes.task;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.rcdhotels.gestiondesolicitudes.R;
import com.rcdhotels.gestiondesolicitudes.model.Material;
import com.rcdhotels.gestiondesolicitudes.model.Request;
import com.rcdhotels.gestiondesolicitudes.model.UtilsClass;
import com.rcdhotels.gestiondesolicitudes.model.Warehouse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.rcdhotels.gestiondesolicitudes.database.WarehouseTableQuerys.findWarehouseById;
import static com.rcdhotels.gestiondesolicitudes.model.UtilsClass.user;
import static com.rcdhotels.gestiondesolicitudes.services.XSJSServices.getEmailsToNotify;
import static com.rcdhotels.gestiondesolicitudes.services.XSJSServices.insertMaterial;
import static com.rcdhotels.gestiondesolicitudes.services.XSJSServices.insertRequest;
import static com.rcdhotels.gestiondesolicitudes.utils.Tools.sendEmail;

public class CreateExtRequestAsyncTask extends AsyncTask<Void, Void, Void> {

    @SuppressLint("StaticFieldLeak")
    private Context context;
    private ProgressDialog dialog;
    private String headerTxt;
    private boolean error = false;

    public CreateExtRequestAsyncTask(String headerTxt, Context context) {
        this.headerTxt = headerTxt;
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

    @SuppressLint("SimpleDateFormat")
    @Override
    protected Void doInBackground(Void... voids) {

        UtilsClass.currentRequest.setHEADER_TXT(headerTxt);
        UtilsClass.currentRequest.setCREATED_DATE(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        Warehouse warehouse = findWarehouseById(UtilsClass.currentRequest.getSTGE_LOC(), context);
        if (warehouse.getConf() != null && !warehouse.getConf().isEmpty())
            UtilsClass.currentRequest.setCONF(1);
        else
            UtilsClass.currentRequest.setCONF(0);

        for (int i = 0; i < UtilsClass.currentRequest.getMaterials().size(); i++) {
            UtilsClass.currentRequest.setTOTAL_VERPR(UtilsClass.currentRequest.getTOTAL_VERPR() + UtilsClass.currentRequest.getMaterials().get(i).getVERPR() * UtilsClass.currentRequest.getMaterials().get(i).getREQ_QNT());
        }
        insertRequest();
        if (UtilsClass.currentRequest.getIDREQUEST() > 0){
            for (int i = 0; i < UtilsClass.currentRequest.getMaterials().size(); i++) {
                UtilsClass.currentRequest.getMaterials().get(i).setPOSNR(i+1);
                UtilsClass.currentRequest.getMaterials().get(i).setIDREQUEST(UtilsClass.currentRequest.getIDREQUEST());
                UtilsClass.currentRequest.getMaterials().set(i, insertMaterial(UtilsClass.currentRequest.getMaterials().get(i)));
            }
            sendEmail(getEmailsToNotify(user.getWarehouse(), "GS_AUTOR1"), context.getString(R.string.app_name), context.getString(R.string.body_email) +" "+ UtilsClass.currentRequest.getIDREQUEST(), context);
            UtilsClass.currentRequest = null;
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        dialog.dismiss();
        ((Activity)context).setResult(Activity.RESULT_OK, new Intent());
        ((Activity)context).overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        ((Activity)context).finish();
    }
}
