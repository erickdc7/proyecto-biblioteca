package com.erickdiaz.proyectobiblioteca;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "prestamos.db";
    private static final int DATABASE_VERSION = 2;  // Incrementa la versión al realizar cambios

    // Tabla de préstamos
    public static final String TABLE_PRESTAMOS = "prestamos";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LIBRO = "libro";
    public static final String COLUMN_FECHA_PRESTAMO = "fecha_prestamo";
    public static final String COLUMN_DURACION = "duracion";

    // Sentencia SQL para crear la tabla de préstamos
    private static final String DATABASE_CREATE = "CREATE TABLE "
            + TABLE_PRESTAMOS + "(" + COLUMN_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_LIBRO
            + " TEXT NOT NULL, " + COLUMN_FECHA_PRESTAMO
            + " TEXT NOT NULL, " + COLUMN_DURACION
            + " TEXT NOT NULL);";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        Log.d("DBHelper", "onCreate: Creating database");
        database.execSQL(DATABASE_CREATE);

        // Puedes agregar código aquí para inicializar la base de datos si es necesario
        // Esto podría incluir inserciones de datos de prueba
        insertTestData(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DBHelper", "onUpgrade: oldVersion=" + oldVersion + ", newVersion=" + newVersion);

        // Agregar cambios específicos de versión
        if (oldVersion < 2) {
            Log.d("DBHelper", "Applying upgrade from version 1 to 2");
            // Agregar cambios de la versión 1 a la versión 2
            db.execSQL("ALTER TABLE " + TABLE_PRESTAMOS + " ADD COLUMN nueva_columna TEXT;");
        }
    }

    private void insertTestData(SQLiteDatabase database) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_LIBRO, "Ejemplo Libro");
        values.put(COLUMN_FECHA_PRESTAMO, "01-01-2023");
        values.put(COLUMN_DURACION, "7 días");

        long newRowId = database.insert(TABLE_PRESTAMOS, null, values);

        if (newRowId != -1) {
            Log.d("DBHelper", "Test data inserted successfully");
        } else {
            Log.e("DBHelper", "Error inserting test data");
        }
    }

    // Resto del código...
}
