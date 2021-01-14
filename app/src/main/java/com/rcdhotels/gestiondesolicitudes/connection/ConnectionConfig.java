package com.rcdhotels.gestiondesolicitudes.connection;

import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

public class ConnectionConfig {

    /*public static String host = "https://187.210.101.19:8012";
    public static String wsUserPass = "userscp:Inicio.01";

    public static String hanaHost = "https://qarcdsaphanag7mizfpy2g.us3.hana.ondemand.com/";
    public static String hanaUserPass = "JLOBATO" + ":" + "..Usuario..01.3";

    public static String catalma = "/sap/bc/srt/rfc/sap/zpp_catalma_dev/200/zpp_catalma_dev/zpp_catalma_bin_dev";
    public static String cataprod = "/sap/bc/srt/rfc/sap/zpp_catalpro_dev/200/zpp_catalpro_dev/zpp_catalpro_bin_dev";
    public static String extdev = "/sap/bc/srt/rfc/sap/zpp_extrac_dev/200/zpp_extrac_dev/zpp_extrac_bin_dev";*/

    public static final String EMAIL = "notificacionesscp@rcdhotels.com";
    public static final String PASSWORD = "S@C1PN0t1F20";

    public static String host = "https://187.210.101.19:8013";
    public static String wsUserPass = "userscp:H4rdH0t3l35.4545";

    public static String hanaHost = "https://rcdsaphanah60fb7be4.us3.hana.ondemand.com/";
    public static String hanaUserPass = "dbexecute" + ":" + "1o4b6z#S0!ElDH5IvwEfxe&41";

    public static String catalma = "/sap/bc/srt/rfc/sap/zpp_catalma_dev/400/zpp_catalma_dev/zpp_catalma_bin_dev";
    public static String cataprod = "/sap/bc/srt/rfc/sap/zpp_catalpro_dev/400/zpp_catalpro_dev/zpp_catalpro_bin_dev";
    public static String extdev = "/sap/bc/srt/rfc/sap/zpp_extrac_dev/400/zpp_extrac_dev/zpp_extrac_bin_dev";

    public static OkHttpClient getClient() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init((KeyStore) null);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
        }
        X509TrustManager trustManager = (X509TrustManager) trustManagers[0];
        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, new TrustManager[] { trustManager }, null);
        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(60, TimeUnit.SECONDS);
        builder.writeTimeout(60, TimeUnit.SECONDS);
        builder.readTimeout(60, TimeUnit.SECONDS);
        builder.hostnameVerifier((hostname, session) -> true);
        builder.sslSocketFactory(sslSocketFactory, trustManager).build();
        OkHttpClient client = builder.build();
        return client;
    }
}
