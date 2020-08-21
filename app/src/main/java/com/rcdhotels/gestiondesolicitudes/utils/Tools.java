package com.rcdhotels.gestiondesolicitudes.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.ColorRes;

import com.rcdhotels.gestiondesolicitudes.R;
import com.rcdhotels.gestiondesolicitudes.connection.ConnectionConfig;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static androidx.core.content.ContextCompat.startActivity;

public class Tools {

    public static void setSystemBarColor(Activity act, @ColorRes int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = act.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(act.getResources().getColor(color));
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static boolean sendEmail(String emails, String mSubject, String mMessage, Context context) {

        emails = emails.substring(1, emails.length() -1);
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session mSession = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ConnectionConfig.EMAIL, ConnectionConfig.PASSWORD);
            }
        });
        try {
            MimeMessage mm = new MimeMessage(mSession);
            mm.setFrom(new InternetAddress(ConnectionConfig.EMAIL, context.getString(R.string.app_name)));
            String[] recipientList = emails.split(",");
            InternetAddress[] recipientAddress = new InternetAddress[recipientList.length];
            for (int i = 0; i < recipientList.length; i++) {
                recipientAddress[i] = new InternetAddress(recipientList[i].trim());
            }
            mm.addRecipients(Message.RecipientType.TO, recipientAddress);
            mm.setSubject(mSubject);
            mMessage = context.getString(R.string.body_email_start) + mMessage + context.getString(R.string.body_email_end);
            mm.setText(mMessage);
            Transport.send(mm);
        } catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
