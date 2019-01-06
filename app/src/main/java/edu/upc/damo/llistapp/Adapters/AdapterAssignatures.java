package edu.upc.damo.llistapp.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.upc.damo.llistapp.Models.ModelAssignatura;
import edu.upc.damo.llistapp.Objectes.Assignatura;
import edu.upc.damo.llistapp.R;

public class AdapterAssignatures extends RecyclerView.Adapter<
        AdapterAssignatures.ViewHolderAssignatures> implements Filterable {

    private List<Assignatura> assignaturas;
    private List<Assignatura> assignaturesCopy;
    private OnItemClickListener listener;
    private Filter mFilterList = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Assignatura> filteredList = new ArrayList<>();
            if(constraint == null || constraint.length() == 0) {
                //Si no s'escriu cap patro de filtre en el quadre de cerca, retornem totes les assignatures
                filteredList.addAll(assignaturesCopy);
            }
            else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(Assignatura item: assignaturesCopy){
                    if(item.getAlias().toLowerCase().contains(filterPattern) ||
                            item.getNom().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            assignaturas.clear();
            assignaturas.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) { this.listener = listener; }

    static class ViewHolderAssignatures extends RecyclerView.ViewHolder {

        private TextView tv_alias, tv_nom;
        private ImageButton ib_delete;

        ViewHolderAssignatures(View itemView, final OnItemClickListener listener) {
            super(itemView);
            tv_alias = itemView.findViewById(R.id.tv_alias);
            tv_nom = itemView.findViewById(R.id.tv_nomAssignatura);
            ib_delete = itemView.findViewById(R.id.ib_deleteAssignatura);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) listener.onItemClick(position);
                    }
                }
            });

            ib_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) listener.onDeleteClick(position);
                    }
                }
            });
        }
    }

    public AdapterAssignatures(ModelAssignatura model) {
        assignaturas = model.getListAssignatures();
        assignaturesCopy = new ArrayList<>(assignaturas);
    }

    @Override
    public ViewHolderAssignatures onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.layout_list_assignatures, parent,false);
        return new ViewHolderAssignatures(view, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolderAssignatures holder, int position) {
        Assignatura assig = assignaturas.get(position);
        holder.tv_alias.setText(assig.getAlias());
        holder.tv_nom.setText(assig.getNom());
    }

    @Override
    public int getItemCount() { return assignaturas.size(); }

    @Override
    public Filter getFilter() { return mFilterList; }

    /* Mètodes per gestionar la coherència de la llista d'estudiants del RecyclerView al modificar
    items amb el searchview. */

    public void deleteCopyListItem(int index) { assignaturesCopy.remove(index); }

    public void editCopyListItem(int index, Assignatura newAssignatura){
        Assignatura a = assignaturesCopy.get(index);
        a.setNom(newAssignatura.getNom());
        a.setAlias(newAssignatura.getAlias());
        a.setDni_matriculats(newAssignatura.getDni_matriculats());
    }

    public void addItemOnCopyList(Assignatura novaAssignatura) {
        assignaturesCopy.add(novaAssignatura);
        Collections.sort(assignaturesCopy);
    }

    public int getCopyListItemIndex(Assignatura a) { return assignaturesCopy.indexOf(a); }

    public Assignatura getItem(int index) { return assignaturas.get(index); }

}
