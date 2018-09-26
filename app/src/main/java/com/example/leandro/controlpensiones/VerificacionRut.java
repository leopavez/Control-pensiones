package com.example.leandro.controlpensiones;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class VerificacionRut extends AppCompatActivity {

    Button validar;
    EditText pass;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verificacion_rut);
        validar=(Button)findViewById(R.id.btnvalidacion);
        pass= (EditText)findViewById(R.id.txtpass);


        validar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passaverificar = pass.getText().toString();
                if (passaverificar.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "CAMPO VACIO", Toast.LENGTH_SHORT).show();
                } else {
                    VerificarPassword();

                }
            }
        });

    }


    public void VerificarPassword(){

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        final String password = extras.getString("PASSWORD");
        final String estadot = extras.getString("ESTADO");
        final String rut=extras.getString("RUT");

        String passaverificar= pass.getText().toString();
        if (passaverificar.equals(password)){
            Intent nuevoform= new Intent(VerificacionRut.this, Splashverificacion.class);
            nuevoform.putExtra("ESTADO",estadot);
            nuevoform.putExtra("RUT",rut);
            startActivity(nuevoform);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else{
            Toast.makeText(getApplicationContext(), "PASSWORD INCORRECTA", Toast.LENGTH_SHORT).show();
            pass.setText("");
            pass.findFocus();
        }

    }

}
