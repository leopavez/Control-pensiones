package com.example.leandro.controlpensiones;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class Splashverificacion extends AppCompatActivity {


    ProgressBar progress;
    ImageView tickverificacion;
    ImageView xCancelacion;
    DatabaseHelper myDB;
    Main_Activity ma;

    protected static final String TAG = "TAG";
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    BluetoothAdapter mBluetoothAdapter;
    private UUID applicationUUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothSocket mBluetoothSocket;
    BluetoothDevice mBluetoothDevice;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gifverificacion);
        progress = (ProgressBar)findViewById(R.id.progressBar);
        tickverificacion= (ImageView)findViewById(R.id.tick);
        tickverificacion.setVisibility(View.INVISIBLE);
        xCancelacion=(ImageView)findViewById(R.id.x);
        xCancelacion.setVisibility(View.INVISIBLE);

        myDB = new DatabaseHelper(this);
        ma = new Main_Activity();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();;
        final String estadot = extras.getString("ESTADO");
        final String rut=extras.getString("RUT");


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (estadot.equals("ACTIVO")){

                    SQLiteDatabase db = myDB.getWritableDatabase();


                    String fecha;
                    String hora;

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    SimpleDateFormat horaformat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                    Date date = new Date();
                    fecha = dateFormat.format(date);
                    hora = horaformat.format(date);


                    int flag=0;

                    Cursor cursor = db.rawQuery("SELECT id FROM trabajador WHERE rut='"+rut+"'",null);
                    if (cursor.moveToFirst()==true){
                        String trabajadorid = cursor.getString(0);
                            Cursor cursor1 = db.rawQuery("SELECT id_servicio FROM pension",null);
                            for (cursor1.moveToFirst(); !cursor1.isAfterLast(); cursor1.moveToNext()){
                                String idservicio=cursor1.getString(0);

                                Cursor cursor2 = db.rawQuery("SELECT desde, hasta FROM pension WHERE id_servicio='"+idservicio+"'",null);

                                for(cursor2.moveToFirst(); !cursor2.isAfterLast(); cursor2.moveToNext()){

                                    String horadesde = cursor2.getString(0);
                                    String horahasta = cursor2.getString(1);


                                    String horaLocal=hora.replace(":","");
                                    String horaDesdeInt = horadesde.replace(":","");
                                    String horaHastaInt = horahasta.replace(":","");

                                    int HoraLocalOK= Integer.parseInt(horaLocal);
                                    int HoraDesdeOK= Integer.parseInt(horaDesdeInt);
                                    int HoraHastaOK= Integer.parseInt(horaHastaInt);



                                    if(HoraLocalOK>=HoraDesdeOK && HoraLocalOK<=HoraHastaOK){


                                        Cursor cursor3 = db.rawQuery("SELECT id_pension, id_servicio FROM pension LIMIT 1",null);

                                        if (cursor3.moveToFirst()==true) {
                                            Cursor cursor4 = db.rawQuery("SELECT * FROM registro WHERE fecha='"+fecha+"' AND servicio_id='"+idservicio+"' AND trabajador_id='"+trabajadorid+"' ",null);
                                            if(cursor4.moveToFirst()==true){
                                                Toast.makeText(getApplicationContext(), "SERVICIO YA CONSUMIDO", Toast.LENGTH_SHORT).show();
                                            }else{

                                                flag = Integer.parseInt(idservicio);
                                            }

                                        }

                                    }


                                }
                            }

                            if(flag!=0){


                                Cursor cursor3 = db.rawQuery("SELECT id_pension, id_servicio FROM pension WHERE id_servicio='"+flag+"'",null);

                                    if (cursor3.moveToFirst()==true){
                                        try {
                                            ma.imprimir();
                                            String id_pension = cursor3.getString(0);
                                            String idservicio = cursor3.getString(1);
                                            myDB.InsertarRegistro(trabajadorid,id_pension,idservicio,hora,fecha,"PENDIENTE");
                                            progress.setVisibility(View.INVISIBLE);
                                            tickverificacion.setVisibility(View.VISIBLE);

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent nuevoform= new Intent(Splashverificacion.this, MainActivity.class);
                                            startActivity(nuevoform);
                                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                                        }
                                    },2000);

                                }

                            }else {
                                progress.setVisibility(View.INVISIBLE);
                                xCancelacion.setVisibility(View.VISIBLE);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent nuevoform= new Intent(Splashverificacion.this,MainActivity.class);
                                        startActivity(nuevoform);
                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                                    }
                                },2000);
                            }

                    }




                }else{
                    progress.setVisibility(View.INVISIBLE);
                    xCancelacion.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent nuevoform= new Intent(Splashverificacion.this, MainActivity.class);
                            startActivity(nuevoform);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                        }
                    },2000);
                }


           //
            }
        }, 3000);






    }

}