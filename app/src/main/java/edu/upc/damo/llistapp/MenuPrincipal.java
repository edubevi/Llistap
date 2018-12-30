package edu.upc.damo.llistapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuPrincipal extends AppCompatActivity {

    private static final int ACT_ESTUDIANTS = 1, ACT_ASSIGNATURES = 2, ACT_ASSISTENCIES = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        init();
    }

    private void init(){
        Button btn_Alumnes = findViewById(R.id.btn_estudiants);
        btn_Alumnes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initNewActivity(ACT_ESTUDIANTS);
            }
        });

        Button btn_Assignatures = findViewById(R.id.btn_assign);
        btn_Assignatures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initNewActivity(ACT_ASSIGNATURES);
            }
        });

        Button btn_Assistencies = findViewById(R.id.btn_assist);
        btn_Assistencies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initNewActivity(ACT_ASSISTENCIES);
            }
        });
    }

    private void initNewActivity(int activity){
        Intent i = new Intent();
        switch (activity){
            case ACT_ESTUDIANTS:
                i.setClass(MenuPrincipal.this, GestioEstudiants.class);
                break;
            case ACT_ASSIGNATURES:
                i.setClass(MenuPrincipal.this, GestioAssignatures.class);
                break;
            case ACT_ASSISTENCIES:
                i.setClass(MenuPrincipal.this, GestioAssistencies.class);
        }
        startActivity(i);
    }
}
