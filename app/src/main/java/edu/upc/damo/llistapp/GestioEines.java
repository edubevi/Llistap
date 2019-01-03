package edu.upc.damo.llistapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import edu.upc.damo.llistapp.Adapters.AdapterEines;
import edu.upc.damo.llistapp.DB.DBManager;



public class GestioEines extends AppCompatActivity{

    private static final int T_ESTUDIANTS = 0, T_ASSIGNATURES = 1, T_ASSISTENCIES = 2;
    private AdapterEines mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestio_eines);
        init();
    }

    private void init() { initRecyclerView(); }

    private void initRecyclerView() {
        RecyclerView RVeines = findViewById(R.id.rv_llistaEines);
        RVeines.setHasFixedSize(true);
        RVeines.setLayoutManager(new LinearLayoutManager(this));

        String[] itemsName = new String[] {"Esborra dades Estudiants", "Esborra dades Assignatures",
                 "Esborra dades Assistències"};
        String[] itemsDesc = new String[] {"Esborra tots els estudiants de la base de dades",
                 "Esborra totes les assignatures de la base de dades",
                "Esborra totes les assistències de la base de dades"};

        mAdapter = new AdapterEines(itemsName, itemsDesc);
        RVeines.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new AdapterEines.OnItemClickListener() {
            @Override
            public void onItemClick(int position) { deleteTableDialog(position); }
        });
    }

    private void deleteTableDialog(int position) {
        final AlertDialog.Builder deleteDialog = new AlertDialog.Builder(GestioEines.this);
        final int pos = position;
        deleteDialog.setTitle("Listapp");
        deleteDialog.setMessage("Estas segur que vols continuar? S'esborraran tots els elements de la base de dades");
        deleteDialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(pos){
                    case T_ESTUDIANTS:
                        //esborra taula estudiants
                        deleteDBtable("Estudiants");
                        break;
                    case T_ASSIGNATURES:
                        //esborra taula assignatures
                        deleteDBtable("Assignatures");
                        break;
                    case T_ASSISTENCIES:
                        //esborra taula assistencies
                        deleteDBtable("Assistencies");
                        break;
                }
                dialog.dismiss();
            }
        });
        deleteDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { dialog.dismiss(); }
        });
        deleteDialog.show();
    }

    private void deleteDBtable(String tableName){
        DBManager conn = new DBManager(getApplicationContext());
        conn.deleteTable(tableName);
    }

}
