package edu.upc.damo.llistapp.Activities;

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
import android.widget.TextView;


import edu.upc.damo.llistapp.Adapters.AdapterAssignatures;
import edu.upc.damo.llistapp.Models.ModelAssignatura;
import edu.upc.damo.llistapp.Objectes.Assignatura;
import edu.upc.damo.llistapp.R;
import edu.upc.damo.llistapp.Utils.Utils;

public class GestioAssignatures extends AppCompatActivity {

    private final int ADD_ASSIGNATURA_REQUEST = 252;
    private final int EDIT_ASSIGNATURA_REQUEST = 253;

    private ModelAssignatura model;
    private AdapterAssignatures adapter;
    private RecyclerView rv_assignatures;
    private TextView empty_view;

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK ){
            Assignatura newAssignatura = new Assignatura(data.getStringExtra(Utils.EXTRA_NOM_ASSIG),
                    data.getStringExtra(Utils.EXTRA_ALIAS_ASSIG),
                    data.getStringArrayListExtra(Utils.EXTRA_MATRICULATS));

            switch (requestCode){
                case ADD_ASSIGNATURA_REQUEST:
                    if(model.addAssignatura(newAssignatura)) {
                        /* Afegim la nova assignatura a la llista còpia del adapter per tal que al
                        sortir del searchview, el adapter carregui els items actualitzats. */
                        adapter.addItemOnCopyList(newAssignatura);
                        adapter.notifyDataSetChanged();
                    }
                    break;

                case EDIT_ASSIGNATURA_REQUEST:
                    int index = data.getIntExtra(Utils.EXTRA_POS, 0);
                    /* Recuperem el index original del item abans de filtrar amb el searchview */
                    int indexCpy = adapter.getCopyListItemIndex(adapter.getItem(index));
                    if(model.editAssignatura(index, newAssignatura)) {
                        /* Editem l'assignatura de la llista còpia del adapter per tal que al
                        sortir del searchview, el adapter carregui els items actualitzats. */
                        adapter.editCopyListItem(indexCpy, newAssignatura);
                        adapter.notifyItemChanged(index);
                    }
                    break;
            }
        }
    }

    //Mètodes init

    private void init() {
        model = new ModelAssignatura(this);
        FloatingActionButton fab_add_ssignautra = findViewById(R.id.fab_addAssignatura);
        fab_add_ssignautra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addAssignautra = new Intent(GestioAssignatures.this, AddEditAssignatura.class);
                startActivityForResult(addAssignautra, ADD_ASSIGNATURA_REQUEST);
            }
        });
        initRecycleView();
    }

    private void initRecycleView() {
        rv_assignatures = findViewById(R.id.rv_llistaAssignatures);
        empty_view = findViewById(R.id.empty_view_assig);
        rv_assignatures.setHasFixedSize(true);
        rv_assignatures.setLayoutManager(new LinearLayoutManager(this));

        adapter = new AdapterAssignatures(model);
        rv_assignatures.setAdapter(adapter);

        /* Cridem a una funció que amaga la vista corresponent al RecyclerView en el cas que la llista
        dels items que l'han de poblar estigui buida i visualitza un TextView informant d'aquesta situació.
        En el cas contrari, fa el procés invers. */
        Utils.setViewsToShow(model.getListAssignatures().size(), rv_assignatures, empty_view);

        /* Amaga o visualitza la empty_view en funció de si afegim o eliminem items de la RecyclerView. */
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                Utils.setViewsToShow(model.getListAssignatures().size(), rv_assignatures, empty_view);
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                Utils.setViewsToShow(model.getListAssignatures().size(), rv_assignatures, empty_view);
            }
        });

        adapter.setOnItemClickListener(new AdapterAssignatures.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent editAssignatura = new Intent(GestioAssignatures.this, AddEditAssignatura.class);
                Assignatura a = model.getAssignatura(position);
                //Enviem parametres
                editAssignatura.putExtra(Utils.EXTRA_NOM_ASSIG, a.getNom());
                editAssignatura.putExtra(Utils.EXTRA_ALIAS_ASSIG, a.getAlias());
                editAssignatura.putExtra(Utils.EXTRA_POS, position);
                editAssignatura.putStringArrayListExtra(Utils.EXTRA_MATRICULATS, a.getDni_matriculats());
                startActivityForResult(editAssignatura, EDIT_ASSIGNATURA_REQUEST);
            }

            @Override
            public void onDeleteClick(final int position) {
                final AlertDialog.Builder deleteDialog = new AlertDialog.Builder(GestioAssignatures.this);
                deleteDialog.setTitle("Listapp");
                deleteDialog.setMessage("Vols eleiminar l'assignatura? ");
                deleteDialog.setPositiveButton("ELIMINA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /* Recuperem el index original del item abans de filtrar amb el searchview */
                        int indexCpy = adapter.getCopyListItemIndex(adapter.getItem(position));
                        if(model.deleteAssignatura(position)) {
                            adapter.deleteCopyListItem(indexCpy);
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

    private void initMenu(Menu menu) {
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
}
