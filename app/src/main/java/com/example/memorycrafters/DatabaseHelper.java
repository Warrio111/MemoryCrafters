package com.example.memorycrafters;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper instance;
    private static final String DATABASE_NAME = "MemoryCraftersDB";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_PARTIDAS = "partidas";
    private static final String TABLE_MONEDAS = "monedas";

    // Columnas de la tabla "partidas"
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_FECHA_INICIO = "fechaInicio";
    private static final String COLUMN_FECHA_FIN = "fechaFin";

    // Columnas de la tabla "monedas"
    private static final String COLUMN_MONEDAS_ID = "monedas_id";
    private static final String COLUMN_CANTIDAD_MONEDAS = "cantidad";
    private static final String SQL_CREATE_PARTIDAS_TABLE = "CREATE TABLE " + TABLE_PARTIDAS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_FECHA_INICIO + " TEXT,"
            + COLUMN_FECHA_FIN + " TEXT,"
            + COLUMN_MONEDAS_ID + " INTEGER," // Columna de la clave externa
            + "FOREIGN KEY (" + COLUMN_MONEDAS_ID + ") REFERENCES " + TABLE_MONEDAS + "(" + COLUMN_MONEDAS_ID + ")" // Restricción de clave externa
            + ")";
    private static final String SQL_DELETE_PARTIDAS_TABLE = "DROP TABLE IF EXISTS " + TABLE_PARTIDAS;
    private static final String SQL_CREATE_MONEDAS_TABLE = "CREATE TABLE " + TABLE_MONEDAS + "("
            + COLUMN_MONEDAS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_CANTIDAD_MONEDAS + " INTEGER"
            + ")";
    private static final String SQL_DELETE_MONEDAS_TABLE = "DROP TABLE IF EXISTS " + TABLE_MONEDAS;

    private static final String SQL_CREATE_CARD_ENTRIES =
            "CREATE TABLE " + MemoryGameContract.CardEntry.TABLE_NAME + " (" +
                    MemoryGameContract.CardEntry._ID + " INTEGER PRIMARY KEY," +
                    MemoryGameContract.CardEntry.COLUMN_POSITION + " INTEGER," +
                    MemoryGameContract.CardEntry.COLUMN_VALUE + " TEXT," +
                    MemoryGameContract.CardEntry.COLUMN_VISIBILITY + " INTEGER," +
                    MemoryGameContract.CardEntry.COLUMN_PARTIDA_ID + " INTEGER," + // Columna de la clave externa
                    "FOREIGN KEY (" + MemoryGameContract.CardEntry.COLUMN_PARTIDA_ID + ") REFERENCES " +
                    TABLE_PARTIDAS + "(" + COLUMN_ID + ")" + // Restricción de clave externa
                    ")";


    private static final String SQL_DELETE_CARD_ENTRIES = "DROP TABLE IF EXISTS " + MemoryGameContract.CardEntry.TABLE_NAME;


    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear tabla de partidas
        db.execSQL(SQL_CREATE_PARTIDAS_TABLE);

        // Crear tabla de monedas
        db.execSQL(SQL_CREATE_MONEDAS_TABLE);

        // Crear tabla de cartas
        db.execSQL(SQL_CREATE_CARD_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_PARTIDAS_TABLE);
        db.execSQL(SQL_DELETE_MONEDAS_TABLE);
        db.execSQL(SQL_DELETE_CARD_ENTRIES);
        onCreate(db);
    }
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
    @SuppressLint("Range")
    public void inicializarPartida(int cantidadCartas) {
        // Lo primero de todo será crear una partida
        registrarPartidaYMonedas(0);

        // Obtener el ID de la última partida
        int partidaId = obtenerUltimaPartidaId();

        // Cerrar la base de datos antes de insertar las cartas
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Insertar las cartas en la tabla "cartas"
        for (int i = 0; i < cantidadCartas; i++) {
            values.put(MemoryGameContract.CardEntry.COLUMN_POSITION, i);
            values.put(MemoryGameContract.CardEntry.COLUMN_VALUE, "card_" + i);
            values.put(MemoryGameContract.CardEntry.COLUMN_VISIBILITY, 0);
            values.put(MemoryGameContract.CardEntry.COLUMN_PARTIDA_ID, partidaId);
            db.insert(MemoryGameContract.CardEntry.TABLE_NAME, null, values);
        }

        // Cerrar la base de datos después de insertar las cartas
        db.close();
    }


    public void updateCardVisibility(int cardId, int visibility) {

        ContentValues values = new ContentValues();
        values.put(MemoryGameContract.CardEntry.COLUMN_VISIBILITY, visibility);

        // Obtener el ID de la última partida
        int partidaId = obtenerUltimaPartidaId();

        String selection = MemoryGameContract.CardEntry.COLUMN_POSITION + " = ? AND " +
                MemoryGameContract.CardEntry.COLUMN_PARTIDA_ID + " = ?";
        String[] selectionArgs = { String.valueOf(cardId), String.valueOf(partidaId) };
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.update(
                    MemoryGameContract.CardEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);
            // Consulta raw para debugar: obtener todos los registros de la tabla de cartas (cards)
            //Cursor cursor = db.rawQuery("SELECT * FROM " + MemoryGameContract.CardEntry.TABLE_NAME, null);
            Cursor cursor = db.rawQuery("SELECT * FROM " + MemoryGameContract.CardEntry.TABLE_NAME +
                    " WHERE " + MemoryGameContract.CardEntry.COLUMN_PARTIDA_ID + " = ?", new String[]{String.valueOf(partidaId)});
            if (cursor.moveToFirst()) {
                do {
                    // Iterar sobre los resultados y mostrarlos en el registro (log)
                    @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(MemoryGameContract.CardEntry._ID));
                    @SuppressLint("Range") int position = cursor.getInt(cursor.getColumnIndex(MemoryGameContract.CardEntry.COLUMN_POSITION));
                    @SuppressLint("Range") String value = cursor.getString(cursor.getColumnIndex(MemoryGameContract.CardEntry.COLUMN_VALUE));
                    @SuppressLint("Range") int visibilityResult = cursor.getInt(cursor.getColumnIndex(MemoryGameContract.CardEntry.COLUMN_VISIBILITY));
                    @SuppressLint("Range") int partidaIdResult = cursor.getInt(cursor.getColumnIndex(MemoryGameContract.CardEntry.COLUMN_PARTIDA_ID));

                    // Mostrar los resultados en el registro (log)
                    Log.d("DEBUG", "ID: " + id + ", Position: " + position + ", Value: " + value +
                            ", Visibility: " + visibilityResult + ", Partida ID: " + partidaIdResult);
                } while (cursor.moveToNext());
            }
            cursor.close(); // Cerrar el cursor después de usarlo
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            db.close();
        }
    }



    @SuppressLint("Range")
    public int obtenerUltimaPartidaId() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MAX(" + COLUMN_ID + ") AS " + COLUMN_ID + " FROM " + TABLE_PARTIDAS, null);
        int partidaId = 0;
        if (cursor.moveToFirst()) {
            partidaId = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
        }
        cursor.close();
        db.close();
        return partidaId;
    }
    @SuppressLint("Range")
    public int obtenerUltimaMonedaId() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MAX(" + COLUMN_MONEDAS_ID + ") AS " + COLUMN_MONEDAS_ID + " FROM " + TABLE_MONEDAS, null);
        int monedasId = 0;
        if (cursor.moveToFirst()) {
            monedasId = cursor.getInt(cursor.getColumnIndex(COLUMN_MONEDAS_ID));
        }
        cursor.close();
        db.close();
        return monedasId;
    }



    public String obtenerFechaActual() {
        // Obtener la fecha y hora actual
        Calendar calendar = Calendar.getInstance();
        // Formatear la fecha y hora actual como una cadena de texto
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    // Método para actualizar las monedas
    public void actualizarMonedas(int cantidadMonedas) {
        int ultimaMonedaId = obtenerUltimaMonedaId();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CANTIDAD_MONEDAS, cantidadMonedas);
        try {
            // Actualizar las monedas en la tabla "monedas"
            db.update(TABLE_MONEDAS, values, COLUMN_MONEDAS_ID + " = " + ultimaMonedaId, null);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    public void finalizarPartida() {
        int ultimaPartidaId = obtenerUltimaPartidaId();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FECHA_FIN, obtenerFechaActual());
        try {
            // Actualizar la fecha de fin de la partida en la tabla "partidas"
            db.update(TABLE_PARTIDAS, values, COLUMN_ID + " = " + ultimaPartidaId, null);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }


    public void registrarPartida(int monedasId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FECHA_INICIO, obtenerFechaActual());
        values.put(COLUMN_FECHA_FIN, "");
        values.put(COLUMN_MONEDAS_ID, monedasId); // Asignar el ID de las monedas a la partida
        try {
            // Insertar la nueva partida en la tabla "partidas"
            db.insert(TABLE_PARTIDAS, null, values);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    public void registrarPartidaYMonedas(int cantidadMonedas) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CANTIDAD_MONEDAS, cantidadMonedas);
        try {
            // Insertar la cantidad de monedas en la tabla "monedas"
            long monedasId = db.insert(TABLE_MONEDAS, null, values);
            // Registrar la partida asociada a estas monedas
            if (monedasId != -1) { // Verificar si se insertaron las monedas correctamente
                registrarPartida((int) monedasId); // Asignar el ID de las monedas a la partida
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }


    public ArrayList<String> obtenerPartidas() {
        ArrayList<String> partidas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT p." + COLUMN_ID + ", p." + COLUMN_FECHA_INICIO + ", p." + COLUMN_FECHA_FIN + ", m." + COLUMN_CANTIDAD_MONEDAS +
                " FROM " + TABLE_PARTIDAS + " p LEFT JOIN " + TABLE_MONEDAS + " m" +
                " ON p." + COLUMN_MONEDAS_ID + " = m." + COLUMN_MONEDAS_ID, null);
        if (cursor.moveToFirst()) {
            do {
                // Obtener el ID de la partida
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                // Obtener la fecha de la partida
                @SuppressLint("Range") String fechaInicio = cursor.getString(cursor.getColumnIndex(COLUMN_FECHA_INICIO));
                @SuppressLint("Range") String fechaFin = cursor.getString(cursor.getColumnIndex(COLUMN_FECHA_FIN));
                // Obtener la cantidad de monedas asociadas a la partida
                @SuppressLint("Range") int cantidadMonedas = cursor.getInt(cursor.getColumnIndex(COLUMN_CANTIDAD_MONEDAS));
                // Agregar la información de la partida a la lista
                partidas.add("Partida #" + id + " - FechaInicio: " + fechaInicio + " - FechaFin: " + fechaFin +" - Cantidad de monedas: " + cantidadMonedas);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return partidas;
    }

    @SuppressLint("Range")
    public int obtenerCantidadMonedas(int monedasId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MONEDAS, new String[]{COLUMN_CANTIDAD_MONEDAS}, COLUMN_MONEDAS_ID + "=?",
                new String[]{String.valueOf(monedasId)}, null, null, null);
        int cantidadMonedas = 0;
        if (cursor != null && cursor.moveToFirst()) {
            cantidadMonedas = cursor.getInt(cursor.getColumnIndex(COLUMN_CANTIDAD_MONEDAS));
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return cantidadMonedas;
    }
    @SuppressLint("Range")
    public String obtenerFechaFinPartida(String fechaInicio) {
        String fechaFin = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PARTIDAS, new String[]{COLUMN_FECHA_FIN}, COLUMN_FECHA_INICIO + "=?",
                new String[]{fechaInicio}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            fechaFin = cursor.getString(cursor.getColumnIndex(COLUMN_FECHA_FIN));
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return fechaFin;
    }

    public HashMap<String, Integer> obtenerTodasVictorias() {
        HashMap<String, Integer> victorias = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PARTIDAS, new String[]{COLUMN_FECHA_INICIO, COLUMN_FECHA_FIN, COLUMN_MONEDAS_ID}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                // Obtener la fecha de la partida
                @SuppressLint("Range") String fechaInicio = cursor.getString(cursor.getColumnIndex(COLUMN_FECHA_INICIO));
                // Obtener la fecha de fin de la partida
                @SuppressLint("Range") String fechaFin = cursor.getString(cursor.getColumnIndex(COLUMN_FECHA_FIN));
                // Obtener el ID de las monedas asociadas
                @SuppressLint("Range") int monedasId = cursor.getInt(cursor.getColumnIndex(COLUMN_MONEDAS_ID));
                // Obtener la cantidad de monedas asociadas
                int cantidadMonedas = obtenerCantidadMonedas(monedasId);
                // Agregar la fecha y la cantidad de monedas al HashMap
                if(fechaFin.equals("")){
                    // The continue statement terminates execution of the statements in the
                    // current iteration of the current or labeled loop, and continues execution of the loop
                    // with the next iteration.
                    continue;
                }
                victorias.put(fechaInicio, cantidadMonedas);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return victorias;
    }

}
