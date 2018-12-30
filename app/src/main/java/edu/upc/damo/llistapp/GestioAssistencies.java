package edu.upc.damo.llistapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerViewAccessibilityDelegate;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.upc.damo.llistapp.Adapters.AdapterAssistencies;
import edu.upc.damo.llistapp.DB.DBManager;
import edu.upc.damo.llistapp.Entitats.Assignatura;
import edu.upc.damo.llistapp.Entitats.Assistencia;

public class GestioAssistencies extends AppCompatActivity {

    private final int ADD_ASSISTENCIA_REQUEST = 254;
    private final int EDIT_ASSISTENCIA_REQUEST = 255;
    public static final String EXTRA_PRESENTS = "upc.edu.damo.llistap.EXTRA_PRESENTS";
    public static final String EXTRA_ABSENTS = "upc.edu.damo.llistap.EXTRA_ABSENTS";
    public static final String EXTRA_POS = "upc.edu.damo.llistap.EXTRA_POS";
    public static final String EXTRA_ID_ASSISTENCIA = "upc.edu.damo.llistap.EXTRA_ID_ASSISTENCIA";
    public static final String EXTRA_DATE_ASSISTENCIA = "upc.edu.damo.llistap.EXTRA_DATE_ASSISTENCIA";

    private DBManager mConn;
    private AdapterAssistencies mAdapter;
    private String mNomAssignatura = "", mAliasAssignatura = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Assistencies");
        setContentView(R.layout.activity_gestio_assistencies);
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            ArrayList<String> dniPresents = data.getStringArrayListExtra(EXTRA_PRESENTS);
            ArrayList<String> dniAbsents = data.getStringArrayListExtra(EXTRA_ABSENTS);
            Assistencia newAssistencia = new Assistencia(mNomAssignatura,
                    data.getLongExtra(EXTRA_DATE_ASSISTENCIA,0), dniPresents);
            switch (requestCode){
                case ADD_ASSISTENCIA_REQUEST:
                    long inserData = mConn.insereixAssistencia(newAssistencia,mNomAssignatura,
                            dniPresents, dniAbsents);
                    mAdapter.notifyDataSetChanged();
                    if(inserData == -1) toastMessage("ERROR al afegir Assistencia a la Base de Dades");
                    else {
                        toastMessage("Assistencia afegida");
                        newAssistencia.setId((int)inserData);
                        mAdapter.insertAssistenciaToList(newAssistencia);
                        mAdapter.notifyDataSetChanged();
                    }
                    break;

                case EDIT_ASSISTENCIA_REQUEST:
                    int index = data.getIntExtra(EXTRA_POS, 0);
                    boolean upd = true;
                    Assistencia a = mAdapter.getAssistenciaFromList(index);
                    // Actualitzem la taula EstudiantAssistencia
                    for(String dni : dniPresents){
                        upd = mConn.updateEstudiantAssistencia(dni, a.getId(),true);
                        if(!upd) {
                            toastMessage("ERROR al actualitzar EstudiantAssistencia a la Base de Dades");
                            break;
                        }
                    }
                    for(String dni : dniAbsents){
                        upd = mConn.updateEstudiantAssistencia(dni, a.getId(),false);
                        if(!upd) {
                            toastMessage("ERROR al actualitzar EstudiantAssistencia a la Base de Dades");
                            break;
                        }
                    }
                    if(upd) toastMessage("Assistencia modificada");
                    mAdapter.notifyItemChanged(index);
                    break;
            }

        }
        else toastMessage("Assignatura no guardada");
    }

    // Init methods
    private void init() {
        mConn = new DBManager(getApplicationContext());
        initSpinner();
        initRecycleView();
        FloatingActionButton mFABAssistencia = findViewById(R.id.fab_addAssistencia);

        mFABAssistencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mNomAssignatura.isEmpty()) toastMessage("Cal seleccionar una assignatura.");
                else {
                    Long date = System.currentTimeMillis();
                    Intent addAssistencia = new Intent(
                            GestioAssistencies.this, AddEditAssistencia.class);
                    addAssistencia.putExtra(GestioAssignatures.EXTRA_NOM_ASSIG, mNomAssignatura);
                    addAssistencia.putExtra(GestioAssignatures.EXTRA_ALIAS_ASSIG, mAliasAssignatura);
                    addAssistencia.putExtra(EXTRA_DATE_ASSISTENCIA, date);
                    startActivityForResult(addAssistencia, ADD_ASSISTENCIA_REQUEST);
                }
            }
        });
    }

    private void initSpinner() {
        Spinner mSpinner = findViewById(R.id.spn_assist);
        List<Assignatura> assignaturaList = mConn.getAssignaturesList();
        ArrayAdapter<Assignatura> adapter = new ArrayAdapter<Assignatura>(this,
                android.R.layout.simple_spinner_item, assignaturaList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 //Capturem el nom de l'assignatura que s'ha seleccionat des d'el spinner.
                Assignatura assignatura = (Assignatura) parent.getSelectedItem();
                // Repoblem el recyclerview amb les seves assistències.
                updateRecycleView(assignatura.getNom());
                mNomAssignatura = assignatura.getNom();
                mAliasAssignatura = assignatura.getAlias();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void initRecycleView() {
        RecyclerView RVassistencies = (RecyclerView) findViewById(R.id.rv_llistaAssistencies);
        RVassistencies.setHasFixedSize(true);
        RVassistencies.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new AdapterAssistencies(mConn.getAssistenciesAssignaturaList(mNomAssignatura));
        RVassistencies.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new AdapterAssistencies.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent editAssistencia = new Intent(GestioAssistencies.this, AddEditAssistencia.class);
                // Enviem Parametres
                Assistencia a = mAdapter.getAssistenciaFromList(position);
                editAssistencia.putExtra(GestioAssignatures.EXTRA_NOM_ASSIG, mNomAssignatura);
                editAssistencia.putExtra(GestioAssignatures.EXTRA_ALIAS_ASSIG, mAliasAssignatura);
                editAssistencia.putExtra(EXTRA_ID_ASSISTENCIA, a.getId());
                editAssistencia.putExtra(EXTRA_DATE_ASSISTENCIA, a.getDate());
                editAssistencia.putExtra(EXTRA_POS, position);
                editAssistencia.putStringArrayListExtra(EXTRA_PRESENTS, (ArrayList<String>)
                mConn.getDniEstudiantsPresentsList(a.getId()));
                startActivityForResult(editAssistencia, EDIT_ASSISTENCIA_REQUEST);
            }

            @Override
            public void onDeleteClick(final int position) {
                final AlertDialog.Builder deleteDialog = new AlertDialog.Builder(GestioAssistencies.this);
                final int pos = position;
                deleteDialog.setTitle("Listapp");
                deleteDialog.setMessage("Vols eleiminar l'assistència? ");
                deleteDialog.setPositiveButton("ELIMINA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mConn.deleteAssistencia(mAdapter.getAssistenciaFromList(pos).getId());
                        mAdapter.notifyItemRemoved(pos);
                        mAdapter.deleteAssistenciaFromList(pos);
                        dialog.dismiss();
                    }
                });
                deleteDialog.setNegativeButton("CANCEL·LA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                deleteDialog.show();
            }
        });
    }

    private void updateRecycleView(String nomAssignatura) {
        mAdapter.setmListAssistencies(mConn.getAssistenciesAssignaturaList(nomAssignatura));
        mAdapter.notifyDataSetChanged();
    }

    //Auxiliar methods
    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}
