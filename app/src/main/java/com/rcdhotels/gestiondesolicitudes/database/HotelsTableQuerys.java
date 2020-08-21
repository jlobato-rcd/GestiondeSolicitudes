package com.rcdhotels.gestiondesolicitudes.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.rcdhotels.gestiondesolicitudes.model.Hotel;

import java.util.ArrayList;

import static com.rcdhotels.gestiondesolicitudes.dbhelper.Request_Management.*;

public class HotelsTableQuerys {

    public class HotelsTable implements BaseColumns {
        public static final String TABLE_NAME_HOTEL = "HOTEL";
        public static final String COLUMN_NAME_IDHOTEL = "IDHOTEL";
        public static final String COLUMN_NAME_NAME = "NAME";
        public static final String COLUMN_NAME_ADDRESS = "ADDRESS";
        public static final String COLUMN_NAME_PHONE = "PHONE";
        public static final String COLUMN_NAME_EMAIL = "EMAIL";
        public static final String COLUMN_NAME_CODIGOLOGIN = "CODIGOLOGIN";
        public static final String COLUMN_NAME_IDSOCIETY = "IDSOCIETY";
    }
    
    public static final String SQL_CREATE_HOTEL_TABLE =
            "CREATE TABLE " + HotelsTable.TABLE_NAME_HOTEL + " (" +
                    HotelsTable._ID + " INTEGER PRIMARY KEY," +
                    HotelsTable.COLUMN_NAME_IDHOTEL + " TEXT," +
                    HotelsTable.COLUMN_NAME_NAME + " TEXT," +
                    HotelsTable.COLUMN_NAME_ADDRESS + " TEXT," +
                    HotelsTable.COLUMN_NAME_PHONE + " TEXT," +
                    HotelsTable.COLUMN_NAME_EMAIL + " TEXT," +
                    HotelsTable.COLUMN_NAME_CODIGOLOGIN + " TEXT," +
                    HotelsTable.COLUMN_NAME_IDSOCIETY + " TEXT)";

    public static final String SQL_DELETE_HOTEL_TABLE = "DROP TABLE IF EXISTS " + HotelsTable.TABLE_NAME_HOTEL;

    public static boolean InsertHotels(ArrayList<Hotel> hotels, Context context) {

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        boolean allInserted = false;
        for (int i = 0; i < hotels.size(); i++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(HotelsTable.COLUMN_NAME_IDHOTEL, hotels.get(i).getIdHotel());
            contentValues.put(HotelsTable.COLUMN_NAME_NAME, hotels.get(i).getNameHotel());
            contentValues.put(HotelsTable.COLUMN_NAME_ADDRESS, hotels.get(i).getAddressHotel());
            contentValues.put(HotelsTable.COLUMN_NAME_PHONE, hotels.get(i).getPhone());
            contentValues.put(HotelsTable.COLUMN_NAME_EMAIL, hotels.get(i).getEmail());
            contentValues.put(HotelsTable.COLUMN_NAME_CODIGOLOGIN, hotels.get(i).getCodigoLogin());
            contentValues.put(HotelsTable.COLUMN_NAME_IDSOCIETY, hotels.get(i).getIdSociety());
            long inserted = db.insert(HotelsTable.TABLE_NAME_HOTEL, null, contentValues);
            if (inserted > 0){
                allInserted = true;
            }
            else {
                return false;
            }
        }

        return allInserted;

    }

    public static Hotel getHotel(Context context, String idHotel){
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                HotelsTable.COLUMN_NAME_IDHOTEL,
                HotelsTable.COLUMN_NAME_NAME,
                HotelsTable.COLUMN_NAME_ADDRESS,
                HotelsTable.COLUMN_NAME_PHONE,
                HotelsTable.COLUMN_NAME_EMAIL,
                HotelsTable.COLUMN_NAME_CODIGOLOGIN,
                HotelsTable.COLUMN_NAME_IDSOCIETY,
        };

        String selection = HotelsTable.COLUMN_NAME_IDHOTEL+ " = ?";
        String[] selectionArgs = {idHotel};

        String sortOrder = HotelsTable.COLUMN_NAME_NAME + " ASC";

        Cursor cursor = db.query(
                HotelsTable.TABLE_NAME_HOTEL,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,          // The columns for the WHERE clause
                selectionArgs,       // The values for the WHERE clause
                null,           // don't group the rows
                null,            // don't filter by row groups
                sortOrder            // The sort order
        );

        Hotel hotel = null;
        if (cursor.moveToFirst()){
            hotel = new Hotel();
            hotel.setIdHotel(cursor.getString(1));
            hotel.setNameHotel(cursor.getString(2));
            hotel.setAddressHotel(cursor.getString(3));
            hotel.setPhone(cursor.getString(4));
            hotel.setEmail(cursor.getString(5));
            hotel.setCodigoLogin(cursor.getString(6));
            hotel.setIdSociety(cursor.getInt(7));

        }
        return hotel;
    }
}
