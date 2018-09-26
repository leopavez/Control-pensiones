package com.example.leandro.controlpensiones;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class Ajustes extends AppCompatActivity {

    private static final String TAG = Ajustes.class.getName();

    Button config;
    EditText idpension;
    DatabaseHelper myDB;

    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ajustes);
        config = (Button) findViewById(R.id.btnconfigurar);
        idpension = (EditText) findViewById(R.id.txtidpension);

        config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                configurarApp();
            }
        });

        myDB = new DatabaseHelper(this);

    }


    public void configurarApp() {
            String id=idpension.getText().toString();

            if (id.isEmpty()){
                Toast.makeText(getApplicationContext(),"CAMPO VACIO",Toast.LENGTH_SHORT).show();
            }else {
                String URLPensionEspecifica = "http://santafeinversiones.com/services/pension" + "/" + "" + id;

                mRequestQueue = Volley.newRequestQueue(this);
                mStringRequest = new StringRequest(Request.Method.GET, URLPensionEspecifica, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            String json;
                            Log.i(TAG, "Response:" + response.toString());

                            json = response.toString();
                            JSONArray jsonarr = null;
                            jsonarr = new JSONArray(json);
                            pension p = new pension();


                            SQLiteDatabase db = myDB.getWritableDatabase();
                            db.execSQL("DELETE FROM pension");
                            for (int i = 0; i < jsonarr.length(); i++) {
                                JSONObject jsonObject = jsonarr.getJSONObject(i);

                                p.id = jsonObject.getString("pension_id");
                                p.rsocial = jsonObject.getString("rsocial");
                                p.rut = jsonObject.getString("rut");
                                p.id_servicio = jsonObject.getString("servicios_id");
                                p.desde = jsonObject.getString("desde");
                                p.hasta = jsonObject.getString("hasta");


                                myDB.InsertarPension(p.id,p.rsocial,p.rut,p.id_servicio,p.desde,p.hasta);


                            }
                            Intent nuevoform= new Intent(Ajustes.this, MainActivity.class);
                            startActivity(nuevoform);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            Toast.makeText(getApplicationContext(), "PARAMETROS DE CONFIGURACION ACTUALIZADOS", Toast.LENGTH_SHORT).show();
                            Log.d("MSG","TU NUEVA PENSION ES: "+p.rsocial);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "Error de conexion:" + error.toString());
                    }
                });
                mRequestQueue.add(mStringRequest);
            }
            }
            }











