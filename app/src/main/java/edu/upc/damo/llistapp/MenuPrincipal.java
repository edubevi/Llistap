package edu.upc.damo.llistapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuPrincipal extends AppCompatActivity {

    Button alumnes, assignatures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        inicialitza();
    }

    private void inicialitza(){
        alumnes = (Button) findViewById(R.id.btn_alumnes);
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
    }

    private void mostraGestioAlumnes(View v){

    }

    private void mostraGestioAssignatures(View v){

    }

}
