package com.erickdiaz.proyectobiblioteca;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String TABLE_PRESTAMOS = "prestamos";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LIBRO = "libro";
    public static final String COLUMN_CANTIDAD_PRESTAMOS = "cantidad_prestamos";
    public static final String COLUMN_FECHA_PRESTAMO = "fecha_prestamo";
    public static final String COLUMN_DURACION = "duracion";

    private static final String DATABASE_NAME = "biblioteca.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "create table "
            + TABLE_PRESTAMOS + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_LIBRO + " text not null, "
            + COLUMN_CANTIDAD_PRESTAMOS + " integer not null default 0, "
            + COLUMN_FECHA_PRESTAMO + " text, "
            + COLUMN_DURACION + " text);";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        try {
            database.execSQL(DATABASE_CREATE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRESTAMOS);
        onCreate(db);
    }

    public long agregarPrestamo(String libro) {
        SQLiteDatabase database = this.getWritableDatabase();
        long insertId = -1; // Inicializamos con un valor predeterminado

        // Verifica si el libro ya existe en la base de datos
        Cursor cursor = database.query(
                TABLE_PRESTAMOS,
                new String[]{COLUMN_ID, COLUMN_LIBRO, COLUMN_CANTIDAD_PRESTAMOS},
                COLUMN_LIBRO + " = ?",
                new String[]{libro},
                null,
                null,
                null
        );

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                // Si el libro ya existe, incrementa la cantidad de préstamos
                int idColumnIndex = cursor.getColumnIndex(COLUMN_ID);
                int cantidadPrestamosColumnIndex = cursor.getColumnIndex(COLUMN_CANTIDAD_PRESTAMOS);

                if (idColumnIndex != -1 && cantidadPrestamosColumnIndex != -1) {
                    int id = cursor.getInt(idColumnIndex);
                    int cantidadPrestamos = cursor.getInt(cantidadPrestamosColumnIndex) + 1;

                    ContentValues values = new ContentValues();
                    values.put(COLUMN_CANTIDAD_PRESTAMOS, cantidadPrestamos);

                    // Actualiza la cantidad de préstamos para el libro existente
                    database.update(TABLE_PRESTAMOS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
                    insertId = id;
                }
            } else {
                // Si el libro no existe, agrégalo con una cantidad inicial de 1
                ContentValues values = new ContentValues();
                values.put(COLUMN_LIBRO, libro);
                values.put(COLUMN_CANTIDAD_PRESTAMOS, 1);

                // Inserta un nuevo préstamo con cantidad inicial 1
                insertId = database.insert(TABLE_PRESTAMOS, null, values);
            }

            cursor.close(); // Cierra el cursor después de usarlo
        }

        database.close();

        return insertId;
    }


    public void actualizarCantidadPrestamos(String idPrestamo, int nuevaCantidad) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CANTIDAD_PRESTAMOS, nuevaCantidad);
        database.update(TABLE_PRESTAMOS, values, COLUMN_ID + " = ?", new String[]{idPrestamo});
        database.close();
    }
}
