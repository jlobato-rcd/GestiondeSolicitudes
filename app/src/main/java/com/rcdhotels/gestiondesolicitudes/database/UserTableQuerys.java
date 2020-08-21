package com.rcdhotels.gestiondesolicitudes.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.rcdhotels.gestiondesolicitudes.model.Permission;
import com.rcdhotels.gestiondesolicitudes.model.User;

import static com.rcdhotels.gestiondesolicitudes.database.HotelsTableQuerys.SQL_DELETE_HOTEL_TABLE;
import static com.rcdhotels.gestiondesolicitudes.database.WarehouseTableQuerys.SQL_DELETE_WAREHOUSE_TABLE;
import static com.rcdhotels.gestiondesolicitudes.dbhelper.Request_Management.*;
import static com.rcdhotels.gestiondesolicitudes.model.UtilsClass.user;

public class UserTableQuerys {

    public class UserTable implements BaseColumns{

        public static final String TABLE_NAME_USER = "user";
        public static final String COLUMN_NAME_IDUSER = "iduser";
        public static final String COLUMN_NAME_IDCOLLABORATOR = "idcollaborator";
        public static final String COLUMN_NAME_PROPERTY = "property";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_PASSWORD = "password";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_FIRSTNAME = "firstname";
        public static final String COLUMN_NAME_SECONDNAME = "secondname";
        public static final String COLUMN_NAME_SURNAME = "surname";
        public static final String COLUMN_NAME_SECONDSURNAME = "secondsurname";
        public static final String COLUMN_NAME_ACTIVE = "active";
        public static final String COLUMN_NAME_CREATEDDATE = "createddate";
        public static final String COLUMN_NAME_PSWENCRYP = "pswencryp";
        public static final String COLUMN_NAME_ROLE = "role";
        public static final String COLUMN_NAME_WAREHOUSE = "warehouse";
        public static final String COLUMN_NAME_MRPCONTROLLER = "mrpcontroller";

        public static final String TABLE_NAME_PERMISSIONS = "permissions";
        public static final String COLUMN_NAME_KEYMODULE = "keymodule";
        public static final String COLUMN_NAME_CREATE = "createP";
        public static final String COLUMN_NAME_READ = "readP";
        public static final String COLUMN_NAME_UPDATE = "updateP";
        public static final String COLUMN_NAME_DELETE = "deleteP";
    }

    public static final String SQL_CREATE_USER_TABLE =
            "CREATE TABLE " + UserTable.TABLE_NAME_USER + " (" +
                    UserTable._ID + " INTEGER PRIMARY KEY," +
                    UserTable.COLUMN_NAME_IDUSER + " INTEGER," +
                    UserTable.COLUMN_NAME_IDCOLLABORATOR + " INTEGER," +
                    UserTable.COLUMN_NAME_PROPERTY + " TEXT," +
                    UserTable.COLUMN_NAME_USERNAME + " TEXT," +
                    UserTable.COLUMN_NAME_PASSWORD + " TEXT," +
                    UserTable.COLUMN_NAME_EMAIL + " TEXT," +
                    UserTable.COLUMN_NAME_FIRSTNAME + " TEXT," +
                    UserTable.COLUMN_NAME_SECONDNAME + " TEXT," +
                    UserTable.COLUMN_NAME_SURNAME + " TEXT," +
                    UserTable.COLUMN_NAME_SECONDSURNAME + " TEXT," +
                    UserTable.COLUMN_NAME_ACTIVE + " TINYINT," +
                    UserTable.COLUMN_NAME_CREATEDDATE + " DATETIME," +
                    UserTable.COLUMN_NAME_PSWENCRYP + " TEXT," +
                    UserTable.COLUMN_NAME_ROLE + " TEXT," +
                    UserTable.COLUMN_NAME_WAREHOUSE + " TEXT," +
                    UserTable.COLUMN_NAME_MRPCONTROLLER + " TEXT)";

    public static final String SQL_DELETE_USER_TABLE = "DROP TABLE IF EXISTS " + UserTable.TABLE_NAME_USER;

    public static final String SQL_CREATE_PERMISSIONS =
            "CREATE TABLE " + UserTable.TABLE_NAME_PERMISSIONS + " (" +
                    UserTable.COLUMN_NAME_IDUSER + " INTEGER," +
                    UserTable.COLUMN_NAME_KEYMODULE + " TEXT," +
                    UserTable.COLUMN_NAME_CREATE + " TINYINT," +
                    UserTable.COLUMN_NAME_READ + " TINYINT," +
                    UserTable.COLUMN_NAME_UPDATE + " TINYINT," +
                    UserTable.COLUMN_NAME_DELETE + " TINYINT)";

    public static final String SQL_DELETE_PERMISSIONS_TABLE = "DROP TABLE IF EXISTS " + UserTable.TABLE_NAME_PERMISSIONS;

    public static long InsertUser(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserTable.COLUMN_NAME_IDUSER, user.getIdUser());
        contentValues.put(UserTable.COLUMN_NAME_IDCOLLABORATOR, user.getIdCollaborator());
        contentValues.put(UserTable.COLUMN_NAME_PROPERTY, user.getHotel().getIdHotel());
        contentValues.put(UserTable.COLUMN_NAME_USERNAME, user.getUserName());
        contentValues.put(UserTable.COLUMN_NAME_PASSWORD, user.getPassword());
        contentValues.put(UserTable.COLUMN_NAME_EMAIL, user.getEmail());
        contentValues.put(UserTable.COLUMN_NAME_FIRSTNAME, user.getFirstname());
        contentValues.put(UserTable.COLUMN_NAME_SECONDNAME, user.getSecondName());
        contentValues.put(UserTable.COLUMN_NAME_SURNAME, user.getSurName());
        contentValues.put(UserTable.COLUMN_NAME_SECONDSURNAME, user.getSecondSurName());
        contentValues.put(UserTable.COLUMN_NAME_ACTIVE, user.getActive());
        contentValues.put(UserTable.COLUMN_NAME_CREATEDDATE, user.getCreatedDate());
        contentValues.put(UserTable.COLUMN_NAME_PSWENCRYP, user.getPswencryp());
        contentValues.put(UserTable.COLUMN_NAME_ROLE, user.getRole());
        contentValues.put(UserTable.COLUMN_NAME_WAREHOUSE, user.getWarehouse());
        contentValues.put(UserTable.COLUMN_NAME_MRPCONTROLLER, user.getMrpController());
        return db.insert(UserTable.TABLE_NAME_USER, null, contentValues);
    }

    public static long UpdateUserWarehouse(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserTable.COLUMN_NAME_WAREHOUSE, user.getWarehouse());
        String selection = UserTable.COLUMN_NAME_IDUSER + " = ?";
        String[] selectionArgs = new String[]{user.getIdUser()};
        return db.update(UserTable.TABLE_NAME_USER, contentValues, selection, selectionArgs);
    }

    public static long InsertPermissions(int idUser, Permission permission, Context context) {

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(UserTable.COLUMN_NAME_IDUSER, idUser);
        contentValues.put(UserTable.COLUMN_NAME_KEYMODULE, permission.getKeyModule());
        contentValues.put(UserTable.COLUMN_NAME_CREATE, permission.getCreate());
        contentValues.put(UserTable.COLUMN_NAME_READ, permission.getRead());
        contentValues.put(UserTable.COLUMN_NAME_UPDATE, permission.getUpdate());
        contentValues.put(UserTable.COLUMN_NAME_DELETE, permission.getDelete());
        return db.insert(UserTable.TABLE_NAME_PERMISSIONS, null, contentValues);
    }

    public static void deleteUser(Context context){
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(SQL_DELETE_USER_TABLE);
        db.execSQL(SQL_DELETE_PERMISSIONS_TABLE);
        db.execSQL(SQL_DELETE_HOTEL_TABLE);
        db.execSQL(SQL_DELETE_WAREHOUSE_TABLE);
        dbHelper.onCreate(db);
    }

    public static void getUserLogged(Context context){
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                UserTable.COLUMN_NAME_IDUSER,
                UserTable.COLUMN_NAME_IDCOLLABORATOR,
                UserTable.COLUMN_NAME_PROPERTY,
                UserTable.COLUMN_NAME_USERNAME,
                UserTable.COLUMN_NAME_PASSWORD,
                UserTable.COLUMN_NAME_EMAIL,
                UserTable.COLUMN_NAME_FIRSTNAME,
                UserTable.COLUMN_NAME_SECONDNAME,
                UserTable.COLUMN_NAME_SURNAME,
                UserTable.COLUMN_NAME_SECONDSURNAME,
                UserTable.COLUMN_NAME_ACTIVE,
                UserTable.COLUMN_NAME_CREATEDDATE,
                UserTable.COLUMN_NAME_PSWENCRYP,
                UserTable.COLUMN_NAME_ROLE,
                UserTable.COLUMN_NAME_WAREHOUSE,
                UserTable.COLUMN_NAME_MRPCONTROLLER,
        };

        String selection = UserTable.COLUMN_NAME_USERNAME + " = ?";
        String[] selectionArgs = { "My Title" };

        String sortOrder = UserTable.COLUMN_NAME_USERNAME + " DESC";

        Cursor cursor = db.query(
                UserTable.TABLE_NAME_USER,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,          // The columns for the WHERE clause
                null,       // The values for the WHERE clause
                null,           // don't group the rows
                null,            // don't filter by row groups
                null            // The sort order
        );

        if (cursor.moveToFirst()){
            user = new User();
            user.setIdUser(cursor.getString(1));
            user.setIdCollaborator(cursor.getString(2));
            user.getHotel().setIdHotel(cursor.getString(3));
            user.setUserName(cursor.getString(4));
            user.setPassword(cursor.getString(5));
            user.setEmail(cursor.getString(6));
            user.setFirstname(cursor.getString(7));
            user.setSecondName(cursor.getString(8));
            user.setSurName(cursor.getString(9));
            user.setSecondSurName(cursor.getString(10));
            user.setActive(cursor.getString(11));
            user.setCreatedDate(cursor.getString(12));
            user.setPswencryp(cursor.getString(13));
            user.setRole(cursor.getString(14));
            user.setWarehouse(cursor.getString(15));
            user.setMrpController(cursor.getString(16));
        }

        if (user != null){
            String[] projectionPermissions = {
                    UserTable.COLUMN_NAME_IDUSER,
                    UserTable.COLUMN_NAME_KEYMODULE,
                    UserTable.COLUMN_NAME_CREATE,
                    UserTable.COLUMN_NAME_READ,
                    UserTable.COLUMN_NAME_UPDATE,
                    UserTable.COLUMN_NAME_DELETE
            };

            String selectionPermissions = UserTable.COLUMN_NAME_IDUSER+ " = ?";
            String[] selectionselectionPermissionsArgs = { user.getIdUser() };

            String sortOrderPermissions = UserTable.COLUMN_NAME_USERNAME + " DESC";

            Cursor cursorP = db.query(
                    UserTable.TABLE_NAME_PERMISSIONS,   // The table to query
                    projectionPermissions,             // The array of columns to return (pass null to get all)
                    selectionPermissions,          // The columns for the WHERE clause
                    selectionselectionPermissionsArgs,       // The values for the WHERE clause
                    null,           // don't group the rows
                    null,            // don't filter by row groups
                    null            // The sort order
            );

            if (cursorP.moveToNext()){
                Permission permission = new Permission();
                permission.setIdUser(cursorP.getInt(0));
                permission.setKeyModule(cursorP.getString(1));
                permission.setCreate(cursorP.getInt(2));
                permission.setRead(cursorP.getInt(3));
                permission.setUpdate(cursorP.getInt(4));
                permission.setDelete(cursorP.getInt(5));
                user.getPermissions().add(permission);
            }
        }
    }
}
