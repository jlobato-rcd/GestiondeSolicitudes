package com.rcdhotels.gestiondesolicitudes.model;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Request {

    private int IDREQUEST;
    private int TYPE;
    private int PLANT;
    private String HEADER_TXT;
    private String REQ_USER;
    private String CREATED_DATE;
    private String MOVE_STLOC;
    private String STGE_LOC;
    private int STATUS;
    private int RELEASE;
    private int CONF;
    private String TEXT;
    private float TOTAL_VERPR;
    private String MAT_TYPE;
    private ArrayList<Material> materials = new ArrayList<>();

    public int getIDREQUEST() {
        return IDREQUEST;
    }

    public void setIDREQUEST(int IDREQUEST) {
        this.IDREQUEST = IDREQUEST;
    }

    public int getTYPE() {
        return TYPE;
    }

    public void setTYPE(int TYPE) {
        this.TYPE = TYPE;
    }

    public int getPLANT() {
        return PLANT;
    }

    public void setPLANT(int PLANT) {
        this.PLANT = PLANT;
    }

    public String getHEADER_TXT() {
        return HEADER_TXT;
    }

    public void setHEADER_TXT(String HEADER_TXT) {
        this.HEADER_TXT = HEADER_TXT;
    }

    public String getREQ_USER() {
        return REQ_USER;
    }

    public void setREQ_USER(String REQ_USER) {
        this.REQ_USER = REQ_USER;
    }

    public String getMOVE_STLOC() {
        return MOVE_STLOC;
    }

    public void setMOVE_STLOC(String MOVE_STLOC) {
        this.MOVE_STLOC = MOVE_STLOC;
    }

    public String getSTGE_LOC() {
        return STGE_LOC;
    }

    public void setSTGE_LOC(String STGE_LOC) {
        this.STGE_LOC = STGE_LOC;
    }

    public String getCREATED_DATE() {
        return CREATED_DATE;
    }

    public void setCREATED_DATE(String CREATED_DATE) {
        this.CREATED_DATE = CREATED_DATE;
    }

    public int getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(int STATUS) {
        this.STATUS = STATUS;
    }

    public int getRELEASE() {
        return RELEASE;
    }

    public void setRELEASE(int RELEASE) {
        this.RELEASE = RELEASE;
    }

    public int getCONF() {
        return CONF;
    }

    public void setCONF(int CONF) {
        this.CONF = CONF;
    }

    public String getTEXT() {
        return TEXT;
    }

    public void setTEXT(String TEXT) {
        this.TEXT = TEXT;
    }

    public float getTOTAL_VERPR() {
        return TOTAL_VERPR;
    }

    public void setTOTAL_VERPR(float TOTAL_VERPR) {
        this.TOTAL_VERPR = TOTAL_VERPR;
    }

    public Request() {
    }

    @NotNull
    @Override
    public String toString() {
        return "{\"IDREQUEST\":" + IDREQUEST +
                ", \"TYPE\":" + TYPE +
                ", \"PLANT\":" + PLANT +
                ", \"HEADER_TXT\":\"" + HEADER_TXT + "\"" +
                ", \"REQ_USER\":\"" + REQ_USER + "\"" +
                ", \"CREATED_DATE\":\"" + CREATED_DATE + "\"" +
                ", \"MOVE_STLOC\":\"" + MOVE_STLOC + "\"" +
                ", \"STGE_LOC\":\"" + STGE_LOC + "\"" +
                ", \"STATUS\":" + STATUS +
                ", \"RELEASE\":" + RELEASE +
                ", \"CONF\":" + CONF +
                ", \"TOTAL_VERPR\":" + TOTAL_VERPR +
                ", \"MAT_TYPE\":\"" + MAT_TYPE + "\"" +
                ", \"TEXT\":\"" + TEXT + "\"}";
    }

    public ArrayList<Material> getMaterials() {
        return materials;
    }

    public void setMaterials(ArrayList<Material> materials) {
        this.materials = materials;
    }

    public String getMAT_TYPE() {
        return MAT_TYPE;
    }

    public void setMAT_TYPE(String MAT_TYPE) {
        this.MAT_TYPE = MAT_TYPE;
    }
}