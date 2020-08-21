package com.rcdhotels.gestiondesolicitudes.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.rcdhotels.gestiondesolicitudes.database.HotelsTableQuerys.SQL_CREATE_HOTEL_TABLE;
import static com.rcdhotels.gestiondesolicitudes.database.HotelsTableQuerys.SQL_DELETE_HOTEL_TABLE;
import static com.rcdhotels.gestiondesolicitudes.database.UserTableQuerys.SQL_CREATE_PERMISSIONS;
import static com.rcdhotels.gestiondesolicitudes.database.UserTableQuerys.SQL_CREATE_USER_TABLE;
import static com.rcdhotels.gestiondesolicitudes.database.UserTableQuerys.SQL_DELETE_PERMISSIONS_TABLE;
import static com.rcdhotels.gestiondesolicitudes.database.UserTableQuerys.SQL_DELETE_USER_TABLE;
import static com.rcdhotels.gestiondesolicitudes.database.WarehouseTableQuerys.SQL_CREATE_WAREHOUSE_TABLE;
import static com.rcdhotels.gestiondesolicitudes.database.WarehouseTableQuerys.SQL_DELETE_WAREHOUSE_TABLE;

public class Request_Management {

    private Request_Management() {}

    public static class DbHelper extends SQLiteOpenHelper {
        // If you change the database schema, you must increment the database version.
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "Request_Management.db";

        public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_USER_TABLE);
            db.execSQL(SQL_CREATE_PERMISSIONS);
            db.execSQL(SQL_CREATE_HOTEL_TABLE);
            db.execSQL(SQL_CREATE_WAREHOUSE_TABLE);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(SQL_DELETE_USER_TABLE);
            db.execSQL(SQL_DELETE_PERMISSIONS_TABLE);
            db.execSQL(SQL_DELETE_HOTEL_TABLE);
            db.execSQL(SQL_DELETE_WAREHOUSE_TABLE);
            onCreate(db);
        }
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }
}
