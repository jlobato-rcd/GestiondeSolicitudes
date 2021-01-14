package com.rcdhotels.gestiondesolicitudes.model;

import java.util.ArrayList;

public class UtilsClass {

    public static User user;
    public static boolean enableMenu;
    public static String currentFragment;
    public static ArrayList<Material> materialsArrayList;
    public static Request currentRequest;
    public static ArrayList<Request> requestArrayList;

    public static boolean findMaterial(int material){
        if (currentRequest != null){
            if (currentRequest.getMaterials() != null){
                for (int i = 0; i < currentRequest.getMaterials().size(); i++) {
                    if (currentRequest.getMaterials().get(i).getMATERIAL() == material)
                        return true;
                }
            }
        }
        return false;
    }

    public static void resetUtilsValues(){
        user = null;
        enableMenu = false;
        currentFragment = null;
        materialsArrayList = null;
        currentRequest = null;
    }
}
