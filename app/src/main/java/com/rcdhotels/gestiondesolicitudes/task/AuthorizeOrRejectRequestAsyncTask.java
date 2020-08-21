package com.rcdhotels.gestiondesolicitudes.task;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.rcdhotels.gestiondesolicitudes.R;
import com.rcdhotels.gestiondesolicitudes.model.Authorization;
import com.rcdhotels.gestiondesolicitudes.model.Request;
import com.rcdhotels.gestiondesolicitudes.model.UtilsClass;

import static com.rcdhotels.gestiondesolicitudes.services.XSJSServices.authorizeRequest;
import static com.rcdhotels.gestiondesolicitudes.services.XSJSServices.getAuthorizationLevel;
import static com.rcdhotels.gestiondesolicitudes.services.XSJSServices.getEmailUserToNotify;
import static com.rcdhotels.gestiondesolicitudes.services.XSJSServices.getEmailsToNotify;
import static com.rcdhotels.gestiondesolicitudes.services.XSJSServices.getRequestById;
import static com.rcdhotels.gestiondesolicitudes.services.XSJSServices.rejectRequest;
import static com.rcdhotels.gestiondesolicitudes.utils.Tools.sendEmail;

public class AuthorizeOrRejectRequestAsyncTask extends AsyncTask<Void, Void, Void> {

    private int idRequest;
    private String reqUser;
    private float totalVerpr;
    private boolean release;
    private String reasons;
    private int affectedRow;
    @SuppressLint("StaticFieldLeak")
    private Context context;
    @SuppressLint("StaticFieldLeak")
    private ProgressDialog dialog;

    public AuthorizeOrRejectRequestAsyncTask(int idRequest, String reqUser, float totalVerpr, boolean release, String reasons, Context context) {
        this.idRequest = idRequest;
        this.reqUser = reqUser;
        this.totalVerpr = totalVerpr;
        this.release = release;
        this.reasons = reasons;
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
        Request request = getRequestById(idRequest);
        if (request.getTYPE() == 1){
            if (release) {
                Authorization authorization;
                switch (UtilsClass.user.getRole()) {
                    case "GS_AUTOR1":
                        authorization = getAuthorizationLevel(1);
                        if (totalVerpr > authorization.getAmount()) {
                            affectedRow = authorizeRequest(0, 1, idRequest);
                            sendEmail(getEmailsToNotify(request.getSTGE_LOC(), "GS_AUTOR2"), context.getString(R.string.app_name), context.getString(R.string.body_email) + " " + idRequest, context);
                        }
                        else{
                            affectedRow = authorizeRequest(3, 3, idRequest);
                            sendEmail(getEmailUserToNotify(request.getREQ_USER()), context.getString(R.string.app_name), context.getString(R.string.body_email6) + " " + idRequest, context);
                        }
                        break;
                    case "GS_AUTOR2":
                        authorization = getAuthorizationLevel(2);
                        if (totalVerpr > authorization.getAmount()) {
                            affectedRow = authorizeRequest(0, 2, idRequest);
                            sendEmail(getEmailsToNotify(request.getSTGE_LOC(), "GS_AUTOR3"), context.getString(R.string.app_name), context.getString(R.string.body_email) + " " + idRequest, context);
                        }
                        else{
                            affectedRow = authorizeRequest(3, 3, idRequest);
                            sendEmail(getEmailUserToNotify(request.getREQ_USER()), context.getString(R.string.app_name), context.getString(R.string.body_email6) + " " + idRequest, context);
                        }
                        break;
                    case "GS_AUTOR3":
                        authorization = getAuthorizationLevel(3);
                        affectedRow = authorizeRequest(3, 3, idRequest);
                        sendEmail(getEmailsToNotify(request.getSTGE_LOC(), "GS_PROCE"), context.getString(R.string.app_name), context.getString(R.string.body_email6) + " " + idRequest, context);
                        sendEmail(getEmailUserToNotify(request.getREQ_USER()), context.getString(R.string.app_name), context.getString(R.string.body_email6) + " " + idRequest, context);
                        break;
                }
            }
            else{
                affectedRow = rejectRequest(2, reasons, idRequest);
                sendEmail(getEmailUserToNotify(request.getREQ_USER()), context.getString(R.string.app_name), context.getString(R.string.body_email) + " " + idRequest, context);
            }
        }
        else{
            if (release) {
                switch (UtilsClass.user.getRole()) {
                    case "GS_AUTOR1":
                        affectedRow = authorizeRequest(3, 3, idRequest);
                        sendEmail(getEmailsToNotify(request.getSTGE_LOC(), "GS_PROCE"), context.getString(R.string.app_name), context.getString(R.string.body_email6) + " " + idRequest, context);
                        sendEmail(getEmailUserToNotify(request.getREQ_USER()), context.getString(R.string.app_name), context.getString(R.string.body_email6) + " " + idRequest, context);
                        break;
                }
            }
            else{
                affectedRow = rejectRequest(2, reasons, idRequest);
                sendEmail(getEmailUserToNotify(request.getREQ_USER()), context.getString(R.string.app_name), context.getString(R.string.body_email) + " " + idRequest, context);
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute (Void aVoid){
        super.onPostExecute(aVoid);
        dialog.dismiss();
        ((Activity)context).overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        ((Activity)context).finish();
    }
}
