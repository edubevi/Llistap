package edu.upc.damo.llistapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import edu.upc.damo.llistapp.Adapters.AdapterMenuItems;

public class MenuPrincipal extends AppCompatActivity {

    private static final int ACT_ESTUDIANTS = 0, ACT_ASSIGNATURES = 1, ACT_ASSISTENCIES = 2,
    ACT_EINES = 3;
    private AdapterMenuItems mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        init();
    }

    private void init(){
        initRecycleView();
    }

    private void initRecycleView() {
        RecyclerView RVmenuItems = findViewById(R.id.rv_llistaMenuItems);
        RVmenuItems.setHasFixedSize(true);
        RVmenuItems.setLayoutManager(new GridLayoutManager(this, 2));

        String[] itemsName = new String[] {"Estudiants","Assignatures","Assistencies","Eines"};
        int[] itemsImage = new int[] {R.drawable.ic_estudiant, R.drawable.ic_assignatura,
                R.drawable.ic_assistencia, R.drawable.ic_eines};

        mAdapter = new AdapterMenuItems(itemsName, itemsImage);
        RVmenuItems.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new AdapterMenuItems.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                initNewActivity(position);
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
                break;
            case ACT_EINES:
                i.setClass(MenuPrincipal.this,GestioEines.class);
        }
        startActivity(i);
    }
}
