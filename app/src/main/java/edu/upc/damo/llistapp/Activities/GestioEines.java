package edu.upc.damo.llistapp.Activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import edu.upc.damo.llistapp.Adapters.AdapterEines;
import edu.upc.damo.llistapp.Models.ModelEines;
import edu.upc.damo.llistapp.R;


public class GestioEines extends AppCompatActivity{

    private static final int T_ESTUDIANTS = 0, T_ASSIGNATURES = 1, T_ASSISTENCIES = 2;
    private ModelEines model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestio_eines);
        init();
    }

    private void init() {
        model = new ModelEines(this);
        initRecyclerView();
    }

    private void initRecyclerView() {
        RecyclerView rv_eines = findViewById(R.id.rv_llistaEines);
        rv_eines.setHasFixedSize(true);
        rv_eines.setLayoutManager(new LinearLayoutManager(this));

        AdapterEines adapter = new AdapterEines(model);
        rv_eines.setAdapter(adapter);
        adapter.setOnItemClickListener(new AdapterEines.OnItemClickListener() {
            @Override
            public void onItemClick(int position) { deleteTableDialog(position); }
        });
    }

    private void deleteTableDialog(int position) {
        final AlertDialog.Builder deleteDialog = new AlertDialog.Builder(GestioEines.this);
        final int pos = position;
        deleteDialog.setTitle("Listapp");
        deleteDialog.setMessage("Estas segur que vols continuar? " +
                "S'esborraran tots els elements de la base de dades corresponents a aquesta taula.");
        deleteDialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(pos){
                    case T_ESTUDIANTS:
                        //esborra taula estudiants
                        model.deleteDBtable("Estudiants");
                        break;
                    case T_ASSIGNATURES:
                        //esborra taula assignatures
                        model.deleteDBtable("Assignatures");
                        break;
                    case T_ASSISTENCIES:
                        //esborra taula assistencies
                        model.deleteDBtable("Assistencies");
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
}
