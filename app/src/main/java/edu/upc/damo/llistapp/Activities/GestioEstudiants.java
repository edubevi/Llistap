package edu.upc.damo.llistapp.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;


import edu.upc.damo.llistapp.Adapters.AdapterEstudiants;
import edu.upc.damo.llistapp.Models.ModelEstudiant;
import edu.upc.damo.llistapp.Objectes.Estudiant;
import edu.upc.damo.llistapp.R;
import edu.upc.damo.llistapp.Utils.Utils;

public class GestioEstudiants extends AppCompatActivity {

    private final int ADD_ESTUDIANT_REQUEST = 250;
    private final int EDIT_ESTUDIANT_REQUEST = 251;

    private AdapterEstudiants adapter;
    private ModelEstudiant model;
    private RecyclerView rv_estudiants;
    private TextView empty_view;

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
        model = new ModelEstudiant(this);
        FloatingActionButton fab_nou_estudiant = findViewById(R.id.fab_addEstudiant);
        fab_nou_estudiant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nouEstudiant =  new Intent(GestioEstudiants.this, AddEditEstudiant.class);
                startActivityForResult(nouEstudiant, ADD_ESTUDIANT_REQUEST);
            }
        });
        inicialitzaRecycleView();
    }

    private void inicialitzaRecycleView(){
        rv_estudiants = findViewById(R.id.rv_llistaEstudiants);
        empty_view = findViewById(R.id.empty_view_est);
        rv_estudiants.setHasFixedSize(true);
        rv_estudiants.setLayoutManager(new LinearLayoutManager(this));

        adapter = new AdapterEstudiants(model);
        rv_estudiants.setAdapter(adapter);
        /* Cridem a una funció que amaga la vista corresponent al RecyclerView en el cas que la llista
        dels items que l'han de poblar estigui buida i visualitza un TextView informant d'aquesta situació.
        En el cas contrari, fa el procés invers. */
        Utils.setViewsToShow(model.getListEstudiants().size(), rv_estudiants, empty_view);

        /* Amaga o visualitza la empty_view en funció de si afegim o eliminem items de la RecyclerView. */
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                Utils.setViewsToShow(model.getListEstudiants().size(), rv_estudiants, empty_view);
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                Utils.setViewsToShow(model.getListEstudiants().size(), rv_estudiants, empty_view);
            }
        });

        adapter.setOnItemClickListener(new AdapterEstudiants.OnItemClickListener() {

            @Override
            public void onDeleteClick(final int position) {
                final AlertDialog.Builder deleteDialog = new AlertDialog.Builder(GestioEstudiants.this);
                deleteDialog.setTitle("Listapp");
                deleteDialog.setMessage("Vols eliminar aquest estudiant?");
                deleteDialog.setPositiveButton("ELIMINA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /* Recuperem el index original del item abans de filtrar amb el searchview */
                        int indexCpy = adapter.getCopyListItemIndex(adapter.getItem(position));
                        if(model.deleteEstudiant(position)) {
                            /* Eliminem l'estudiant de la llista còpia del adapter per tal que al
                            sortir del searchview, el adapter carregui els items actualitzats. */
                            adapter.deleteCopyListItem(indexCpy);
                            adapter.notifyItemRemoved(position);
                        }
                        dialog.dismiss();
                    }
                });
                deleteDialog.setNegativeButton("CANCEL·LA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { dialog.dismiss(); }
                });
                deleteDialog.show();
            }

            @Override
            public void onEditClick(int position) {
                Intent editEstudiant = new Intent(GestioEstudiants.this, AddEditEstudiant.class);
                Estudiant e = model.getEstudiant(position);
                //Parametres que s'enviaran.
                editEstudiant.putExtra(Utils.EXTRA_NOM, e.getNom());
                editEstudiant.putExtra(Utils.EXTRA_COGNOMS, e.getCognoms());
                editEstudiant.putExtra(Utils.EXTRA_DNI, e.getDni());
                editEstudiant.putExtra(Utils.EXTRA_MAIL, e.getMail());
                editEstudiant.putExtra(Utils.EXTRA_FOTO, e.getFoto());
                editEstudiant.putExtra(Utils.EXTRA_POS, position);
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
            public boolean onQueryTextSubmit(String query) { return false; }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
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
            Estudiant nouEstudiant = new Estudiant(data.getStringExtra(Utils.EXTRA_NOM),
                    data.getStringExtra(Utils.EXTRA_COGNOMS), data.getStringExtra(Utils.EXTRA_DNI),
                    data.getStringExtra(Utils.EXTRA_MAIL),data.getByteArrayExtra(Utils.EXTRA_FOTO));

            switch (requestCode) {
                case ADD_ESTUDIANT_REQUEST:
                    if(model.addEstudiant(nouEstudiant)) {
                        /* Afegim el nou estudiant a la llista còpia del adapter per tal que al
                        sortir del searchview, el adapter carregui els items actualitzats. */
                        adapter.addItemOnCopyList(nouEstudiant);
                        adapter.notifyDataSetChanged();
                    }
                    break;

                case EDIT_ESTUDIANT_REQUEST:
                    int index = data.getIntExtra(Utils.EXTRA_POS, 0);
                    /* Recuperem el index original del item abans de filtrar amb el searchview */
                    int indexCpy = adapter.getCopyListItemIndex(adapter.getItem(index));
                    if(model.editEstudiant(index, nouEstudiant)) {
                        /* Editem l'estudiant de la llista còpia del adapter per tal que al
                        sortir del searchview, el adapter carregui els items actualitzats. */
                        adapter.editCopyListItem(indexCpy, nouEstudiant);
                        adapter.notifyItemChanged(index);
                    }
                    break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){ return super.onOptionsItemSelected(item); }
}
