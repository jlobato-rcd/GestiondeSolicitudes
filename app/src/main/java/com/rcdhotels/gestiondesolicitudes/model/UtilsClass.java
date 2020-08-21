package com.rcdhotels.gestiondesolicitudes.model;

import java.util.ArrayList;

public class UtilsClass {

    public static User user;
    public static boolean enableMenu;
    public static String currentFragment;
    public static ArrayList<Material> materialsArrayList;
    public static ArrayList<Material> materialsToProcess;
    public static Request currentRequest;

    public static boolean findMaterial(int material){
        if (materialsToProcess != null){
            for (int i = 0; i < materialsToProcess.size(); i++) {
                if (materialsToProcess.get(i).getMATERIAL() == material)
                    return true;
            }
        }
        return false;
    }
}
