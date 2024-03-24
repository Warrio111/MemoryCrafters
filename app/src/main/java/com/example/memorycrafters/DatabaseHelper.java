package com.example.memorycrafters;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MemoryCraftersDB";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_PARTIDAS = "partidas";
    private static final String TABLE_MONEDAS = "monedas";

    // Columnas de la tabla "partidas"
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_FECHA = "fecha";

    // Columnas de la tabla "monedas"
    private static final String COLUMN_MONEDAS_ID = "monedas_id";
    private static final String COLUMN_CANTIDAD_MONEDAS = "cantidad";
    private static final String SQL_CREATE_PARTIDAS_TABLE = "CREATE TABLE " + TABLE_PARTIDAS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_FECHA + " TEXT,"
            + COLUMN_MONEDAS_ID + " INTEGER," // Columna de la clave externa
            + "FOREIGN KEY (" + COLUMN_MONEDAS_ID + ") REFERENCES " + TABLE_MONEDAS + "(" + COLUMN_MONEDAS_ID + ")" // Restricción de clave externa
            + ")";
    private static final String SQL_DELETE_PARTIDAS_TABLE = "DROP TABLE IF EXISTS " + TABLE_PARTIDAS;
    private static final String SQL_CREATE_MONEDAS_TABLE = "CREATE TABLE " + TABLE_MONEDAS + "("
            + COLUMN_MONEDAS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_CANTIDAD_MONEDAS + " INTEGER"
            + ")";
    private static final String SQL_DELETE_MONEDAS_TABLE = "DROP TABLE IF EXISTS " + TABLE_MONEDAS;
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear tabla de partidas
        db.execSQL(SQL_CREATE_PARTIDAS_TABLE);

        // Crear tabla de monedas
        db.execSQL(SQL_CREATE_MONEDAS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_PARTIDAS_TABLE);
        db.execSQL(SQL_DELETE_MONEDAS_TABLE);
        onCreate(db);
    }
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    private String obtenerFechaActual() {
        // Obtener la fecha y hora actual
        Calendar calendar = Calendar.getInstance();
        // Formatear la fecha y hora actual como una cadena de texto
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    public void registrarPartida(int monedasId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FECHA, obtenerFechaActual());
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
        Cursor cursor = db.rawQuery("SELECT p." + COLUMN_ID + ", p." + COLUMN_FECHA + ", m." + COLUMN_CANTIDAD_MONEDAS +
                " FROM " + TABLE_PARTIDAS + " p LEFT JOIN " + TABLE_MONEDAS + " m" +
                " ON p." + COLUMN_MONEDAS_ID + " = m." + COLUMN_MONEDAS_ID, null);
        if (cursor.moveToFirst()) {
            do {
                // Obtener el ID de la partida
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                // Obtener la fecha de la partida
                @SuppressLint("Range") String fecha = cursor.getString(cursor.getColumnIndex(COLUMN_FECHA));
                // Obtener la cantidad de monedas asociadas a la partida
                @SuppressLint("Range") int cantidadMonedas = cursor.getInt(cursor.getColumnIndex(COLUMN_CANTIDAD_MONEDAS));
                // Agregar la información de la partida a la lista
                partidas.add("Partida #" + id + " - Fecha: " + fecha + " - Cantidad de monedas: " + cantidadMonedas);
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



}
