package edu.upc.damo.llistapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import com.bumptech.glide.util.Util;

import java.util.ArrayList;
import java.util.List;

import edu.upc.damo.llistapp.Adapters.AdapterAssignatures;
import edu.upc.damo.llistapp.DB.DBManager;
import edu.upc.damo.llistapp.Objectes.Assignatura;
import edu.upc.damo.llistapp.Utils.Utils;

public class GestioAssignatures extends AppCompatActivity {

    private final int ADD_ASSIGNATURA_REQUEST = 252;
    private final int EDIT_ASSIGNATURA_REQUEST = 253;
    public static final String EXTRA_NOM_ASSIG = "edu.upc.damo.llistap.EXTRA_NOM_ASSIG";
    public static final String EXTRA_ALIAS_ASSIG = "edu.upc.damo.llistap.EXTRA_ALIAS_ASSIG";
    public static final String EXTRA_MATRICULATS = "upc.edu.damo.llistap.EXTRA_MATRICULATS";
    public static final String EXTRA_POS = "upc.edu.damo.llistap.EXTRA_POS";

    private List<Assignatura> mListAssignatures;
    private AdapterAssignatures mAdapter;
    private DBManager mConn;

    //Metodes onCreate

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Assignatures");
        setContentView(R.layout.activity_gestio_assignatures);
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_gestio, menu);
        initMenu(menu);
        return true;
    }


    // Altres metodes on..
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK ){
            ArrayList<String> dniMatriculats = data.getStringArrayListExtra(EXTRA_MATRICULATS);
            Assignatura newAssignatura = new Assignatura(data.getStringExtra(EXTRA_NOM_ASSIG),
                    data.getStringExtra(EXTRA_ALIAS_ASSIG), dniMatriculats);

            switch (requestCode){
                case ADD_ASSIGNATURA_REQUEST:
                    boolean inserData = mConn.insereixAssignatura(newAssignatura,dniMatriculats);
                    mListAssignatures.add(newAssignatura);
                    mAdapter.notifyDataSetChanged();
                    mAdapter.updateFullList();
                    if(!inserData) Utils.toastMessage(
                            "ERROR al afegir Assignatura a la base de dades",this);
                    else Utils.toastMessage("Assignatura afegida",this);
                    break;

                case EDIT_ASSIGNATURA_REQUEST:
                    int index = data.getIntExtra(EXTRA_POS,0);
                    boolean upd = mConn.updateAssignatura(mListAssignatures.get(index), newAssignatura);
                    if(!upd) Utils.toastMessage(
                            "ERROR al actualitzar Assignatura a la Base de Dades",this);
                    else Utils.toastMessage("Assignatura modificada",this);
                    mListAssignatures.get(index).setNom(newAssignatura.getNom());
                    mListAssignatures.get(index).setAlias(newAssignatura.getAlias());
                    mListAssignatures.get(index).setMatriculats(newAssignatura.getMatriculats());
                    mAdapter.notifyItemChanged(index);
                    break;
            }
        }
        else Utils.toastMessage("Assignatura no guardada",this);
    }

    //Init methods
    private void init() {
        mConn = new DBManager(getApplicationContext());
        FloatingActionButton mFABaddAssignautra = findViewById(R.id.fab_addAssignatura);
        mFABaddAssignautra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addAssignautra = new Intent(GestioAssignatures.this, AddEditAssignatura.class);
                startActivityForResult(addAssignautra, ADD_ASSIGNATURA_REQUEST);
            }
        });

        initRecycleView();
    }

    private void initRecycleView() {
        RecyclerView RVassignatures = findViewById(R.id.rv_llistaAssignatures);
        RVassignatures.setHasFixedSize(true);
        RVassignatures.setLayoutManager(new LinearLayoutManager(this));

        mListAssignatures = new ArrayList<>();
        mListAssignatures = mConn.getAssignaturesList();


        mAdapter = new AdapterAssignatures(mListAssignatures);
        RVassignatures.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new AdapterAssignatures.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent editAssignatura = new Intent(GestioAssignatures.this, AddEditAssignatura.class);
                //Enviem parametres
                editAssignatura.putExtra(EXTRA_NOM_ASSIG, mListAssignatures.get(position).getNom());
                editAssignatura.putExtra(EXTRA_ALIAS_ASSIG, mListAssignatures.get(position).getAlias());
                editAssignatura.putExtra(EXTRA_POS, position);
                editAssignatura.putStringArrayListExtra(EXTRA_MATRICULATS,
                        (ArrayList<String>) mConn.getDniEstudiantsAssignaturaList(mListAssignatures.get(position).getNom()));
                startActivityForResult(editAssignatura, EDIT_ASSIGNATURA_REQUEST);
            }

            @Override
            public void onDeleteClick(int position) {
                final AlertDialog.Builder deleteDialog = new AlertDialog.Builder(GestioAssignatures.this);
                final int pos = position;
                deleteDialog.setTitle("Listapp");
                deleteDialog.setMessage("Vols eleiminar l'assignatura? ");
                deleteDialog.setPositiveButton("ELIMINA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mConn.deleteAssignatura(mListAssignatures.get(pos).getNom());
                        mListAssignatures.remove(pos);
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

        });
    }

    private void initMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }
}
