package com.rcdhotels.gestiondesolicitudes.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class PreferencesLanguages {

    public static String getLanguage(Context context){
        SharedPreferences settings = context.getSharedPreferences("Preferences_GS", MODE_PRIVATE);
        String pref = settings.getString("LANG", "");
        String lang;
        if (pref.equalsIgnoreCase("es")) {
            lang = "_ES";
        }
        else {
            lang = "_EN";
        }
        return lang;
    }

    public static void setLanguage(Context context, String lang){
        SharedPreferences.Editor editor = context.getSharedPreferences("Preferences_GS", MODE_PRIVATE).edit();
        editor.putString("LANG", lang);
        editor.apply();
    }

    public static void checkLanguage(String lang, Context context) {
        final Configuration config = context.getResources().getConfiguration();
        if (!lang.isEmpty()) {
            if (lang.equalsIgnoreCase("_ES")){
                Locale locale = new Locale("es");
                Locale.setDefault(locale);
                config.locale = locale;
                context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
            }
            else{
                Locale locale = new Locale("en");
                Locale.setDefault(locale);
                config.locale = locale;
                context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
            }
        }
    }
}
