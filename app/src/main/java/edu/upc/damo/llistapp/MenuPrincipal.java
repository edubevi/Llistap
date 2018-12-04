package edu.upc.damo.llistapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuPrincipal extends AppCompatActivity {

    Button alumnes, assignatures, assistencia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        inicialitza();
    }

    private void inicialitza(){
        alumnes = (Button)findViewById(R.id.btn_estudiants);
        alumnes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostraGestioAlumnes(v);
            }
        });

        assignatures = (Button)findViewById(R.id.btn_assign);
        assignatures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostraGestioAssignatures(v);
            }
        });

        assistencia = (Button)findViewById(R.id.btn_assist);
        assistencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostraGestioAssistencia(v);
            }
        });
    }

    private void mostraGestioAlumnes(View v){
        Intent i = new Intent(MenuPrincipal.this,GestioEstudiants.class);
        startActivity(i);
    }

    private void mostraGestioAssignatures(View v){
        Intent i = new Intent(MenuPrincipal.this,GestioAssignatures.class);
        startActivity(i);
    }

    private void mostraGestioAssistencia(View v){
        //Intent i = new Intent(MenuPrincipal.this,.class);
        //startActivity(i);
    }

}
