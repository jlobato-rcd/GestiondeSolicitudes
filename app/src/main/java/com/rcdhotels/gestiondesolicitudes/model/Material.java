package com.rcdhotels.gestiondesolicitudes.model;

import org.jetbrains.annotations.NotNull;

public class Material {

    private int IDMATERIAL;
    private int MATERIAL;
    private int IDREQUEST;
    private String MAKTX;
    private int POSNR;
    private float REQ_QNT;
    private String ENTRY_UOM;
    private float VERPR;
    private int STATUS_CONF;
    private float QNT_TO_CONF;
    private float ENTRY_QNT;
    private int PROCESSED;
    private String PSTNG_DATE;
    private String MATERIALDOCUMENT;
    private int MATDOCUMENTYEAR;
    private int DELETE;
    private String STGE_LOC;
    private float LABST;
    private boolean isChecked;
    private String MTART;

    public int getIDMATERIAL() {
        return IDMATERIAL;
    }

    public void setIDMATERIAL(int IDMATERIAL) {
        this.IDMATERIAL = IDMATERIAL;
    }

    public int getMATERIAL() {
        return MATERIAL;
    }

    public void setMATERIAL(int MATERIAL) {
        this.MATERIAL = MATERIAL;
    }

    public int getIDREQUEST() {
        return IDREQUEST;
    }

    public void setIDREQUEST(int IDREQUEST) {
        this.IDREQUEST = IDREQUEST;
    }

    public String getMAKTX() {
        return MAKTX;
    }

    public void setMAKTX(String MAKTX) {
        this.MAKTX = MAKTX;
    }

    public int getPOSNR() {
        return POSNR;
    }

    public void setPOSNR(int POSNR) {
        this.POSNR = POSNR;
    }

    public float getREQ_QNT() {
        return REQ_QNT;
    }

    public void setREQ_QNT(float REQ_QNT) {
        this.REQ_QNT = REQ_QNT;
    }

    public String getENTRY_UOM() {
        return ENTRY_UOM;
    }

    public void setENTRY_UOM(String ENTRY_UOM) {
        this.ENTRY_UOM = ENTRY_UOM;
    }

    public float getVERPR() {
        return VERPR;
    }

    public void setVERPR(float VERPR) {
        this.VERPR = VERPR;
    }

    public int getSTATUS_CONF() {
        return STATUS_CONF;
    }

    public void setSTATUS_CONF(int STATUS_CONF) {
        this.STATUS_CONF = STATUS_CONF;
    }

    public float getQNT_TO_CONF() {
        return QNT_TO_CONF;
    }

    public void setQNT_TO_CONF(float QNT_TO_CONF) {
        this.QNT_TO_CONF = QNT_TO_CONF;
    }

    public float getENTRY_QNT() {
        return ENTRY_QNT;
    }

    public void setENTRY_QNT(float ENTRY_QNT) {
        this.ENTRY_QNT = ENTRY_QNT;
    }

    public int getPROCESSED() {
        return PROCESSED;
    }

    public void setPROCESSED(int PROCESSED) {
        this.PROCESSED = PROCESSED;
    }

    public String getPSTNG_DATE() {
        return PSTNG_DATE;
    }

    public void setPSTNG_DATE(String PSTNG_DATE) {
        this.PSTNG_DATE = PSTNG_DATE;
    }

    public String getMATERIALDOCUMENT() {
        return MATERIALDOCUMENT;
    }

    public void setMATERIALDOCUMENT(String MATERIALDOCUMENT) {
        this.MATERIALDOCUMENT = MATERIALDOCUMENT;
    }

    public int getMATDOCUMENTYEAR() {
        return MATDOCUMENTYEAR;
    }

    public void setMATDOCUMENTYEAR(int MATDOCUMENTYEAR) {
        this.MATDOCUMENTYEAR = MATDOCUMENTYEAR;
    }

    public int getDELETE() {
        return DELETE;
    }

    public void setDELETE(int DELETE) {
        this.DELETE = DELETE;
    }

    public float getLABST() {
        return LABST;
    }

    public void setLABST(float LABST) {
        this.LABST = LABST;
    }

    @NotNull
    @Override
    public String toString() {
        return "{\"IDMATERIAL\":" + IDMATERIAL +
                ", \"MATERIAL\":" + MATERIAL +
                ", \"IDREQUEST\":" + IDREQUEST +
                ", \"MAKTX\":\"" + MAKTX + "\"" +
                ", \"POSNR\":" + POSNR +
                ", \"REQ_QNT\":" + REQ_QNT +
                ", \"ENTRY_UOM\":\"" + ENTRY_UOM + "\"" +
                ", \"VERPR\":" + VERPR +
                ", \"STATUS_CONF\":" + STATUS_CONF +
                ", \"QNT_TO_CONF\":" + QNT_TO_CONF +
                ", \"ENTRY_QNT\":" + ENTRY_QNT +
                ", \"PROCESSED\":" + PROCESSED +
                ", \"PSTNG_DATE\":\"" + PSTNG_DATE + "\"" +
                ", \"MATERIALDOCUMENT\":\"" + MATERIALDOCUMENT + "\"" +
                ", \"MTART\":\"" + MTART + "\"" +
                ", \"MATDOCUMENTYEAR\":" + MATDOCUMENTYEAR +
                ", \"LABST\":" + LABST +
                ", \"DELETE\":" + DELETE + "}";
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getSTGE_LOC() {
        return STGE_LOC;
    }

    public void setSTGE_LOC(String STGE_LOC) {
        this.STGE_LOC = STGE_LOC;
    }

    public String getMTART() {
        return MTART;
    }

    public void setMTART(String MTART) {
        this.MTART = MTART;
    }
}