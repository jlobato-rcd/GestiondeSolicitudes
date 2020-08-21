package com.rcdhotels.gestiondesolicitudes.login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.rcdhotels.gestiondesolicitudes.R;
import com.rcdhotels.gestiondesolicitudes.adapters.SpinnersLanguagesAdapter;
import com.rcdhotels.gestiondesolicitudes.model.Hotel;
import com.rcdhotels.gestiondesolicitudes.task.GetHotelsAsyncTask;
import com.rcdhotels.gestiondesolicitudes.task.UserLoginTask;
import com.rcdhotels.gestiondesolicitudes.utils.PreferencesLanguages;

import java.util.Locale;

import static com.rcdhotels.gestiondesolicitudes.utils.Tools.hideKeyboard;

public class LoginActivity extends AppCompatActivity {

    private Spinner hotelsSpinner;
    private Hotel hotel;
    private String userName;
    private String password;
    private TextInputEditText textInputEditTextUser;
    private TextInputEditText textInputEditTextPass;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            GetHotelsAsyncTask getHotelsAsyncTask = new GetHotelsAsyncTask(LoginActivity.this);
            getHotelsAsyncTask.execute();

            textInputEditTextUser = findViewById(R.id.textInputEditTextUser);
            textInputEditTextPass = findViewById(R.id.textInputEditTextPass);

            hotelsSpinner = findViewById(R.id.spinnerHotels);
            hotelsSpinner.setOnTouchListener((v, event) -> {
                hideKeyboard(LoginActivity.this);
                return false;
            }) ;

            hotelsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                    // TODO Auto-generated method stub
                    hotel = (Hotel) hotelsSpinner.getItemAtPosition(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                }
            });

            Button buttonLogin = findViewById(R.id.buttonLogin);
            buttonLogin.setOnClickListener(view -> {
                userName = textInputEditTextUser.getText().toString();
                password = textInputEditTextPass.getText().toString();
                if (userName.isEmpty() || password.isEmpty())
                    Toast.makeText(LoginActivity.this, R.string.empty_fields, Toast.LENGTH_SHORT).show();
                else if (hotel.getIdHotel().equalsIgnoreCase("SELECT"))
                    Toast.makeText(LoginActivity.this, R.string.select_property, Toast.LENGTH_SHORT).show();
                else {
                    new UserLoginTask(userName, password, hotel.getIdHotel(), LoginActivity.this).execute();
                }
            });
        }
        else {
            Toast.makeText(LoginActivity.this, R.string.internet_connection_failed, Toast.LENGTH_SHORT).show();
        }
    }
}
