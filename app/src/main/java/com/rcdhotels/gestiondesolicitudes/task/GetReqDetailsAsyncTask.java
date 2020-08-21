package com.rcdhotels.gestiondesolicitudes.task;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.rcdhotels.gestiondesolicitudes.R;
import com.rcdhotels.gestiondesolicitudes.model.Request;
import com.rcdhotels.gestiondesolicitudes.model.UtilsClass;
import com.rcdhotels.gestiondesolicitudes.ui.activities.ReqDetailsActivity;

import static com.rcdhotels.gestiondesolicitudes.services.XSJSServices.getRequestById;
import static com.rcdhotels.gestiondesolicitudes.services.XSJSServices.getResquestMaterialList;

public class GetReqDetailsAsyncTask extends AsyncTask<Void, Void, Void> {

    private int idRequest;
    @SuppressLint("StaticFieldLeak")
    private Context context;
    private Request request;
    private RecyclerView recyclerViewMaterials;
    private ProgressDialog dialog;

    public GetReqDetailsAsyncTask(int idRequest, Context context) {
        this.idRequest = idRequest;
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
        request = getRequestById(idRequest);
        if (request != null){
            request.setMaterials(getResquestMaterialList(idRequest));
        }
        return null;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        dialog.dismiss();
        if (request == null) {
            ((Activity) context).runOnUiThread(() -> Toast.makeText(context, context.getResources().getString(R.string.connection_failed), Toast.LENGTH_LONG).show());
        }
        else{
            UtilsClass.currentRequest = request;
            context.startActivity(new Intent(context, ReqDetailsActivity.class));
            ((Activity) context).overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        }
    }
}
