package com.rcdhotels.gestiondesolicitudes.task;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.rcdhotels.gestiondesolicitudes.R;
import com.rcdhotels.gestiondesolicitudes.model.Request;
import com.rcdhotels.gestiondesolicitudes.model.UtilsClass;
import com.rcdhotels.gestiondesolicitudes.model.Warehouse;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.rcdhotels.gestiondesolicitudes.database.WarehouseTableQuerys.findWarehouseById;
import static com.rcdhotels.gestiondesolicitudes.model.UtilsClass.user;
import static com.rcdhotels.gestiondesolicitudes.services.XSJSServices.getEmailsToNotify;
import static com.rcdhotels.gestiondesolicitudes.services.XSJSServices.getRequestById;
import static com.rcdhotels.gestiondesolicitudes.services.XSJSServices.getResquestMaterialList;
import static com.rcdhotels.gestiondesolicitudes.services.XSJSServices.insertMaterial;
import static com.rcdhotels.gestiondesolicitudes.services.XSJSServices.insertRequest;
import static com.rcdhotels.gestiondesolicitudes.utils.Tools.sendEmail;

public class CreateExtRequestAgainAsyncTask extends AsyncTask<Void, Void, Void> {

    @SuppressLint("StaticFieldLeak")
    private Context context;
    private ProgressDialog dialog;
    private int idRequest;

    public CreateExtRequestAgainAsyncTask(int idRequest, Context context) {
        this.idRequest = idRequest;
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
        UtilsClass.currentRequest = getRequestById(idRequest);
        UtilsClass.currentRequest.setSTATUS(1);
        UtilsClass.currentRequest.setREQ_USER(user.getUserName());
        UtilsClass.currentRequest.setCREATED_DATE(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        UtilsClass.currentRequest.setMaterials(getResquestMaterialList(idRequest));
        Warehouse warehouse = findWarehouseById(user.getWarehouse(), context);
        if (warehouse.getConf() != null && !warehouse.getConf().isEmpty())
            UtilsClass.currentRequest.setCONF(1);
        else
            UtilsClass.currentRequest.setCONF(0);
        insertRequest();
        if (UtilsClass.currentRequest.getIDREQUEST() > 0){
            for (int i = 0; i < UtilsClass.currentRequest.getMaterials().size(); i++) {
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
        ((Activity)context).overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        ((Activity)context).finish();
    }
}
