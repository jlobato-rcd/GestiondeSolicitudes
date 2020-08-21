package com.rcdhotels.gestiondesolicitudes.task;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.rcdhotels.gestiondesolicitudes.R;
import com.rcdhotels.gestiondesolicitudes.model.Material;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.rcdhotels.gestiondesolicitudes.services.XSJSServices.updateMaterialList;

public class UpdateRequestMaterialsAsyncTask extends AsyncTask<Void, Void, Void> {

    @SuppressLint("StaticFieldLeak")
    private Context context;
    private ArrayList<Material> items;
    private ProgressDialog dialog;
    private int error;

    public UpdateRequestMaterialsAsyncTask(ArrayList<Material> items, Context context) {
        this.items = items;
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
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < items.size(); i++) {
            try {
                JSONObject jsonObject = new JSONObject(items.get(i).toString());
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        error = updateMaterialList(jsonArray);
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
