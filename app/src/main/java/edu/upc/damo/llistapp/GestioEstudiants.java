package edu.upc.damo.llistapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.upc.damo.llistapp.Adapters.AdapterEstudiants;
import edu.upc.damo.llistapp.DB.DBManager;
import edu.upc.damo.llistapp.Objectes.Estudiant;
import edu.upc.damo.llistapp.Utils.Utils;

public class GestioEstudiants extends AppCompatActivity {

    private final int ADD_ESTUDIANT_REQUEST = 250;
    private final int EDIT_ESTUDIANT_REQUEST = 251;
    public static final String EXTRA_NOM = "edu.upc.damo.llistapp.EXTRA_NOM";
    public static final String EXTRA_COGNOMS = "edu.upc.damo.llistapp.EXTRA_COGNOMS";
    public static final String EXTRA_DNI = "edu.upc.damo.llistapp.EXTRA_DNI";
    public static final String EXTRA_MAIL = "edu.upc.damo.llistapp.EXTRA_MAIL";
    public static final String EXTRA_FOTO = "edu.upc.damo.llistapp.EXTRA_FOTO";
    public static final String EXTRA_POS = "edu.upc.damo.llistapp.EXTRA_POS";

    private static final String TAG = "GestioEstudiants";

    private List<Estudiant> mLlistaEstudiants;
    private AdapterEstudiants mAdapter;
    private DBManager mConn;

    //Metodes onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Estudiants");
        setContentView(R.layout.activity_gestio_estudiants);
        inicialitza();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_gestio, menu);
        inicialitzaMenu(menu);
        return true;
    }

    //Metodes init

    private void inicialitza(){
        mConn = new DBManager(getApplicationContext());
        FloatingActionButton mFABnouEstudiant = findViewById(R.id.fab_addEstudiant);
        mFABnouEstudiant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nouEstudiant =  new Intent(GestioEstudiants.this, AddEditEstudiant.class);
                startActivityForResult(nouEstudiant, ADD_ESTUDIANT_REQUEST);
            }
        });
        inicialitzaRecycleView();
    }

    private void inicialitzaRecycleView(){
        Log.d(TAG, "initRecyclerView: init");
        RecyclerView mRVestudiants = findViewById(R.id.rv_llistaEstudiants);
        mRVestudiants.setHasFixedSize(true);
        mRVestudiants.setLayoutManager(new LinearLayoutManager(this));

        mLlistaEstudiants = new ArrayList<>();
        mLlistaEstudiants = mConn.getEstudiantsList();

        mAdapter= new AdapterEstudiants(mLlistaEstudiants);
        mRVestudiants.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new AdapterEstudiants.OnItemClickListener() {

            @Override
            public void onDeleteClick(int position) {
                final AlertDialog.Builder deleteDialog = new AlertDialog.Builder(GestioEstudiants.this);
                final int pos = position;
                deleteDialog.setTitle("Listapp");
                deleteDialog.setMessage("Vols eliminar aquest estudiant?");
                deleteDialog.setPositiveButton("ELIMINA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mConn.deleteEstudiant(mLlistaEstudiants.get(pos).getDni());
                        mLlistaEstudiants.remove(pos);
                        mAdapter.notifyItemRemoved(pos);
                        mAdapter.updateFullList();
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

            @Override
            public void onEditClick(int position) {
                Intent editEstudiant = new Intent(GestioEstudiants.this, AddEditEstudiant.class);
                //Parametres que s'enviaran.
                editEstudiant.putExtra(EXTRA_NOM, mLlistaEstudiants.get(position).getNom());
                editEstudiant.putExtra(EXTRA_COGNOMS, mLlistaEstudiants.get(position).getCognoms());
                editEstudiant.putExtra(EXTRA_DNI, mLlistaEstudiants.get(position).getDni());
                editEstudiant.putExtra(EXTRA_MAIL,mLlistaEstudiants.get(position).getMail());
                editEstudiant.putExtra(EXTRA_FOTO, mLlistaEstudiants.get(position).getFoto());
                editEstudiant.putExtra(EXTRA_POS, position);
                startActivityForResult(editEstudiant, EDIT_ESTUDIANT_REQUEST);
            }
        });
    }

    private void inicialitzaMenu(Menu menu){
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    /* Capturem el resultat de l'activity AddEditEstudiant i actualitzem la llista d'estudiants i
     avisem al adapter dels canvis. */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            /* Recuperem els valors dels EditText que l'activity AddEditEstudiant ens han enviat com a resultat */
            Estudiant nouEstudiant = new Estudiant(data.getStringExtra(EXTRA_NOM),
                    data.getStringExtra(EXTRA_COGNOMS), data.getStringExtra(EXTRA_DNI),
                    data.getStringExtra(EXTRA_MAIL),data.getByteArrayExtra(EXTRA_FOTO));

            switch (requestCode) {
                case ADD_ESTUDIANT_REQUEST:
                    mLlistaEstudiants.add(nouEstudiant);
                    Collections.sort(mLlistaEstudiants);
                    mAdapter.notifyDataSetChanged();
                    mAdapter.updateFullList();
                    boolean inserData = mConn.insereixEstudiant(nouEstudiant);
                    if (!inserData) Utils.toastMessage(
                            "ERROR al afegir Estudiant a la Base de Dades", this);
                    else Utils.toastMessage("Estudiant afegit",this);
                    break;

                case EDIT_ESTUDIANT_REQUEST:
                    int index = data.getIntExtra(EXTRA_POS,0);
                    boolean upd = mConn.updateEstudiant(mLlistaEstudiants.get(index),nouEstudiant);
                    if(!upd) Utils.toastMessage(
                            "ERROR al actualitzar Estudiant a la Base de Dades", this);
                    else Utils.toastMessage("Estudiant modificat", this);
                    mLlistaEstudiants.get(index).setNom(nouEstudiant.getNom());
                    mLlistaEstudiants.get(index).setCognoms(nouEstudiant.getCognoms());
                    mLlistaEstudiants.get(index).setDni(nouEstudiant.getDni());
                    mLlistaEstudiants.get(index).setMail(nouEstudiant.getMail());
                    mLlistaEstudiants.get(index).setFoto(nouEstudiant.getFoto());
                    mAdapter.notifyItemChanged(index);
                    break;
            }
        }
        else Utils.toastMessage("Estudiant no guardat", this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
