package com.rcdhotels.gestiondesolicitudes.model;

public class Warehouse {

    private String StgeLoc;
    private String Lgobe;
    private String Zsumi;
    private String Stock0;
    private String Conf;

    public String getStgeLoc() {
        return StgeLoc;
    }

    public void setStgeLoc(String stgeLoc) {
        StgeLoc = stgeLoc;
    }

    public String getLgobe() {
        return Lgobe;
    }

    public void setLgobe(String lgobe) {
        Lgobe = lgobe;
    }

    public String getZsumi() {
        return Zsumi;
    }

    public void setZsumi(String zsumi) {
        Zsumi = zsumi;
    }

    public String getStock0() {
        return Stock0;
    }

    public void setStock0(String stock0) {
        Stock0 = stock0;
    }

    public String getConf() {
        return Conf;
    }

    public void setConf(String conf) {
        Conf = conf;
    }

    public Warehouse(String stgeLoc, String lgobe, String zsumi, String stock0, String conf) {
        StgeLoc = stgeLoc;
        Lgobe = lgobe;
        Zsumi = zsumi;
        Stock0 = stock0;
        Conf = conf;
    }

    public Warehouse() {
    }

    @Override
    public String toString() {
        return Lgobe;
    }
}
