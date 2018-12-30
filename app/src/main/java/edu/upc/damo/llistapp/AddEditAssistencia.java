package edu.upc.damo.llistapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import edu.upc.damo.llistapp.Adapters.AdapterEstudiantsAssistencia;
import edu.upc.damo.llistapp.DB.DBManager;
import edu.upc.damo.llistapp.Entitats.Estudiant;

public class AddEditAssistencia extends AppCompatActivity {

    private TextView mInfoAssistencia;
    private List<Estudiant> mListEstudiants;
    private Set<String> mAssistents = new HashSet<>();
    private boolean editActivity = false;
    private int mIdAssistencia;
    private Long mDate;

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
                ArrayList<String> dniPresents = new ArrayList<>(mAssistents);
                resultat.putStringArrayListExtra(GestioAssistencies.EXTRA_PRESENTS, dniPresents);
                resultat.putExtra(GestioAssistencies.EXTRA_DATE_ASSISTENCIA, mDate);
                ArrayList<String> dniAbsents = new ArrayList<>();
                for (Estudiant e: mListEstudiants){
                    String dni = e.getDni();
                    if (!dniPresents.contains(dni)) dniAbsents.add(dni);
                }
                resultat.putStringArrayListExtra(GestioAssistencies.EXTRA_ABSENTS, dniAbsents);
                if (editActivity) {
                    Intent i = getIntent();
                    resultat.putExtra(GestioAssistencies.EXTRA_POS,
                            i.getIntExtra(GestioAssistencies.EXTRA_POS, 0));
                }
                setResult(RESULT_OK, resultat);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Init Methods
    private void inicialitza() {
        DBManager mCon = new DBManager(getApplicationContext());
        Intent intent = getIntent();
        TextView mAliasAssig = findViewById(R.id.tv_assigAssist);
        mAliasAssig.setText(intent.getStringExtra(GestioAssignatures.EXTRA_ALIAS_ASSIG));
        TextView tv_date = findViewById(R.id.tv_dataAssist);
        mDate = intent.getLongExtra(GestioAssistencies.EXTRA_DATE_ASSISTENCIA,0);
        tv_date.setText(getFormatDate(mDate));
        String mAssignatura = intent.getStringExtra(GestioAssignatures.EXTRA_NOM_ASSIG);
        mIdAssistencia = intent.getIntExtra(GestioAssistencies.EXTRA_ID_ASSISTENCIA,0);
        mListEstudiants = new ArrayList<>(mCon.getEstudiantsAssignaturaList(mAssignatura));
        mInfoAssistencia = findViewById(R.id.tv_infoAssist);
        //Comprovem si l'intent te assistents, en cas afirmatiu estem editant una assistencia.
        if(intent.hasExtra(GestioAssistencies.EXTRA_PRESENTS)){
            setTitle("Edit Assistència");
            editActivity = true;
            mAssistents.addAll(intent.getStringArrayListExtra(GestioAssistencies.EXTRA_PRESENTS));
            mInfoAssistencia.setText(getString(R.string.tv_info_assistents, mAssistents.size(),
                    (mListEstudiants.size()- mAssistents.size())));
        }
        else {
            setTitle("Nova Assistència");
            mInfoAssistencia.setText(getString(R.string.tv_info_assistents, 0,mListEstudiants.size()));
        }
        initRecycleView();
    }

    private void initRecycleView() {
        RecyclerView RVestudiantsCb = findViewById(R.id.rv_llistaEstudiantsAssistencies);
        RVestudiantsCb.setHasFixedSize(true);
        RVestudiantsCb.setLayoutManager(new LinearLayoutManager(this));

        AdapterEstudiantsAssistencia mAdapter = new AdapterEstudiantsAssistencia(mListEstudiants, getApplicationContext(), mIdAssistencia);
        RVestudiantsCb.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new AdapterEstudiantsAssistencia.OnItemClickListener() {

            @Override
            public void onCheckBox(int position, boolean checked) {
                if(checked) mAssistents.add(mListEstudiants.get(position).getDni());
                else mAssistents.remove(mListEstudiants.get(position).getDni());
                updateInfoAssistencia();
            }
        });
    }

    //Funcions Auxiliars
    private String getFormatDate(long date){
        SimpleDateFormat sdf = new SimpleDateFormat("EE d-MM-y H:m:s", new Locale("ca","ES"));
        Date formattedDate = new java.util.Date(date);
        return sdf.format(formattedDate);
    }

    private void updateInfoAssistencia(){
        mInfoAssistencia.setText(getString(R.string.tv_info_assistents, mAssistents.size(),
                (mListEstudiants.size()- mAssistents.size())));
    }
}
