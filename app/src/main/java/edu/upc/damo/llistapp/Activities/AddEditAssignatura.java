package edu.upc.damo.llistapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import edu.upc.damo.llistapp.Adapters.AdapterEstudiantsAssignatura;
import edu.upc.damo.llistapp.Models.ModelEstudiantAssignatura;
import edu.upc.damo.llistapp.R;
import edu.upc.damo.llistapp.Utils.Utils;

public class AddEditAssignatura extends AppCompatActivity {

    private EditText et_nom_assig, et_alias_assig;
    private ModelEstudiantAssignatura model;
    private boolean is_edit_activity = false;
    private RecyclerView rv_estudiants;
    private TextView empty_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assignatura);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        inicialitza();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_nou_estudiant, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_add:
                boolean emptyFields = checkEmptyFields();
                if(emptyFields)Utils.toastMessage(
                        "Cal omplir tots el camps i seleccionar almenys un matriculat", this);
                else {
                    Intent resultat = new Intent();
                    resultat.putExtra(Utils.EXTRA_NOM_ASSIG, et_nom_assig.getText().toString());
                    resultat.putExtra(Utils.EXTRA_ALIAS_ASSIG, et_alias_assig.getText().toString());
                    //Enviem els dni dels estudiants matricualts.
                    resultat.putStringArrayListExtra(Utils.EXTRA_MATRICULATS,
                            model.getDniMatriculatsList());
                    if(is_edit_activity){
                        Intent i = getIntent();
                        resultat.putExtra(Utils.EXTRA_POS, i.getIntExtra(Utils.EXTRA_POS,0));
                    }
                    setResult(RESULT_OK, resultat);
                    finish();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Init methods
    private void inicialitza() {
        et_nom_assig = findViewById(R.id.et_nomAssignatura);
        et_alias_assig = findViewById(R.id.et_aliasAssignatura);
        Intent intent = getIntent();
        if(intent.hasExtra(Utils.EXTRA_NOM_ASSIG)){
            setTitle("Edit Assignatura");
            is_edit_activity = true;
            et_nom_assig.setText(intent.getStringExtra(Utils.EXTRA_NOM_ASSIG));
            et_alias_assig.setText(intent.getStringExtra(Utils.EXTRA_ALIAS_ASSIG));
        }
        else setTitle("Nova Assignatura");
        model = new ModelEstudiantAssignatura(this, et_nom_assig.getText().toString());
        initRecycleView();
    }

    private void initRecycleView() {
        rv_estudiants = findViewById(R.id.rv_llistaEstudiantsCb);
        empty_view = findViewById(R.id.empty_view_estAssig);
        rv_estudiants.setHasFixedSize(true);
        rv_estudiants.setLayoutManager(new LinearLayoutManager(this));

        AdapterEstudiantsAssignatura adapter = new AdapterEstudiantsAssignatura(model);
        rv_estudiants.setAdapter(adapter);

        /* Cridem a una funció que amaga la vista corresponent al RecyclerView en el cas que la llista
        dels items que l'han de poblar estigui buida i visualitza un TextView informant d'aquesta situació.
        En el cas contrari, fa el procés invers. */
        Utils.setViewsToShow(model.getEstudiants().size(), rv_estudiants, empty_view);

        adapter.setOnItemClickListener(new AdapterEstudiantsAssignatura.OnItemClickListener() {

            @Override
            public void onCheckBox(int position, boolean checked) {
                if(checked) model.addMatriculat(position);
                else model.removeMatriculat(position);
            }
        });
    }

    private boolean checkEmptyFields(){
        return et_nom_assig.getText().length() == 0 || et_alias_assig.getText().length() == 0 ||
                model.getDniMatriculatsList().size() == 0;
    }
}
