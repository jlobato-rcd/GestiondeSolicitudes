package com.rcdhotels.gestiondesolicitudes.task;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.rcdhotels.gestiondesolicitudes.R;
import com.rcdhotels.gestiondesolicitudes.model.Hotel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import static com.rcdhotels.gestiondesolicitudes.connection.ConnectionConfig.hanaHost;
import static com.rcdhotels.gestiondesolicitudes.database.HotelsTableQuerys.InsertHotels;
import static com.rcdhotels.gestiondesolicitudes.services.XSJSServices.getProperties;

public class GetHotelsAsyncTask extends AsyncTask<Void, Void, Void> {

    @SuppressLint("StaticFieldLeak")
    private Context context;
    private ArrayList<Hotel> hotels;

    public GetHotelsAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... args) {
        hotels = getProperties(context);
        if (hotels != null)
            InsertHotels(hotels, context);
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {

        Spinner hotelsSpinner = ((Activity)context).findViewById(R.id.spinnerHotels);
        if(hotels != null && !hotels.isEmpty()) {
            hotelsSpinner.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, hotels));
            LinearLayout linearLayout = ((Activity) context).findViewById(R.id.linearLayoutLogin);
            linearLayout.setVisibility(View.VISIBLE);
            linearLayout.setAlpha(0.0f);
            linearLayout.animate().setDuration(2000).alpha(1.0f);
        }
        else{
            Toast.makeText(context, R.string.connection_failed ,Toast.LENGTH_SHORT).show();
            ((Activity) context).finish();
        }
    }
}
