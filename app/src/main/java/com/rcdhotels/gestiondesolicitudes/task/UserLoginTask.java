package com.rcdhotels.gestiondesolicitudes.task;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.rcdhotels.gestiondesolicitudes.R;
import com.rcdhotels.gestiondesolicitudes.model.User;

import static android.content.Context.MODE_PRIVATE;
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
        if (user != null){
            long newRowId = InsertUser(context);
            if (newRowId != -1) {
                SharedPreferences.Editor editor = context.getSharedPreferences("Preferences_GS", MODE_PRIVATE).edit();
                editor.putBoolean("isLogged", true);
                editor.apply();
            }
            user.setHotel(getHotel(context, user.getHotel().getIdHotel()));
        }
        return user;
    }

    @Override
    protected void onPostExecute(User user) {
        super.onPostExecute(user);
        if (user == null){
            Toast.makeText(context, R.string.user_not_found,Toast.LENGTH_SHORT).show();
        }
        else if(user.getRole() == null){
            Toast.makeText(context, R.string.user_has_no_permissions,Toast.LENGTH_SHORT).show();
        }
        else if(user.getWarehouse() == null && !user.getRole().equalsIgnoreCase("GS_AUTOR2") && !user.getRole().equalsIgnoreCase("GS_AUTOR3")){
            Toast.makeText(context, R.string.user_has_no_warehouse,Toast.LENGTH_SHORT).show();
        }
        else{
            new GetWarehouseCatalogAsyncTask(context).execute();
        }
    }
}
