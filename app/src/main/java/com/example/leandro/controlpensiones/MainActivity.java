package com.example.leandro.controlpensiones;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG =MainActivity.class.getName() ;
    EditText rut;
    Button siguiente;
    FloatingActionButton ajustes;

    ArrayList<trabajador> listatrabajadores;

    ArrayList<registro> listaregistros;

    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;

    String id, nombre, apellido, rutt, pass, estado;

    Cursor cursor;


    DatabaseHelper myDB;
    final static String urltrabajadores="http://santafeinversiones.com/services/trabajador";
    final static String urlENVIO="http://santafeinversiones.com/services/regpensiones";
    trabajador trabajador= new trabajador();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rut = (EditText) findViewById(R.id.txtrut);
        siguiente = (Button) findViewById(R.id.siguiente);
        ajustes = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        Actualizar actualizar = new Actualizar();
        actualizar.execute();

        myDB = new DatabaseHelper(this);


        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rutaverificar = rut.getText().toString();
                if (rutaverificar.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "CAMPO VACIO", Toast.LENGTH_SHORT).show();
                } else {
                    buscarRutenDB();

                }
            }


        });

        ajustes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nuevoform = new Intent(MainActivity.this, CSetting.class);
                startActivityForResult(nuevoform, 1);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==1){
            if (resultCode==RESULT_OK){
                Toast.makeText(this, "Operacion exitosa", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"Codigo incorrecto",Toast.LENGTH_SHORT).show();
            }
        }
    }

public void ejecutar() {

    try {
        Thread.sleep(10000);
        actualizaciontrabajadores();
        EnviodeDatos();
    }catch (InterruptedException e){
        e.printStackTrace();
    }


}



    public class Actualizar extends AsyncTask<Void,Integer,Boolean>
    {

        @Override
        protected Boolean doInBackground(Void... voids) {
            for(int i = 1; i<=3;i++){
            ejecutar();
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            Actualizar actualizar = new Actualizar();
            actualizar.execute();
        }
    }


    public void EnviodeDatos(){

        try {
            String estado = "PENDIENTE";
            SQLiteDatabase db = myDB.getWritableDatabase();


            cursor= db.rawQuery("SELECT id,trabajador_id,pension_id,servicio_id,hora,fecha FROM registro WHERE estado='"+estado+"'",null);
            registro regis= new registro();
            listaregistros = new ArrayList<>();

            for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                regis.setId(cursor.getInt(0));
                final String id = String.valueOf(regis.getId());
                regis.setTrabajador_id(cursor.getString(1));
                final String trabajador_id = regis.getTrabajador_id();
                regis.setPension_id(cursor.getString(2));
                final String pension_id = regis.getPension_id();
                regis.setServicio_id(cursor.getString(3));
                final String servicio_id = regis.getServicio_id();
                regis.setHora(cursor.getString(4));
                final String hora= regis.getHora();
                regis.setFecha(cursor.getString(5));
                final String fecha = regis.getFecha();

                mRequestQueue = Volley.newRequestQueue(this);
                mStringRequest = new StringRequest(Request.Method.POST, urlENVIO, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("MSG" , "RESPONSE: "+response);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG,"Error de conexion:" +error.toString());
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String>params = new HashMap<String,String>();
                        params.put("id",id);
                        params.put("trabajador_id",trabajador_id);
                        params.put("pension_id",pension_id);
                        params.put("servicio_id",servicio_id);
                        params.put("hora",hora);
                        params.put("fecha",fecha);
                        return params;


                    }
                };
                mRequestQueue.add(mStringRequest);

                db.execSQL("UPDATE registro SET estado='ENVIADO' WHERE id="+id+"");


            }



        }catch (Exception e){
            e.printStackTrace();
        }



    }



    public void actualizaciontrabajadores() {



            mRequestQueue = Volley.newRequestQueue(this);
            mStringRequest = new StringRequest(Request.Method.GET, urltrabajadores,new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        String json;
                        Log.i(TAG,"Response:"+ response.toString());

                        json=response.toString();
                        JSONArray jsonarr = null;
                        jsonarr = new JSONArray(json);
                        trabajador t = new trabajador();

                        for (int i = 0; i<jsonarr.length(); i++){
                            JSONObject jsonObject = jsonarr.getJSONObject(i);

                            t.id = jsonObject.getString("id");
                            t.nombre = jsonObject.getString("nombre");
                            t.apellido = jsonObject.getString("apellido");
                            t.rut = jsonObject.getString("rutenvio");
                            t.pass = jsonObject.getString("pass");
                            t.estado = jsonObject.getString("estado");

                            SQLiteDatabase db = myDB.getWritableDatabase();

                            Cursor cursor = db.rawQuery("SELECT id FROM trabajador WHERE id='"+t.id+"'",null);

                            if (cursor.getCount()<=0){
                               //NO SE ENCUENTRA AL TRABAJADOR
                                myDB.InsertarTrabajador(t.id,t.nombre,t.apellido,t.rut,t.pass,t.estado);
                                Log.d("MSG","TRABAJADOR AGREGADO");

                            }else{
                                Log.d("MSG", "TRABAJADOR SI EXISTE");
                                Cursor cursor1 = db.rawQuery("SELECT estado FROM trabajador WHERE id='"+t.id+"'",null);

                                if(cursor1.moveToFirst()==true){
                                    String estadotrabajador=cursor1.getString(0);
                                    Log.d("MSG","ESTADO "+estadotrabajador);

                                    if(t.estado.toString().equals(estadotrabajador)){
                                        Log.d("MSG","ES EL MISMO ESTADO");
                                    }else{

                                        db.execSQL("UPDATE trabajador SET estado='"+t.estado.toString()+"' WHERE id='"+t.id+"'");
                                        Log.d("MSG","TRABAJADOR ACTUALIZADO");

                                    }

                                }

                            }


                        }



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i(TAG,"Error de conexion:" +error.toString());
                }
            });
            mRequestQueue.add(mStringRequest);
        }

        public void buscarRutenDB(){

            SQLiteDatabase db = myDB.getWritableDatabase();

            String txtrut=rut.getText().toString();

            Cursor cursor = db.rawQuery("SELECT pass,estado FROM trabajador WHERE rut='"+txtrut+"'",null);

            if(cursor.moveToFirst()==true){
                String password= cursor.getString(0);
                String estadot= cursor.getString(1);
                Intent nuevoform = new Intent(MainActivity.this, VerificacionRut.class);
                nuevoform.putExtra("PASSWORD",password);
                nuevoform.putExtra("ESTADO",estadot);
                nuevoform.putExtra("RUT",txtrut);
                startActivity(nuevoform);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }else{
                Toast.makeText(this,"RUT NO ENCONTRADO", Toast.LENGTH_SHORT).show();
            }

        }

    }

