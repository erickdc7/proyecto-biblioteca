import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "biblioteca.db";
    private static final int DATABASE_VERSION = 1;

    // Nombre de la tabla y columnas
    private static final String TABLE_NAME = "usuarios";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NOMBRES = "nombres";
    private static final String COLUMN_DNI = "dni";
    private static final String COLUMN_CODIGO = "codigo";
    private static final String COLUMN_TELEFONO = "telefono";
    private static final String COLUMN_CORREO = "correo";
    private static final String COLUMN_PASSWORD = "password";

    // Consulta SQL para crear la tabla
    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NOMBRES + " TEXT,"
                    + COLUMN_DNI + " TEXT,"
                    + COLUMN_CODIGO + " TEXT,"
                    + COLUMN_TELEFONO + " TEXT,"
                    + COLUMN_CORREO + " TEXT,"
                    + COLUMN_PASSWORD + " TEXT" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear la tabla cuando la base de datos es creada por primera vez
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Manejar las actualizaciones de la base de datos, si es necesario
    }

    // Método para agregar un nuevo usuario a la base de datos
    public long agregarUsuario(String nombres, String dni, String codigo, String telefono, String correo, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOMBRES, nombres);
        values.put(COLUMN_DNI, dni);
        values.put(COLUMN_CODIGO, codigo);
        values.put(COLUMN_TELEFONO, telefono);
        values.put(COLUMN_CORREO, correo);
        values.put(COLUMN_PASSWORD, password);

        // Insertar fila
        long id = db.insert(TABLE_NAME, null, values);
        db.close();
        return id;
    }
}
