package com.rcdhotels.gestiondesolicitudes.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.rcdhotels.gestiondesolicitudes.model.Hotel;
import com.rcdhotels.gestiondesolicitudes.model.Warehouse;

import java.util.ArrayList;

import static com.rcdhotels.gestiondesolicitudes.database.HotelsTableQuerys.SQL_DELETE_HOTEL_TABLE;
import static com.rcdhotels.gestiondesolicitudes.dbhelper.Request_Management.DbHelper;

public class WarehouseTableQuerys {

    public class WarehouseTable implements BaseColumns {
        public static final String TABLE_NAME_WAREHOUSE = "WAREHOUSE";
        public static final String COLUMN_NAME_STGELOC = "STGELOC";
        public static final String COLUMN_NAME_LGOBE = "LGOBE";
        public static final String COLUMN_NAME_ZSUMI = "ZSUMI";
        public static final String COLUMN_NAME_STOCK0 = "STOCK0";
        public static final String COLUMN_NAME_CONF = "CONF";
        public static final String COLUMN_NAME_STGELOCTYPE = "STGELOCTYPE";
    }
    
    public static final String SQL_CREATE_WAREHOUSE_TABLE =
            "CREATE TABLE " + WarehouseTable.TABLE_NAME_WAREHOUSE + " (" +
                    WarehouseTable._ID + " INTEGER PRIMARY KEY," +
                    WarehouseTable.COLUMN_NAME_STGELOC + " TEXT," +
                    WarehouseTable.COLUMN_NAME_LGOBE + " TEXT," +
                    WarehouseTable.COLUMN_NAME_ZSUMI + " TEXT," +
                    WarehouseTable.COLUMN_NAME_STOCK0 + " TEXT," +
                    WarehouseTable.COLUMN_NAME_CONF + " TEXT," +
                    WarehouseTable.COLUMN_NAME_STGELOCTYPE + " TEXT)";

    public static final String SQL_DELETE_WAREHOUSE_TABLE = "DROP TABLE IF EXISTS " + WarehouseTable.TABLE_NAME_WAREHOUSE;

    public static final String SQL_DELETE_ALL_WAREHOUSES = "DELETE FROM " + WarehouseTable.TABLE_NAME_WAREHOUSE;

    public static void deleteAllWarehouses(Context context){
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(SQL_DELETE_ALL_WAREHOUSES);
    }

    public static boolean insertWarehouses(ArrayList<Warehouse> warehouses, Context context) {

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        boolean allInserted = false;
        for (int i = 0; i < warehouses.size(); i++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(WarehouseTable.COLUMN_NAME_STGELOC, warehouses.get(i).getStgeLoc());
            contentValues.put(WarehouseTable.COLUMN_NAME_LGOBE, warehouses.get(i).getLgobe());
            contentValues.put(WarehouseTable.COLUMN_NAME_ZSUMI, warehouses.get(i).getZsumi());
            contentValues.put(WarehouseTable.COLUMN_NAME_STOCK0, warehouses.get(i).getStock0());
            contentValues.put(WarehouseTable.COLUMN_NAME_CONF, warehouses.get(i).getConf());
            contentValues.put(WarehouseTable.COLUMN_NAME_STGELOCTYPE, warehouses.get(i).getStgeLocType());
            long inserted = 0;
            try {
                inserted = db.insert(WarehouseTable.TABLE_NAME_WAREHOUSE, null, contentValues);
            }
            catch (Exception e){
                e.printStackTrace();
            }
            if (inserted > 0){
                allInserted = true;
            }
            else {
                return false;
            }
        }
        return allInserted;
    }

    public static Warehouse findWarehouseById(String stgeloc, Context context){

        Warehouse warehouse = null;
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                WarehouseTable.COLUMN_NAME_STGELOC,
                WarehouseTable.COLUMN_NAME_LGOBE,
                WarehouseTable.COLUMN_NAME_ZSUMI,
                WarehouseTable.COLUMN_NAME_STOCK0,
                WarehouseTable.COLUMN_NAME_CONF,
                WarehouseTable.COLUMN_NAME_STGELOCTYPE,
        };

        String selection = WarehouseTable.COLUMN_NAME_STGELOC+ " = ?";
        String[] selectionArgs = {stgeloc};

        Cursor cursor = db.query(
                WarehouseTable.TABLE_NAME_WAREHOUSE,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,          // The columns for the WHERE clause
                selectionArgs,       // The values for the WHERE clause
                null,           // don't group the rows
                null,            // don't filter by row groups
                null            // The sort order
        );

        if (cursor.moveToFirst()){
            warehouse = new Warehouse();
            warehouse.setStgeLoc(cursor.getString(1));
            warehouse.setLgobe(cursor.getString(2));
            warehouse.setZsumi(cursor.getString(3));
            warehouse.setStock0(cursor.getString(4));
            warehouse.setConf(cursor.getString(5));
            warehouse.setStgeLocType(cursor.getString(6));
        }
        return warehouse;
    }


    public static ArrayList<Warehouse> findAllWarehouses(Context context){

        ArrayList<Warehouse> warehouses = new ArrayList<>();
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                WarehouseTable.COLUMN_NAME_STGELOC,
                WarehouseTable.COLUMN_NAME_LGOBE,
                WarehouseTable.COLUMN_NAME_ZSUMI,
                WarehouseTable.COLUMN_NAME_STOCK0,
                WarehouseTable.COLUMN_NAME_CONF,
                WarehouseTable.COLUMN_NAME_STGELOCTYPE,
        };

        String sortOrder = WarehouseTable.COLUMN_NAME_LGOBE + " ASC";

        Cursor cursor = db.query(
                WarehouseTable.TABLE_NAME_WAREHOUSE,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,          // The columns for the WHERE clause
                null,       // The values for the WHERE clause
                null,           // don't group the rows
                null,            // don't filter by row groups
                sortOrder            // The sort order
        );


        while (cursor.moveToNext()){
            Warehouse warehouse = new Warehouse();
            warehouse.setStgeLoc(cursor.getString(1));
            warehouse.setLgobe(cursor.getString(2));
            warehouse.setZsumi(cursor.getString(3));
            warehouse.setStock0(cursor.getString(4));
            warehouse.setConf(cursor.getString(5));
            warehouse.setStgeLocType(cursor.getString(6));
            warehouses.add(warehouse);
        }
        return warehouses;
    }

    public static ArrayList<Warehouse> findAllWarehousesByZsumi(Context context){

        ArrayList<Warehouse> warehouses = new ArrayList<>();
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                WarehouseTable.COLUMN_NAME_STGELOC,
                WarehouseTable.COLUMN_NAME_LGOBE,
                WarehouseTable.COLUMN_NAME_ZSUMI,
                WarehouseTable.COLUMN_NAME_STOCK0,
                WarehouseTable.COLUMN_NAME_CONF,
                WarehouseTable.COLUMN_NAME_STGELOCTYPE,
        };

        String selection = WarehouseTable.COLUMN_NAME_ZSUMI+ " = ?";
        String[] selectionArgs = {"X"};

        String sortOrder = WarehouseTable.COLUMN_NAME_LGOBE + " ASC";

        Cursor cursor = db.query(
                WarehouseTable.TABLE_NAME_WAREHOUSE,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,          // The columns for the WHERE clause
                selectionArgs,       // The values for the WHERE clause
                null,           // don't group the rows
                null,            // don't filter by row groups
                sortOrder            // The sort order
        );


        while (cursor.moveToNext()){
            Warehouse warehouse = new Warehouse();
            warehouse.setStgeLoc(cursor.getString(1));
            warehouse.setLgobe(cursor.getString(2));
            warehouse.setZsumi(cursor.getString(3));
            warehouse.setStock0(cursor.getString(4));
            warehouse.setConf(cursor.getString(5));
            warehouse.setStgeLocType(cursor.getString(6));
            warehouses.add(warehouse);
        }
        return warehouses;
    }

    public static ArrayList<Warehouse> findAllWarehousesByStgeLocType(int stgeloctype, Context context){

        ArrayList<Warehouse> warehouses = new ArrayList<>();
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                WarehouseTable.COLUMN_NAME_STGELOC,
                WarehouseTable.COLUMN_NAME_LGOBE,
                WarehouseTable.COLUMN_NAME_ZSUMI,
                WarehouseTable.COLUMN_NAME_STOCK0,
                WarehouseTable.COLUMN_NAME_CONF,
                WarehouseTable.COLUMN_NAME_STGELOCTYPE,
        };

        String selection = WarehouseTable.COLUMN_NAME_STGELOCTYPE+ " = ?";
        String[] selectionArgs = {String.valueOf(stgeloctype)};

        String sortOrder = WarehouseTable.COLUMN_NAME_LGOBE + " ASC";

        Cursor cursor = db.query(
                WarehouseTable.TABLE_NAME_WAREHOUSE,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,          // The columns for the WHERE clause
                selectionArgs,       // The values for the WHERE clause
                null,           // don't group the rows
                null,            // don't filter by row groups
                sortOrder            // The sort order
        );


        while (cursor.moveToNext()){
            Warehouse warehouse = new Warehouse();
            warehouse.setStgeLoc(cursor.getString(1));
            warehouse.setLgobe(cursor.getString(2));
            warehouse.setZsumi(cursor.getString(3));
            warehouse.setStock0(cursor.getString(4));
            warehouse.setConf(cursor.getString(5));
            warehouse.setStgeLocType(cursor.getString(6));
            warehouses.add(warehouse);
        }
        return warehouses;
    }
}
