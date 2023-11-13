package com.erickdiaz.proyectobiblioteca;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "LibrosDB";
    private static final int DATABASE_VERSION = 1;

    // Tabla de préstamos
    public static final String TABLE_PRESTAMOS = "prestamos";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LIBRO = "libro";
    public static final String COLUMN_FECHA_PRESTAMO = "fecha_prestamo";
    public static final String COLUMN_DURACION = "duracion";

    // Sentencia SQL para crear la tabla de préstamos
    private static final String DATABASE_CREATE = "create table "
            + TABLE_PRESTAMOS + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_LIBRO
            + " text not null, " + COLUMN_FECHA_PRESTAMO
            + " text not null, " + COLUMN_DURACION
            + " text not null);";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Aquí puedes realizar acciones específicas si la base de datos necesita ser actualizada.
    }
}

