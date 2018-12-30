package edu.upc.damo.llistapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.upc.damo.llistapp.Adapters.AdapterEstudiantsAssignatura;
import edu.upc.damo.llistapp.DB.DBManager;
import edu.upc.damo.llistapp.Entitats.Estudiant;

public class AddEditAssignatura extends AppCompatActivity {

    private EditText mNomAssig, mAliasAssig;
    private DBManager mCon;
    private AdapterEstudiantsAssignatura mAdapter;
    private List<Estudiant> mListEstudiantCb;
    private Set<String> mMatriculats = new HashSet<>();
    private boolean editActivity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assignatura);
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
                if(emptyFields) toastMessage("Cal omplir tots el camps i seleccionar almenys un matriculat");
                else {
                    Intent resultat = new Intent();
                    resultat.putExtra(GestioAssignatures.EXTRA_NOM_ASSIG, mNomAssig.getText().toString());
                    resultat.putExtra(GestioAssignatures.EXTRA_ALIAS_ASSIG, mAliasAssig.getText().toString());
                    //Enviem el dni dels estudiants matricualts a l'assignatura amb una ArrayList
                    ArrayList<String> dniMatriculats = new ArrayList<>(mMatriculats);
                    resultat.putStringArrayListExtra(GestioAssignatures.EXTRA_MATRICULATS, dniMatriculats);
                    if(editActivity){
                        Intent i = getIntent();
                        resultat.putExtra(GestioAssignatures.EXTRA_POS,
                                i.getIntExtra(GestioAssignatures.EXTRA_POS,0));
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
        mNomAssig = findViewById(R.id.et_nomAssignatura);
        mAliasAssig = findViewById(R.id.et_aliasAssignatura);
        mCon = new DBManager(getApplicationContext());
        //Comprovem si l'intent te extras, en cas afirmatiu estem editant una assignatura
        Intent intent = getIntent();
        if(intent.hasExtra(GestioAssignatures.EXTRA_NOM_ASSIG)){
            setTitle("Edit Assignatura");
            editActivity = true;
            mNomAssig.setText(intent.getStringExtra(GestioAssignatures.EXTRA_NOM_ASSIG));
            mAliasAssig.setText(intent.getStringExtra(GestioAssignatures.EXTRA_ALIAS_ASSIG));
            mMatriculats.addAll(intent.getStringArrayListExtra(GestioAssignatures.EXTRA_MATRICULATS));
        }
        else setTitle("Nova Assignatura");
        initRecycleView();
    }

    private void initRecycleView() {
        RecyclerView RVestudiantsCb = findViewById(R.id.rv_llistaEstudiantsCb);
        RVestudiantsCb.setHasFixedSize(true);
        RVestudiantsCb.setLayoutManager(new LinearLayoutManager(this));

        mListEstudiantCb = new ArrayList<>();
        mListEstudiantCb = mCon.getEstudiantsList();
        mAdapter = new AdapterEstudiantsAssignatura(mListEstudiantCb, getApplicationContext(),
                    mNomAssig.getText().toString());
        RVestudiantsCb.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new AdapterEstudiantsAssignatura.OnItemClickListener() {

            @Override
            public void onCheckBox(int position, boolean checked) {
                if(checked) mMatriculats.add(mListEstudiantCb.get(position).getDni());
                else mMatriculats.remove(mListEstudiantCb.get(position).getDni());
            }
        });
    }

    //Metodes auxiliars
    private void toastMessage(String missatge){
        Toast.makeText(this,missatge,Toast.LENGTH_SHORT).show();
    }
    private boolean checkEmptyFields(){
        return mNomAssig.getText().length() == 0 || mAliasAssig.getText().length() == 0 ||
                mMatriculats.size() == 0;
    }
}
