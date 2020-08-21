package com.rcdhotels.gestiondesolicitudes.settings;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.rcdhotels.gestiondesolicitudes.R;
import com.rcdhotels.gestiondesolicitudes.adapters.SpinnersLanguagesAdapter;
import com.rcdhotels.gestiondesolicitudes.utils.PreferencesLanguages;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    private Spinner langSpinner;
    private String prefLang;
    private boolean langSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        String lang = PreferencesLanguages.getLanguage(SettingsActivity.this);
        final Configuration config = getBaseContext().getResources().getConfiguration();

        langSpinner = findViewById(R.id.spinnerLanguage);
        String[] languages = getResources().getStringArray(R.array.languages);
        SpinnersLanguagesAdapter spinnersLanguagesAdapter = new SpinnersLanguagesAdapter(this, languages);
        langSpinner.setAdapter(spinnersLanguagesAdapter);

        if (!lang.isEmpty()) {
            if (lang.equalsIgnoreCase("_ES")){
                Locale locale = new Locale("es");
                Locale.setDefault(locale);
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                langSpinner.setSelection(1);
            }
            else{
                Locale locale = new Locale("en");
                Locale.setDefault(locale);
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                langSpinner.setSelection(2);
            }
        }

        langSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(SettingsActivity.this);
                return false;
            }
        });

        langSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 1:
                        prefLang="es";
                        break;
                    case 2:
                        prefLang="en";
                        break;
                }
                if(langSelected == true){
                    if(prefLang!=null) {
                        Locale locale = new Locale(prefLang);
                        Locale.setDefault(locale);
                        Configuration config = new Configuration();
                        config.locale = locale;
                        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                        PreferencesLanguages.setLanguage(SettingsActivity.this, prefLang);
                        langSelected = false;
                        finish();
                        Intent intent = new Intent(SettingsActivity.this, SettingsActivity.class);
                        intent.putExtra("langSelected", langSelected);
                        startActivity(intent);

                    }
                }
                langSelected = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        finish();
    }

    public void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}