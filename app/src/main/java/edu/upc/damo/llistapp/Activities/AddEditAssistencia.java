package edu.upc.damo.llistapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import edu.upc.damo.llistapp.Adapters.AdapterEstudiantsAssistencia;
import edu.upc.damo.llistapp.DB.DBManager;
import edu.upc.damo.llistapp.Models.ModelEstudiantAssistencia;
import edu.upc.damo.llistapp.R;
import edu.upc.damo.llistapp.Utils.Utils;

public class AddEditAssistencia extends AppCompatActivity {

    private ModelEstudiantAssistencia model;
    private TextView tv_info_assistencia;
    private boolean is_edit_activity = false;
    private Long date;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assistencia);
        inicialitza();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_nou_estudiant, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent resultat = new Intent();
                //Enviem el dni dels estudiants presents i absents amb una ArrayList
                resultat.putStringArrayListExtra(Utils.EXTRA_PRESENTS, model.getDniPresents());
                resultat.putExtra(Utils.EXTRA_DATE_ASSISTENCIA, date);
                resultat.putStringArrayListExtra(Utils.EXTRA_ABSENTS, model.getDniAbsents());
                if(is_edit_activity){
                    Intent i = getIntent();
                    resultat.putExtra(Utils.EXTRA_POS, i.getIntExtra(Utils.EXTRA_POS, 0));
                    resultat.putExtra(Utils.EXTRA_ID_ASSISTENCIA, model.getId_assistencia());
                }
                setResult(RESULT_OK, resultat);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Init Methods
    private void inicialitza() {
        Intent intent = getIntent();
        TextView tv_alias = findViewById(R.id.tv_assigAssist);
        tv_alias.setText(intent.getStringExtra(Utils.EXTRA_ALIAS_ASSIG));
        TextView tv_date = findViewById(R.id.tv_dataAssist);
        date = intent.getLongExtra(Utils.EXTRA_DATE_ASSISTENCIA, 0);
        tv_date.setText(Utils.getFormatDate(date));
        String nom_assignatura = intent.getStringExtra(Utils.EXTRA_NOM_ASSIG);
        model = new ModelEstudiantAssistencia(this, nom_assignatura,
                intent.getIntExtra(Utils.EXTRA_ID_ASSISTENCIA, 0));
        tv_info_assistencia = findViewById(R.id.tv_infoAssist);
        /* Comprovem si l'intent té la posició d'una assistència del recyclerview, si és així,
        estem editant una assistència i marquem l'atribut is_edit_activity a true.*/
        if(intent.hasExtra(Utils.EXTRA_POS)){
            setTitle("Edit Assistència");
            is_edit_activity = true;
            int presents = model.getNumPresents();
            int absents = model.getNumAbsents();
            tv_info_assistencia.setText(getString(R.string.tv_info_assistents, presents, absents));
        }
        else {
            setTitle("Nova Assistència");
            int absents = model.getNumEstudiantsAssignatura();
            tv_info_assistencia.setText(getString(R.string.tv_info_assistents, 0, absents));
        }
        initRecycleView();
    }

    private void initRecycleView() {
        RecyclerView RVestudiantsCb = findViewById(R.id.rv_llistaEstudiantsAssistencies);
        RVestudiantsCb.setHasFixedSize(true);
        RVestudiantsCb.setLayoutManager(new LinearLayoutManager(this));

        AdapterEstudiantsAssistencia mAdapter = new AdapterEstudiantsAssistencia(model);
        RVestudiantsCb.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new AdapterEstudiantsAssistencia.OnItemClickListener() {

            @Override
            public void onCheckBox(int position, boolean checked) {
                if(checked) model.addAssistent(position);
                else model.removeAssistent(position);
                updateInfoAssistencia();
            }
        });
    }

    private void updateInfoAssistencia(){
        int presents = model.getNumPresents();
        int absents = model.getNumAbsents();
        tv_info_assistencia.setText(getString(R.string.tv_info_assistents, presents, absents));
    }
}
