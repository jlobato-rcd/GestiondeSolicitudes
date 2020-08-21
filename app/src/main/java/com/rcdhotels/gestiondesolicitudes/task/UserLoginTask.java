package com.rcdhotels.gestiondesolicitudes.task;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Base64;
import android.widget.Toast;

import com.rcdhotels.gestiondesolicitudes.R;
import com.rcdhotels.gestiondesolicitudes.main.MainActivity;
import com.rcdhotels.gestiondesolicitudes.model.User;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import static android.content.Context.MODE_PRIVATE;
import static com.rcdhotels.gestiondesolicitudes.connection.ConnectionConfig.hanaHost;
import static com.rcdhotels.gestiondesolicitudes.connection.ConnectionConfig.hanaUserPass;
import static com.rcdhotels.gestiondesolicitudes.database.HotelsTableQuerys.getHotel;
import static com.rcdhotels.gestiondesolicitudes.database.UserTableQuerys.InsertUser;
import static com.rcdhotels.gestiondesolicitudes.model.UtilsClass.user;
import static com.rcdhotels.gestiondesolicitudes.services.XSJSServices.LoginUser;

public class UserLoginTask extends AsyncTask<Void, Void, User> {

    private String userName;
    private String password;
    private String idHotel;
    @SuppressLint("StaticFieldLeak")
    private Context context;
    private String mMessage;

    public UserLoginTask(String userName, String password, String idHotel, Context context) {
        this.userName = userName;
        this.password = password;
        this.idHotel = idHotel;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected User doInBackground(Void... voids) {
        user = LoginUser(userName, password, idHotel);
        long newRowId = InsertUser(context);
        if (newRowId != -1) {
            SharedPreferences.Editor editor = context.getSharedPreferences("Preferences_GS", MODE_PRIVATE).edit();
            editor.putBoolean("isLogged", true);
            editor.apply();
        }
        user.setHotel(getHotel(context, user.getHotel().getIdHotel()));
        return user;
    }

    @Override
    protected void onPostExecute(User user) {
        super.onPostExecute(user);

        if (mMessage != null){
            Toast.makeText(context, mMessage,Toast.LENGTH_SHORT).show();
        }
        else if(user == null || (user != null && user.getRole() == null)){
            Toast.makeText(context, R.string.login_error,Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context, R.string.welcome,Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            ((Activity) context).finish();
        }
    }
}
