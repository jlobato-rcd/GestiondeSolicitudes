package com.rcdhotels.gestiondesolicitudes.services;

import android.annotation.SuppressLint;
import android.util.Base64;
import android.util.Log;

import com.rcdhotels.gestiondesolicitudes.model.Material;

import com.rcdhotels.gestiondesolicitudes.model.Request;
import com.rcdhotels.gestiondesolicitudes.model.Warehouse;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import javax.xml.parsers.DocumentBuilderFactory;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;
import static com.rcdhotels.gestiondesolicitudes.connection.ConnectionConfig.catalma;
import static com.rcdhotels.gestiondesolicitudes.connection.ConnectionConfig.cataprod;
import static com.rcdhotels.gestiondesolicitudes.connection.ConnectionConfig.extdev;
import static com.rcdhotels.gestiondesolicitudes.connection.ConnectionConfig.getClient;
import static com.rcdhotels.gestiondesolicitudes.connection.ConnectionConfig.host;
import static com.rcdhotels.gestiondesolicitudes.connection.ConnectionConfig.wsUserPass;
import static com.rcdhotels.gestiondesolicitudes.model.UtilsClass.user;

public class WebServices {

    public static ArrayList<Warehouse> GetWarehouseCatalog(){

        ArrayList<Warehouse> warehouses = null;
        String url = host + catalma;
        try {
            OkHttpClient client = getClient();
            String requestXML = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:sap-com:document:sap:soap:functions:mc-style\">\n" +
                    "   <soapenv:Header/>\n" +
                    "   <soapenv:Body>\n" +
                    "      <urn:ZppCatalmaDev>\n" +
                    "         <Catalogo>\n" +
                    "            <!--Zero or more repetitions:-->\n" +
                    "            <item>\n" +
                    "               <StgeLoc></StgeLoc>\n" +
                    "               <Lgobe></Lgobe>\n" +
                    "               <Zsumi></Zsumi>\n" +
                    "               <Stock0></Stock0>\n" +
                    "               <Conf></Conf>\n" +
                    "               <StgeLocType></StgeLocType>\n" +
                    "            </item>\n" +
                    "         </Catalogo>\n" +
                    "         <Werks>"+user.getHotel().getIdSociety()+"</Werks>\n" +
                    "      </urn:ZppCatalmaDev>\n" +
                    "   </soapenv:Body>\n" +
                    "</soapenv:Envelope>";

            MediaType mediaType = MediaType.parse("text/xml; charset=utf-8");
            RequestBody body = RequestBody.create(mediaType, requestXML);

            String basicAuth = "Basic " + new String(Base64.encode(wsUserPass.getBytes(), 0));
            basicAuth = basicAuth.substring(0, basicAuth.length() - 1);

            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("Content-Type", "text/xml; charset=utf-8")
                    .addHeader("Authorization", basicAuth)
                    .build();
            Response response = client.newCall(request).execute();
            String responseXML = response.body().string();
            Log.i(TAG, "Response: " + responseXML);
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(responseXML)));

            NodeList nodeListCatalog = document.getElementsByTagName("Catalogo").item(0).getChildNodes();
            warehouses = new ArrayList<>();
            for (int i = 0; i < nodeListCatalog.getLength(); i++){
                Element element = (Element) nodeListCatalog.item(i);
                Warehouse warehouse = new Warehouse();
                warehouse.setStgeLoc(element.getElementsByTagName("StgeLoc").item(0).getTextContent());
                warehouse.setLgobe(element.getElementsByTagName("Lgobe").item(0).getTextContent());
                warehouse.setZsumi(element.getElementsByTagName("Zsumi").item(0).getTextContent());
                warehouse.setStock0(element.getElementsByTagName("Stock0").item(0).getTextContent());
                warehouse.setConf(element.getElementsByTagName("Conf").item(0).getTextContent());
                warehouse.setStgeLocType(element.getElementsByTagName("StgeLocType").item(0).getTextContent());
                if (!warehouse.getStgeLoc().equalsIgnoreCase(""))
                    warehouses.add(warehouse);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return warehouses;
    }

    public static ArrayList<Material> GetMaterialsCatalog(String StgeLocSumi, String Mtart){

        ArrayList<Material> materials = new ArrayList<>();
        String url = host + cataprod;
        try {
            OkHttpClient client = getClient();
            String requestXML = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:sap-com:document:sap:soap:functions:mc-style\">\n" +
                    "   <soapenv:Header/>\n" +
                    "   <soapenv:Body>\n" +
                    "      <urn:ZppCatalproDev>\n" +
                    "         <Catalogo>\n" +
                    "            <!--Zero or more repetitions:-->\n" +
                    "            <item>\n" +
                    "               <Plant></Plant>\n" +
                    "               <StgeLoc></StgeLoc>\n" +
                    "               <Material></Material>\n" +
                    "               <Maktx></Maktx>\n" +
                    "               <Labst></Labst>\n" +
                    "               <EntryUom></EntryUom>\n" +
                    "               <Verpr></Verpr>\n" +
                    "               <Mtart></Mtart>\n" +
                    "            </item>\n" +
                    "         </Catalogo>\n" +
                    "         <Mtart>"+Mtart+"</Mtart>\n" +
                    "         <Plant>"+user.getHotel().getIdSociety()+"</Plant>\n" +
                    "         <StgeLoc>"+user.getWarehouse()+"</StgeLoc>\n" +
                    "         <StgeLocSumi>"+StgeLocSumi+"</StgeLocSumi>\n" +
                    "         <Ztype>1</Ztype>\n" +
                    "      </urn:ZppCatalproDev>\n" +
                    "   </soapenv:Body>\n" +
                    "</soapenv:Envelope>";

            MediaType mediaType = MediaType.parse("text/xml; charset=utf-8");
            RequestBody body = RequestBody.create(mediaType, requestXML);

            String basicAuth = "Basic " + new String(Base64.encode(wsUserPass.getBytes(), 0));
            basicAuth = basicAuth.substring(0, basicAuth.length() - 1);

            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("Content-Type", "text/xml; charset=utf-8")
                    .addHeader("Authorization", basicAuth)
                    .build();
            Response response = client.newCall(request).execute();
            String responseXML = response.body().string();
            Log.i(TAG, "Response: " + responseXML);
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(responseXML)));

            NodeList nodeListCatalog = document.getElementsByTagName("Catalogo").item(0).getChildNodes();
            for (int i = 0; i < nodeListCatalog.getLength(); i++){
                Element element = (Element) nodeListCatalog.item(i);
                if (!element.getElementsByTagName("Material").item(0).getTextContent().isEmpty()){
                    Material material = new Material();
                    material.setMATERIAL(Integer.parseInt(element.getElementsByTagName("Material").item(0).getTextContent()));
                    material.setMAKTX(element.getElementsByTagName("Maktx").item(0).getTextContent());
                    material.setVERPR(Float.parseFloat(element.getElementsByTagName("Verpr").item(0).getTextContent()));
                    material.setSTGE_LOC(element.getElementsByTagName("StgeLoc").item(0).getTextContent());
                    material.setLABST(Float.parseFloat(element.getElementsByTagName("Labst").item(0).getTextContent()));
                    material.setENTRY_UOM(element.getElementsByTagName("EntryUom").item(0).getTextContent());
                    material.setMTART(element.getElementsByTagName("Mtart").item(0).getTextContent());
                    materials.add(material);
                }
            }
            Collections.sort(materials,(p1, p2) -> p1.getMAKTX().compareTo(p2.getMAKTX()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return materials;
    }

    public static ArrayList<Material> GetRetMaterialsOnStock(String Mtart){

        ArrayList<Material> materials = new ArrayList<>();
        String url = host + cataprod;
        try {
            OkHttpClient client = getClient();
            String requestXML = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:sap-com:document:sap:soap:functions:mc-style\">\n" +
                    "   <soapenv:Header/>\n" +
                    "   <soapenv:Body>\n" +
                    "      <urn:ZppCatalproDev>\n" +
                    "         <Catalogo>\n" +
                    "            <!--Zero or more repetitions:-->\n" +
                    "            <item>\n" +
                    "               <Plant></Plant>\n" +
                    "               <StgeLoc></StgeLoc>\n" +
                    "               <Material></Material>\n" +
                    "               <Maktx></Maktx>\n" +
                    "               <Labst></Labst>\n" +
                    "               <EntryUom></EntryUom>\n" +
                    "               <Verpr></Verpr>\n" +
                    "               <Mtart></Mtart>\n" +
                    "            </item>\n" +
                    "         </Catalogo>\n" +
                    "         <Mtart>"+Mtart+"</Mtart>\n" +
                    "         <Plant>"+user.getHotel().getIdSociety()+"</Plant>\n" +
                    "         <StgeLoc>"+user.getWarehouse()+"</StgeLoc>\n" +
                    "         <StgeLocSumi></StgeLocSumi>\n" +
                    "         <Ztype>0</Ztype>\n" +
                    "      </urn:ZppCatalproDev>\n" +
                    "   </soapenv:Body>\n" +
                    "</soapenv:Envelope>";

            MediaType mediaType = MediaType.parse("text/xml; charset=utf-8");
            RequestBody body = RequestBody.create(mediaType, requestXML);

            String basicAuth = "Basic " + new String(Base64.encode(wsUserPass.getBytes(), 0));
            basicAuth = basicAuth.substring(0, basicAuth.length() - 1);

            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("Content-Type", "text/xml; charset=utf-8")
                    .addHeader("Authorization", basicAuth)
                    .build();
            Response response = client.newCall(request).execute();
            String responseXML = response.body().string();
            Log.i(TAG, "Response: " + responseXML);
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(responseXML)));

            NodeList nodeListCatalog = document.getElementsByTagName("Catalogo").item(0).getChildNodes();
            for (int i = 0; i < nodeListCatalog.getLength(); i++){
                Element element = (Element) nodeListCatalog.item(i);
                if (!element.getElementsByTagName("Material").item(0).getTextContent().isEmpty() && Float.parseFloat(element.getElementsByTagName("Labst").item(0).getTextContent()) > 0){
                    Material material = new Material();
                    material.setMATERIAL(Integer.parseInt(element.getElementsByTagName("Material").item(0).getTextContent()));
                    material.setMAKTX(element.getElementsByTagName("Maktx").item(0).getTextContent());
                    material.setVERPR(Float.parseFloat(element.getElementsByTagName("Verpr").item(0).getTextContent()));
                    material.setSTGE_LOC(element.getElementsByTagName("StgeLoc").item(0).getTextContent());
                    material.setLABST(Float.parseFloat(element.getElementsByTagName("Labst").item(0).getTextContent()));
                    material.setENTRY_UOM(element.getElementsByTagName("EntryUom").item(0).getTextContent());
                    materials.add(material);
                }
            }
            Collections.sort(materials,(p1, p2) -> p1.getMAKTX().compareTo(p2.getMAKTX()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return materials;
    }

    @SuppressLint("SimpleDateFormat")
    public static Request processRequest(Request request) throws NumberFormatException{
        String url = host + extdev;
        try {
            OkHttpClient client = getClient();
            String requestXML = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:sap-com:document:sap:soap:functions:mc-style\">\n" +
                    "   <soap:Header/>\n" +
                    "   <soap:Body>\n" +
                    "      <urn:ZppExtracDev>\n" +
                    "         <Return>\n" +
                    "            <!--Zero or more repetitions:-->\n" +
                    "            <item>\n" +
                    "               <Tipomens></Tipomens>\n" +
                    "               <RefDocNo></RefDocNo>\n" +
                    "               <Ztype></Ztype>\n" +
                    "               <Posnr></Posnr>\n" +
                    "               <Plant></Plant>\n" +
                    "               <Material></Material>\n" +
                    "               <EntryUom></EntryUom>\n" +
                    "               <MoveStloc></MoveStloc>\n" +
                    "               <StgeLoc></StgeLoc>\n" +
                    "               <HeaderTxt></HeaderTxt>\n" +
                    "               <PstngDate></PstngDate>\n" +
                    "               <EntryQnt></EntryQnt>\n" +
                    "               <Message></Message>\n" +
                    "               <MatDoc></MatDoc>\n" +
                    "               <DocYear></DocYear>\n" +
                    "            </item>\n" +
                    "         </Return>\n" +
                    "         <Solextdev>\n";

            if (request.getTYPE() == 1 && request.getCONF() == 1 || request.getCONF() == 2){
                for (int i = 0; i < request.getMaterials().size(); i++) {
                    if(request.getMaterials().get(i).getPROCESSED() == 0 && request.getMaterials().get(i).getSTATUS_CONF() == 1 && request.getMaterials().get(i).getREQ_QNT() == request.getMaterials().get(i).getQNT_TO_CONF() && request.getMaterials().get(i).getREQ_QNT() > 0){
                        if (request.getMaterials().get(i).getENTRY_QNT() == 0){
                            request.getMaterials().get(i).setENTRY_QNT(request.getMaterials().get(i).getREQ_QNT());
                        }
                        request.getMaterials().get(i).setPSTNG_DATE(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                        requestXML += "            <item>\n" +
                                "               <RefDocNo>"+request.getIDREQUEST()+"</RefDocNo>\n" +
                                "               <Ztype>"+request.getTYPE()+"</Ztype>\n" +
                                "               <Posnr>"+request.getMaterials().get(i).getPOSNR()+"</Posnr>\n" +
                                "               <Plant>"+request.getPLANT()+"</Plant>\n" +
                                "               <Material>"+request.getMaterials().get(i).getMATERIAL()+"</Material>\n" +
                                "               <EntryUom>"+request.getMaterials().get(i).getENTRY_UOM()+"</EntryUom>\n" +
                                "               <MoveStloc>"+request.getMOVE_STLOC()+"</MoveStloc>\n" +
                                "               <StgeLoc>"+request.getSTGE_LOC()+"</StgeLoc>\n" +
                                "               <HeaderTxt>"+request.getHEADER_TXT()+"</HeaderTxt>\n" +
                                "               <PstngDate>"+request.getMaterials().get(i).getPSTNG_DATE().substring(0,10).replace("-","")+"</PstngDate>\n" +
                                "               <EntryQnt>"+request.getMaterials().get(i).getENTRY_QNT()+"</EntryQnt>\n" +
                                "            </item>\n";
                    }
                }
            }
            else{
                for (int i = 0; i < request.getMaterials().size(); i++) {
                    if(request.getMaterials().get(i).getPROCESSED() == 0 && request.getMaterials().get(i).getREQ_QNT() > 0){
                        if (request.getMaterials().get(i).getENTRY_QNT() == 0){
                            request.getMaterials().get(i).setENTRY_QNT(request.getMaterials().get(i).getREQ_QNT());
                        }
                        request.getMaterials().get(i).setPSTNG_DATE(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                        requestXML += "            <item>\n" +
                                "               <RefDocNo>"+request.getIDREQUEST()+"</RefDocNo>\n" +
                                "               <Ztype>"+request.getTYPE()+"</Ztype>\n" +
                                "               <Posnr>"+request.getMaterials().get(i).getPOSNR()+"</Posnr>\n" +
                                "               <Plant>"+request.getPLANT()+"</Plant>\n" +
                                "               <Material>"+request.getMaterials().get(i).getMATERIAL()+"</Material>\n" +
                                "               <EntryUom>"+request.getMaterials().get(i).getENTRY_UOM()+"</EntryUom>\n" +
                                "               <MoveStloc>"+request.getMOVE_STLOC()+"</MoveStloc>\n" +
                                "               <StgeLoc>"+request.getSTGE_LOC()+"</StgeLoc>\n" +
                                "               <HeaderTxt>"+request.getHEADER_TXT()+"</HeaderTxt>\n" +
                                "               <PstngDate>"+request.getMaterials().get(i).getPSTNG_DATE().substring(0,10).replace("-","")+"</PstngDate>\n" +
                                "               <EntryQnt>"+request.getMaterials().get(i).getENTRY_QNT()+"</EntryQnt>\n" +
                                "            </item>\n";
                    }
                }
            }
            requestXML += "         </Solextdev>\n" +
                          "      </urn:ZppExtracDev>\n" +
                          "   </soap:Body>\n" +
                          "</soap:Envelope>";

            MediaType mediaType = MediaType.parse("text/xml; charset=utf-8");
            RequestBody body = RequestBody.create(mediaType, requestXML);

            String basicAuth = "Basic " + new String(Base64.encode(wsUserPass.getBytes(), 0));
            basicAuth = basicAuth.substring(0, basicAuth.length() - 1);

            okhttp3.Request httpRequest = new okhttp3.Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("Content-Type", "text/xml; charset=utf-8")
                    .addHeader("Authorization", basicAuth)
                    .build();

            Response response = client.newCall(httpRequest).execute();
            String responseXML = response.body().string();
            Log.i(TAG, "Response: " + responseXML);
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(responseXML)));

            NodeList nodeListReturn = document.getElementsByTagName("Return").item(0).getChildNodes();
            for (int i = 1; i < nodeListReturn.getLength(); i++){
                Element element = (Element) nodeListReturn.item(i);
                String Tipomens = element.getElementsByTagName("Tipomens").item(0).getTextContent();
                for (int j = 0; j < request.getMaterials().size(); j++) {
                    if (request.getMaterials().get(j).getMATERIAL() == Integer.parseInt(element.getElementsByTagName("Material").item(0).getTextContent())){
                        if (Tipomens.equalsIgnoreCase("0")){
                            request.getMaterials().get(j).setMATERIALDOCUMENT(element.getElementsByTagName("MatDoc").item(0).getTextContent());
                            request.getMaterials().get(j).setMATDOCUMENTYEAR(Integer.parseInt(element.getElementsByTagName("DocYear").item(0).getTextContent()));
                            request.getMaterials().get(j).setPROCESSED(1);
                        }
                        else{
                            request.getMaterials().get(j).setPROCESSED(2);
                            request.setTEXT(element.getElementsByTagName("Message").item(0).getTextContent());
                            request.setSTATUS(6);
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return request;
    }
}
