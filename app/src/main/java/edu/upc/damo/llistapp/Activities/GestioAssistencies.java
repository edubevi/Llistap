package edu.upc.damo.llistapp.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import edu.upc.damo.llistapp.Adapters.AdapterAssistencies;
import edu.upc.damo.llistapp.Models.ModelAssistencia;
import edu.upc.damo.llistapp.Objectes.Assignatura;
import edu.upc.damo.llistapp.Objectes.Assistencia;
import edu.upc.damo.llistapp.R;
import edu.upc.damo.llistapp.Utils.Utils;

public class GestioAssistencies extends AppCompatActivity {

    private final int ADD_ASSISTENCIA_REQUEST = 254;
    private final int EDIT_ASSISTENCIA_REQUEST = 255;

    private AdapterAssistencies adapter;
    private ModelAssistencia model;

    private RecyclerView rv_assistencies;
    private TextView empty_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Assistències");
        setContentView(R.layout.activity_gestio_assistencies);
        init();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            ArrayList<String> dniPresents = data.getStringArrayListExtra(Utils.EXTRA_PRESENTS);
            ArrayList<String> dniAbsents = data.getStringArrayListExtra(Utils.EXTRA_ABSENTS);
            Assistencia newAssistencia = new Assistencia(model.getNom_assignatura(),
                    data.getLongExtra(Utils.EXTRA_DATE_ASSISTENCIA, 0));
            switch (requestCode){
                case ADD_ASSISTENCIA_REQUEST:
                    if(model.addAssistencia(newAssistencia, dniPresents, dniAbsents)){
                        adapter.notifyDataSetChanged();
                    }
                    break;

                case EDIT_ASSISTENCIA_REQUEST:
                    int index = data.getIntExtra(Utils.EXTRA_POS, 0);
                    newAssistencia.setId(data.getIntExtra(Utils.EXTRA_ID_ASSISTENCIA,0));
                    model.editAssistencia(index, newAssistencia, dniPresents, dniAbsents);
                    /* No avisem al adapter ja que en realitat no modifiquem cap element que infli
                    el propi adapter.*/
                    break;
            }
        }
    }

    // Init methods
    private void init() {
        model = new ModelAssistencia(this);
        initSpinner();
        initRecycleView();
        FloatingActionButton fab_assistencia = findViewById(R.id.fab_addAssistencia);

        fab_assistencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nom_assignatura = model.getNom_assignatura();
                if(nom_assignatura.isEmpty()) {
                    Utils.toastMessage("Cal seleccionar una assignatura", getApplicationContext());
                }
                else {
                    String alias_asssignatura = model.getAlias_assignatura();
                    Long date = System.currentTimeMillis();
                    Intent addAssistencia = new Intent(
                            GestioAssistencies.this, AddEditAssistencia.class);
                    addAssistencia.putExtra(Utils.EXTRA_NOM_ASSIG, nom_assignatura);
                    addAssistencia.putExtra(Utils.EXTRA_ALIAS_ASSIG, alias_asssignatura);
                    addAssistencia.putExtra(Utils.EXTRA_DATE_ASSISTENCIA, date);
                    startActivityForResult(addAssistencia, ADD_ASSISTENCIA_REQUEST);
                }
            }
        });
    }

    private void initSpinner() {
        Spinner mSpinner = findViewById(R.id.spn_assist);
        ArrayAdapter<Assignatura> spn_adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, model.getAssignatures());
        spn_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(spn_adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Capturem el nom i l'alias de l'assignatura que s'ha seleccionat des d'el spinner.
                Assignatura a = (Assignatura) parent.getSelectedItem();
                model.setNom_assignatura(a.getNom());
                model.setAlias_assignatura(a.getAlias());
                // Repoblem el recyclerview amb les seves assistències.
                updateRecycleView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void initRecycleView() {
        rv_assistencies = findViewById(R.id.rv_llistaAssistencies);
        empty_view = findViewById(R.id.empty_view_assist);
        rv_assistencies.setHasFixedSize(true);
        rv_assistencies.setLayoutManager(new LinearLayoutManager(this));

        adapter = new AdapterAssistencies(model);
        rv_assistencies.setAdapter(adapter);

        /* Cridem a una funció que amaga la vista corresponent al RecyclerView en el cas que la llista
        dels items que l'han de poblar estigui buida i visualitza un TextView informant d'aquesta situació.
        En el cas contrari, fa el procés invers. */
        Utils.setViewsToShow(model.getAssistencies().size(), rv_assistencies, empty_view);

        /* Amaga o visualitza la empty_view en funció de si afegim o eliminem items de la RecyclerView. */
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                Utils.setViewsToShow(model.getAssistencies().size(), rv_assistencies, empty_view);
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                Utils.setViewsToShow(model.getAssistencies().size(), rv_assistencies, empty_view);
            }
        });

        adapter.setOnItemClickListener(new AdapterAssistencies.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent editAssistencia = new Intent(GestioAssistencies.this, AddEditAssistencia.class);
                // Enviem Parametres
                Assistencia assist = model.getAssistencia(position);
                editAssistencia.putExtra(Utils.EXTRA_NOM_ASSIG, model.getNom_assignatura());
                editAssistencia.putExtra(Utils.EXTRA_ALIAS_ASSIG, model.getAlias_assignatura());
                editAssistencia.putExtra(Utils.EXTRA_ID_ASSISTENCIA, assist.getId());
                editAssistencia.putExtra(Utils.EXTRA_DATE_ASSISTENCIA, assist.getDate());
                editAssistencia.putExtra(Utils.EXTRA_POS, position);
                startActivityForResult(editAssistencia, EDIT_ASSISTENCIA_REQUEST);
            }

            @Override
            public void onDeleteClick(final int position) {
                final AlertDialog.Builder deleteDialog = new AlertDialog.Builder(GestioAssistencies.this);
                deleteDialog.setTitle("Listapp");
                deleteDialog.setMessage("Vols eleiminar l'assistència? ");
                deleteDialog.setPositiveButton("ELIMINA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(model.deleteAssistencia(position)){
                            adapter.notifyItemRemoved(position);
                        }
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

    private void updateRecycleView() {
        adapter.setAssistencies(model.getAssistencies());
        adapter.notifyDataSetChanged();
    }
}
