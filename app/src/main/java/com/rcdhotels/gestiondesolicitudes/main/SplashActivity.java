package com.rcdhotels.gestiondesolicitudes.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.rcdhotels.gestiondesolicitudes.BuildConfig;
import com.rcdhotels.gestiondesolicitudes.R;
import com.rcdhotels.gestiondesolicitudes.dbhelper.Request_Management;
import com.rcdhotels.gestiondesolicitudes.login.LoginActivity;

import static com.rcdhotels.gestiondesolicitudes.database.WarehouseTableQuerys.SQL_DELETE_WAREHOUSE_TABLE;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TextView textViewVersion = findViewById(R.id.textViewVersion);
        textViewVersion.setText(BuildConfig.VERSION_NAME);
        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(3000);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
                finally {
                    if (isLogged()){
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                        finish();
                    }
                    else{
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                        finish();
                    }
                }
            }
        };
        timerThread.start();
    }

    public boolean isLogged(){
        SharedPreferences prefs = SplashActivity.this.getSharedPreferences("Preferences_GS", Context.MODE_PRIVATE);
        try{
            if (prefs.getBoolean("isLogged", false)) return true;
            else return false;
        }
        catch (Exception ex){

        }
        return false;
    }
}
