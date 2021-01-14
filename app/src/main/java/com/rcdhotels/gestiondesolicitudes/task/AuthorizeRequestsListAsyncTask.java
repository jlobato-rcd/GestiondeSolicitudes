package com.rcdhotels.gestiondesolicitudes.task;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.rcdhotels.gestiondesolicitudes.R;
import com.rcdhotels.gestiondesolicitudes.model.Authorization;
import com.rcdhotels.gestiondesolicitudes.model.Request;
import com.rcdhotels.gestiondesolicitudes.model.UtilsClass;

import java.util.ArrayList;

import static com.rcdhotels.gestiondesolicitudes.model.UtilsClass.user;
import static com.rcdhotels.gestiondesolicitudes.services.XSJSServices.authorizeRequest;
import static com.rcdhotels.gestiondesolicitudes.services.XSJSServices.getAuthorizationLevel;
import static com.rcdhotels.gestiondesolicitudes.services.XSJSServices.getEmailUserToNotify;
import static com.rcdhotels.gestiondesolicitudes.services.XSJSServices.getEmailsToNotify;
import static com.rcdhotels.gestiondesolicitudes.utils.Tools.sendEmail;

public class AuthorizeRequestsListAsyncTask extends AsyncTask<Void, Void, Void> {

    private ArrayList<Request> requests;
    private int affectedRow;
    @SuppressLint("StaticFieldLeak")
    private Context context;
    @SuppressLint("StaticFieldLeak")
    private ProgressDialog dialog;

    public AuthorizeRequestsListAsyncTask(ArrayList<Request> requests, Context context) {
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
            authorizeRequestByRequest(requests.get(i));
        }
        return null;
    }
    @Override
    protected void onPostExecute (Void aVoid){
        super.onPostExecute(aVoid);
        if (UtilsClass.currentFragment.equalsIgnoreCase("Extraction"))
            new GetExtRequestAsyncTask(context, "", "", -1, "", user.getWarehouse(), "").execute();
        else
            new GetRetRequestAsyncTask(context, "", "", -1, user.getWarehouse(), "", "").execute();
        dialog.dismiss();
    }

    public void authorizeRequestByRequest(Request request) {
        if (request.getTYPE() == 1){
            Authorization authorization;
            switch (user.getRole()) {
                case "GS_AUTOR1":
                    authorization = getAuthorizationLevel(1);
                    if (request.getTOTAL_VERPR() > authorization.getAmount() && (UtilsClass.currentRequest.getSTGE_LOC().equalsIgnoreCase("AC01") || UtilsClass.currentRequest.getSTGE_LOC().equalsIgnoreCase("AC02") || UtilsClass.currentRequest.getSTGE_LOC().equalsIgnoreCase("ACGC"))) {
                        affectedRow = authorizeRequest(UtilsClass.currentRequest.getTOTAL_VERPR(),0, 1, request.getIDREQUEST());
                        sendEmail(getEmailsToNotify(request.getSTGE_LOC(), "GS_AUTOR2"), context.getString(R.string.app_name), context.getString(R.string.body_email) + " " + request.getIDREQUEST(), context);
                    }
                    else{
                        affectedRow = authorizeRequest(UtilsClass.currentRequest.getTOTAL_VERPR(),3, 3, request.getIDREQUEST());
                        sendEmail(getEmailUserToNotify(request.getREQ_USER()), context.getString(R.string.app_name), context.getString(R.string.body_email6) + " " + request.getIDREQUEST(), context);
                    }
                    break;
                case "GS_AUTOR2":
                    authorization = getAuthorizationLevel(2);
                    if (request.getTOTAL_VERPR() > authorization.getAmount() && (UtilsClass.currentRequest.getSTGE_LOC().equalsIgnoreCase("AC01") || UtilsClass.currentRequest.getSTGE_LOC().equalsIgnoreCase("AC02") || UtilsClass.currentRequest.getSTGE_LOC().equalsIgnoreCase("ACGC"))) {
                        affectedRow = authorizeRequest(UtilsClass.currentRequest.getTOTAL_VERPR(),0, 2, request.getIDREQUEST());
                        sendEmail(getEmailsToNotify(request.getSTGE_LOC(), "GS_AUTOR3"), context.getString(R.string.app_name), context.getString(R.string.body_email) + " " + request.getIDREQUEST(), context);
                    }
                    else{
                        affectedRow = authorizeRequest(UtilsClass.currentRequest.getTOTAL_VERPR(),3, 3, request.getIDREQUEST());
                        sendEmail(getEmailUserToNotify(request.getREQ_USER()), context.getString(R.string.app_name), context.getString(R.string.body_email6) + " " + request.getIDREQUEST(), context);
                    }
                    break;
                case "GS_AUTOR3":
                    affectedRow = authorizeRequest(UtilsClass.currentRequest.getTOTAL_VERPR(),3, 3, request.getIDREQUEST());
                    sendEmail(getEmailsToNotify(request.getSTGE_LOC(), "GS_PROCE"), context.getString(R.string.app_name), context.getString(R.string.body_email6) + " " + request.getIDREQUEST(), context);
                    sendEmail(getEmailUserToNotify(request.getREQ_USER()), context.getString(R.string.app_name), context.getString(R.string.body_email6) + " " + request.getIDREQUEST(), context);
                    break;
            }
        }
        else{
            switch (user.getRole()) {
                case "GS_AUTOR1":
                    affectedRow = authorizeRequest(UtilsClass.currentRequest.getTOTAL_VERPR(),3, 3, request.getIDREQUEST());
                    sendEmail(getEmailsToNotify(request.getSTGE_LOC(), "GS_PROCE"), context.getString(R.string.app_name), context.getString(R.string.body_email6) + " " + request.getIDREQUEST(), context);
                    sendEmail(getEmailUserToNotify(request.getREQ_USER()), context.getString(R.string.app_name), context.getString(R.string.body_email6) + " " + request.getIDREQUEST(), context);
                    break;
            }
        }
    }
}
