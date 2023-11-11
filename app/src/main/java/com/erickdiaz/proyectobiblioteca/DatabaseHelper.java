package com.erickdiaz.proyectobiblioteca;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "proyecto_biblioteca.db";
    private static final String TABLE_NAME = "users";
    private static final String COL_ID = "ID";
    private static final String COL_USERNAME = "username";
    private static final String COL_DNI = "dni";
    private static final String COL_CODIGO = "codigo";
    private static final String COL_TELEFONO = "telefono";
    private static final String COL_CORREO = "correo";
    private static final String COL_PASSWORD = "password";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USERNAME + " TEXT, " +
                COL_DNI + " TEXT, " +
                COL_CODIGO + " TEXT, " +
                COL_TELEFONO + " TEXT, " +
                COL_CORREO + " TEXT, " +
                COL_PASSWORD + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long addUser(String username, String dni, String codigo, String telefono, String correo, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, username);
        values.put(COL_DNI, dni);
        values.put(COL_CODIGO, codigo);
        values.put(COL_TELEFONO, telefono);
        values.put(COL_CORREO, correo);
        values.put(COL_PASSWORD, password);

        long result = db.insert(TABLE_NAME, null, values);

        db.close();
        return result;
    }
}
