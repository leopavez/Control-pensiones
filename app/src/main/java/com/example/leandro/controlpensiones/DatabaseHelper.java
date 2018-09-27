package com.example.leandro.controlpensiones;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME= "Pensiones.db";
    //TRABAJADORES
    public static final String ID="id";
    public static final String NOMBRE= "nombre";
    public static final String APELLIDO= "apellido";
    public static final String RUT= "rut";
    public static final String PASS= "pass";
    public static final String ESTADO= "estado";

    //REGISTROS
    public static final int ID_REGISTRO=0;
    public static final String TRABAJADOR_ID= "trabajador_id";
    public static final String PENSION_ID= "pension_id";
    public static final String SERVICIO_ID= "servicio_id";
    public static final String HORA= "hora";
    public static final String FECHA= "fecha";
    public static final String ESTADO_REGISTRO="estado";

    //PENSION
    public static final String ID_PENSION="id_pension";
    public static final String RAZON_SOCIAL="rsocial";
    public static final String RUT_PENSION="rut";
    public static final String ID_SERVICIO="id_servicio";
    public static final String NAMESERVICE="nameservice";
    public static final String DESDE="desde";
    public static final String HASTA="hasta";


    final String CREAR_TABLA_TRABAJADOR="CREATE TABLE trabajador (id INTEGER unique, nombre TEXT, apellido TEXT, rut TEXT, pass TEXT, estado TEXT)";
    final String CREAR_TABLA_SERVICIOS="CREATE TABLE pension (id_pension INTEGER, rsocial TEXT, rut TEXT, id_servicio TEXT,nameservice TEXT, desde TEXT, hasta TEXT)";
    final String CREAR_TABLA_REGISTRO="CREATE TABLE registro (id INTEGER PRIMARY KEY AUTOINCREMENT, trabajador_id TEXT, pension_id TEXT, servicio_id TEXT, hora TEXT, fecha TEXT, estado TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREAR_TABLA_TRABAJADOR);
        db.execSQL(CREAR_TABLA_SERVICIOS);
        db.execSQL(CREAR_TABLA_REGISTRO);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

        db.execSQL("DROP TABLE IF EXISTS trabajador");
        db.execSQL("DROP TABLE IF EXISTS pension");
        db.execSQL("DROP TABLE IF EXISTS registro");

        db.execSQL(CREAR_TABLA_TRABAJADOR);
        db.execSQL(CREAR_TABLA_SERVICIOS);
        db.execSQL(CREAR_TABLA_REGISTRO);
    }

    public boolean InsertarTrabajador(String id, String nombre, String apellido, String rut, String pass, String estado) {

        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues contentValues = new ContentValues();

            contentValues.put(ID,id);
            contentValues.put(NOMBRE, nombre);
            contentValues.put(APELLIDO, apellido);
            contentValues.put(RUT, rut);
            contentValues.put(PASS, pass);
            contentValues.put(ESTADO, estado);

            db.insert("trabajador",null,contentValues);
            db.close();
            return true;
        }catch(Exception exp){
            exp.printStackTrace();
            return false;
        }

    }

    public boolean InsertarRegistro(String trabajador_id, String pension_id, String servicio_id, String hora, String fecha, String estado) {

        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put(TRABAJADOR_ID, trabajador_id);
            contentValues.put(PENSION_ID, pension_id);
            contentValues.put(SERVICIO_ID, servicio_id);
            contentValues.put(HORA, hora);
            contentValues.put(FECHA, fecha);
            contentValues.put(ESTADO_REGISTRO,estado);

            db.insert("registro",null,contentValues);
            return true;
        }catch(Exception exp){
            exp.printStackTrace();
            return false;
        }

    }

    public boolean InsertarPension(String id_pension, String rsocial, String rut, String id_servicio,String nameservice, String desde, String hasta){
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put(ID_PENSION, id_pension);
            contentValues.put(RAZON_SOCIAL, rsocial);
            contentValues.put(RUT_PENSION, rut);
            contentValues.put(ID_SERVICIO, id_servicio);
            contentValues.put(NAMESERVICE,nameservice);
            contentValues.put(DESDE, desde);
            contentValues.put(HASTA, hasta);

            db.insert("pension",null,contentValues);
            db.close();
            return true;
        }catch(Exception exp){
            exp.printStackTrace();
            return false;
        }
    }


}
