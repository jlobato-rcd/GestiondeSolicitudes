package com.rcdhotels.gestiondesolicitudes.task;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.rcdhotels.gestiondesolicitudes.R;
import com.rcdhotels.gestiondesolicitudes.model.Request;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.rcdhotels.gestiondesolicitudes.services.XSJSServices.updateRequest;

public class DeleteRequestAsyncTask extends AsyncTask<Void, Void, Void> {

    private Request request;
    @SuppressLint("StaticFieldLeak")
    private Context context;
    private ProgressDialog dialog;
    private int affectedRow;

    public DeleteRequestAsyncTask(Request request, Context context) {
        this.request = request;
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
        affectedRow = updateRequest(request);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        dialog.dismiss();
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        new GetExtRequestAsyncTask(context, date, -1).execute();
    }
}
