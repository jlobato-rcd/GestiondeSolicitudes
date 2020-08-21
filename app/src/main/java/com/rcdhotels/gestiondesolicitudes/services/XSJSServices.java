package com.rcdhotels.gestiondesolicitudes.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.rcdhotels.gestiondesolicitudes.R;
import com.rcdhotels.gestiondesolicitudes.model.Authorization;
import com.rcdhotels.gestiondesolicitudes.model.Hotel;
import com.rcdhotels.gestiondesolicitudes.model.Material;
import com.rcdhotels.gestiondesolicitudes.model.Request;
import com.rcdhotels.gestiondesolicitudes.model.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.rcdhotels.gestiondesolicitudes.connection.ConnectionConfig.getClient;
import static com.rcdhotels.gestiondesolicitudes.connection.ConnectionConfig.hanaHost;
import static com.rcdhotels.gestiondesolicitudes.connection.ConnectionConfig.hanaUserPass;
import static com.rcdhotels.gestiondesolicitudes.database.HotelsTableQuerys.getHotel;
import static com.rcdhotels.gestiondesolicitudes.database.UserTableQuerys.InsertUser;
import static com.rcdhotels.gestiondesolicitudes.model.UtilsClass.user;

public class XSJSServices {

    public static ArrayList<Hotel> getProperties(Context context) {
        ArrayList<Hotel> hotels = null;
        try {
            String url =hanaHost + "genericos/INFHOTELS.xsjs";

            OkHttpClient client = getClient();
            String basicAuth = "Basic " + new String(Base64.encode(hanaUserPass.getBytes(), 0));
            basicAuth = basicAuth.substring(0, basicAuth.length() - 1);

            okhttp3.Request httpRequest = new okhttp3.Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .addHeader("Authorization", basicAuth)
                    .build();

            Response response = client.newCall(httpRequest).execute();
            String responseXML = response.body().string();

            JSONArray jsonarray = new JSONArray(responseXML);

            hotels = new ArrayList<>();
            Hotel h = new Hotel();
            h.setIdHotel("SELECT");
            h.setNameHotel(context.getString(R.string.select));
            hotels.add(h);

            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                Hotel hotel = new Hotel();
                hotel.setIdHotel(jsonobject.getString("IDHOTEL"));
                hotel.setNameHotel(jsonobject.getString("NAME"));
                hotel.setAddressHotel(jsonobject.getString("ADDRESS"));
                hotel.setPhone(jsonobject.getString("PHONE"));
                hotel.setEmail(jsonobject.getString("EMAIL"));
                hotel.setCodigoLogin(jsonobject.getString("CODIGOLOGIN"));
                if (!jsonobject.getString("IDSOCIETY").isEmpty())
                    hotel.setIdSociety(Integer.parseInt(jsonobject.getString("IDSOCIETY")));
                hotels.add(hotel);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return hotels;
    }

    public static User LoginUser(String userName, String password, String idHotel){

        try {
            String url = hanaHost + "Food_Processing/INFUSER.xsjs?userName="+userName+"&password="+password+"&idHotel="+idHotel+"&idSistema=PA";

            OkHttpClient client = getClient();
            String basicAuth = "Basic " + new String(Base64.encode(hanaUserPass.getBytes(), 0));
            basicAuth = basicAuth.substring(0, basicAuth.length() - 1);

            okhttp3.Request httpRequest = new okhttp3.Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .addHeader("Authorization", basicAuth)
                    .build();

            Response response = client.newCall(httpRequest).execute();
            String responseXML = response.body().string();

            JSONObject jsonObject = new JSONObject(responseXML);
            if (jsonObject != null){
                user = new User();
                user.getHotel().setIdHotel(idHotel);
                user.setIdUser(jsonObject.getString("idUser"));
                user.setIdCollaborator(jsonObject.getString("idCollaborator"));
                user.setUserName(jsonObject.getString("userName"));
                user.setPassword(jsonObject.getString("password"));
                user.setEmail(jsonObject.getString("email"));
                user.setFirstname(jsonObject.getString("firstName"));
                user.setSecondName(jsonObject.getString("secondName"));
                user.setSurName(jsonObject.getString("surName"));
                user.setSecondSurName(jsonObject.getString("secondSurName"));
                user.setActive(jsonObject.getString("active"));
                user.setCreatedDate(jsonObject.getString("createdDate"));
                user.setRole(jsonObject.getString("role"));
                user.setWarehouse(jsonObject.getString("warehouse"));
                if (user.getWarehouse().equalsIgnoreCase("null")){
                    user.setWarehouse("ACYP");
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public static Request insertRequest(Request request){

        try {
            String url = hanaHost + "/Request_Management/INSERTREQUEST.xsjs";
            OkHttpClient client = getClient();
            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(mediaType, request.toString());

            String basicAuth = "Basic " + new String(Base64.encode(hanaUserPass.getBytes(), 0));
            basicAuth = basicAuth.substring(0, basicAuth.length() - 1);

            okhttp3.Request httpRequest = new okhttp3.Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .addHeader("Authorization", basicAuth)
                    .build();

            Response response = client.newCall(httpRequest).execute();
            String responseXML = response.body().string();
            request.setIDREQUEST(Integer.parseInt(responseXML));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return request;
    }

    public static int updateRequest(Request request){

        int affectedRow = 0;
        try {
            String url = hanaHost + "/Request_Management/UPDATEREQUEST.xsjs";
            OkHttpClient client = getClient();
            //OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(mediaType, request.toString());

            String basicAuth = "Basic " + new String(Base64.encode(hanaUserPass.getBytes(), 0));
            basicAuth = basicAuth.substring(0, basicAuth.length() - 1);

            okhttp3.Request httpRequest = new okhttp3.Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .addHeader("Authorization", basicAuth)
                    .build();

            Response response = client.newCall(httpRequest).execute();
            String responseXML = response.body().string();
            affectedRow = Integer.parseInt(responseXML);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return affectedRow;
    }

    public static int authorizeRequest(int status, int release, int idRequest){

        int affectedRow = 0;
        try {
            String url = hanaHost + "/Request_Management/AUTHORIZEREQUEST.xsjs?status="+status+"&release="+release+"&idrequest="+idRequest;
            OkHttpClient client = getClient();
            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(mediaType, "");

            String basicAuth = "Basic " + new String(Base64.encode(hanaUserPass.getBytes(), 0));
            basicAuth = basicAuth.substring(0, basicAuth.length() - 1);

            okhttp3.Request httpRequest = new okhttp3.Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .addHeader("Authorization", basicAuth)
                    .build();

            Response response = client.newCall(httpRequest).execute();
            String responseXML = response.body().string();
            affectedRow = Integer.parseInt(responseXML);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return affectedRow;
    }

    public static int rejectRequest(int status, String reasons , int idRequest){

        int affectedRow = 0;
        try {
            String url = hanaHost + "/Request_Management/REJECTREQUEST.xsjs?status="+status+"&reasons="+reasons+"&idrequest="+idRequest;
            OkHttpClient client = getClient();
            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(mediaType, "");

            String basicAuth = "Basic " + new String(Base64.encode(hanaUserPass.getBytes(), 0));
            basicAuth = basicAuth.substring(0, basicAuth.length() - 1);

            okhttp3.Request httpRequest = new okhttp3.Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .addHeader("Authorization", basicAuth)
                    .build();

            Response response = client.newCall(httpRequest).execute();
            String responseXML = response.body().string();
            affectedRow = Integer.parseInt(responseXML);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return affectedRow;
    }

    public static Request getRequestById(int idRequest){

        Request request = null;
        try {
            String url = hanaHost + "/Request_Management/GETREQUESTBYID.xsjs?idRequest=" + idRequest;
            OkHttpClient client = getClient();
            String basicAuth = "Basic " + new String(Base64.encode(hanaUserPass.getBytes(), 0));
            basicAuth = basicAuth.substring(0, basicAuth.length() - 1);

            okhttp3.Request httpRequest = new okhttp3.Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .addHeader("Authorization", basicAuth)
                    .build();

            Response response = client.newCall(httpRequest).execute();
            String responseXML = response.body().string();

            JSONObject jsonObject = new JSONObject(responseXML);
            if (jsonObject.length() > 0){
                request = new Request();
                request.setIDREQUEST(jsonObject.getInt("IDREQUEST"));
                request.setTYPE(jsonObject.getInt("TYPE"));
                request.setPLANT(jsonObject.getInt("PLANT"));
                request.setMOVE_STLOC(jsonObject.getString("MOVE_STLOC"));
                request.setSTGE_LOC(jsonObject.getString("STGE_LOC"));
                request.setHEADER_TXT(jsonObject.getString("HEADER_TXT"));
                request.setREQ_USER(jsonObject.getString("REQ_USER"));
                request.setCREATED_DATE(jsonObject.getString("CREATED_DATE"));
                request.setSTATUS(jsonObject.getInt("STATUS"));
                request.setRELEASE(jsonObject.getInt("RELEASE"));
                request.setCONF(jsonObject.getInt("CONF"));
                request.setTEXT(jsonObject.getString("TEXT"));
                request.setTOTAL_VERPR((float) jsonObject.getDouble("TOTAL_VERPR"));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return request;
    }

    public static ArrayList<Request> getRequestList(int type, String date, int status){

        ArrayList<Request> requests = null;
        try {
            String url = hanaHost + "/Request_Management/GETREQUESTLIST.xsjs?user="+ user.getUserName()+
                    "&idSociety="+user.getHotel().getIdSociety()+
                    "&warehouse="+user.getWarehouse()+
                    "&type="+type+
                    "&role="+user.getRole()+
                    "&date="+date+
                    "&status="+status;
            OkHttpClient client = getClient();
            String basicAuth = "Basic " + new String(Base64.encode(hanaUserPass.getBytes(), 0));
            basicAuth = basicAuth.substring(0, basicAuth.length() - 1);

            okhttp3.Request httpRequest = new okhttp3.Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .addHeader("Authorization", basicAuth)
                    .build();

            Response response = client.newCall(httpRequest).execute();
            String responseXML = response.body().string();

            JSONArray jsonArray = new JSONArray(responseXML);
            if (jsonArray.length() > 0){
                requests = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Request request = new Request();
                    request.setIDREQUEST(jsonObject.getInt("IDREQUEST"));
                    request.setTYPE(jsonObject.getInt("TYPE"));
                    request.setPLANT(jsonObject.getInt("PLANT"));
                    request.setMOVE_STLOC(jsonObject.getString("MOVE_STLOC"));
                    request.setSTGE_LOC(jsonObject.getString("STGE_LOC"));
                    request.setHEADER_TXT(jsonObject.getString("HEADER_TXT"));
                    request.setREQ_USER(jsonObject.getString("REQ_USER"));
                    request.setCREATED_DATE(jsonObject.getString("CREATED_DATE"));
                    request.setSTATUS(jsonObject.getInt("STATUS"));
                    request.setRELEASE(jsonObject.getInt("RELEASE"));
                    request.setCONF(jsonObject.getInt("CONF"));
                    request.setTEXT(jsonObject.getString("TEXT"));
                    request.setTOTAL_VERPR((float) jsonObject.getDouble("TOTAL_VERPR"));
                    requests.add(request);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return requests;
    }

    public static Material insertMaterial(Material material){

        try {
            String url = hanaHost + "/Request_Management/INSERTMATERIAL.xsjs";
            OkHttpClient client = getClient();
            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(mediaType, material.toString());

            String basicAuth = "Basic " + new String(Base64.encode(hanaUserPass.getBytes(), 0));
            basicAuth = basicAuth.substring(0, basicAuth.length() - 1);

            okhttp3.Request httpRequest = new okhttp3.Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .addHeader("Authorization", basicAuth)
                    .build();

            Response response = client.newCall(httpRequest).execute();
            String responseXML = response.body().string();
            material.setIDMATERIAL(Integer.parseInt(responseXML));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return material;
    }

    /*public static int updateMaterial(Material material){

        int affectedRow = 0;
        try {
            String url = hanaHost + "/Request_Management/UPDATEMATERIAL.xsjs";
            OkHttpClient client = getClient();
            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(mediaType, material.toString());

            String basicAuth = "Basic " + new String(Base64.encode(hanaUserPass.getBytes(), 0));
            basicAuth = basicAuth.substring(0, basicAuth.length() - 1);

            okhttp3.Request httpRequest = new okhttp3.Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .addHeader("Authorization", basicAuth)
                    .build();

            Response response = client.newCall(httpRequest).execute();
            String responseXML = response.body().string();
            affectedRow = Integer.parseInt(responseXML);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return affectedRow;
    }*/

    public static int updateMaterialList(JSONArray jsonArray){

        int error = 0;
        try {
            String url = hanaHost + "/Request_Management/UPDATEMATERIALSLIST.xsjs";
            OkHttpClient client = getClient();
            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(mediaType, jsonArray.toString());

            String basicAuth = "Basic " + new String(Base64.encode(hanaUserPass.getBytes(), 0));
            basicAuth = basicAuth.substring(0, basicAuth.length() - 1);

            okhttp3.Request httpRequest = new okhttp3.Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .addHeader("Authorization", basicAuth)
                    .build();

            Response response = client.newCall(httpRequest).execute();
            String responseXML = response.body().string();
            error = Integer.parseInt(responseXML);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return error;
    }

    public static ArrayList<Material> getResquestMaterialList(int idRequest){

        ArrayList<Material> materials = null;
        try {
            String url = hanaHost + "/Request_Management/GETREQUESTMATERIALS.xsjs?idrequest=" + idRequest;
            OkHttpClient client = getClient();

            String basicAuth = "Basic " + new String(Base64.encode(hanaUserPass.getBytes(), 0));
            basicAuth = basicAuth.substring(0, basicAuth.length() - 1);

            okhttp3.Request httpRequest = new okhttp3.Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .addHeader("Authorization", basicAuth)
                    .build();

            Response response = client.newCall(httpRequest).execute();
            String responseXML = response.body().string();
            JSONArray jsonArray = new JSONArray(responseXML);
            if (jsonArray.length() > 0){
                materials = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Material material = new Material();
                    material.setIDMATERIAL(jsonObject.getInt("IDMATERIAL"));
                    material.setMATERIAL(jsonObject.getInt("MATERIAL"));
                    material.setIDREQUEST(jsonObject.getInt("IDREQUEST"));
                    material.setMAKTX(jsonObject.getString("MAKTX"));
                    material.setPOSNR(jsonObject.getInt("POSNR"));
                    material.setREQ_QNT((float) jsonObject.getDouble("REQ_QNT"));
                    material.setENTRY_UOM(jsonObject.getString("ENTRY_UOM"));
                    material.setVERPR((float) jsonObject.getDouble("VERPR"));
                    material.setSTATUS_CONF(jsonObject.getInt("STATUS_CONF"));
                    material.setQNT_TO_CONF((float) jsonObject.getDouble("QNT_TO_CONF"));
                    material.setENTRY_QNT((float) jsonObject.getDouble("ENTRY_QNT"));
                    material.setPROCESSED(jsonObject.getInt("PROCESSED"));
                    material.setPSTNG_DATE(jsonObject.getString("PSTNG_DATE"));
                    material.setMATERIALDOCUMENT(jsonObject.getString("MATERIALDOCUMENT"));
                    material.setMATDOCUMENTYEAR(jsonObject.getInt("MATDOCUMENTYEAR"));
                    material.setDELETE(jsonObject.getInt("DELETE"));
                    materials.add(material);
                }
                Collections.sort(materials,(p1, p2) -> p1.getMAKTX().compareTo(p2.getMAKTX()));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return materials;
    }

    public static String getEmailsToNotify(String warehouse, String role){

        String responseXML = null;
        try {
            String url = hanaHost + "/Request_Management/GETEMAILTONOTIFY.xsjs?idproperty="+user.getHotel().getIdHotel()+
                    "&warehouse="+warehouse+"&role="+role+"&idsystem=GS" ;
            OkHttpClient client = getClient();
            String basicAuth = "Basic " + new String(Base64.encode(hanaUserPass.getBytes(), 0));
            basicAuth = basicAuth.substring(0, basicAuth.length() - 1);

            okhttp3.Request httpRequest = new okhttp3.Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .addHeader("Authorization", basicAuth)
                    .build();

            Response response = client.newCall(httpRequest).execute();
            responseXML = response.body().string();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return responseXML;
    }

    public static String getEmailUserToNotify(String userName){

        String responseXML = null;
        try {
            String url = hanaHost + "/Request_Management/GETREQUSEREMAIL.xsjs?userName="+userName;
            OkHttpClient client = getClient();
            String basicAuth = "Basic " + new String(Base64.encode(hanaUserPass.getBytes(), 0));
            basicAuth = basicAuth.substring(0, basicAuth.length() - 1);

            okhttp3.Request httpRequest = new okhttp3.Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .addHeader("Authorization", basicAuth)
                    .build();

            Response response = client.newCall(httpRequest).execute();
            responseXML = response.body().string();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return responseXML;
    }

    public static Authorization getAuthorizationLevel(int level){

        Authorization authorization = null;
        try {
            String url = hanaHost + "/Request_Management/GETAUTHORIZATIONLEVELS.xsjs?property=" + user.getHotel().getIdHotel() + "&level=" + level;
            OkHttpClient client = getClient();
            String basicAuth = "Basic " + new String(Base64.encode(hanaUserPass.getBytes(), 0));
            basicAuth = basicAuth.substring(0, basicAuth.length() - 1);

            okhttp3.Request httpRequest = new okhttp3.Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .addHeader("Authorization", basicAuth)
                    .build();

            Response response = client.newCall(httpRequest).execute();
            String responseXML = response.body().string();

            JSONObject jsonObject = new JSONObject(responseXML);
            authorization = new Authorization();
            authorization.setId(jsonObject.getInt("ID"));
            authorization.setLevel(jsonObject.getInt("LEVEL"));
            authorization.setAmount((float) jsonObject.getDouble("AMOUNT"));
            authorization.setProperty(jsonObject.getString("PROPERTY"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return authorization;
    }

}
