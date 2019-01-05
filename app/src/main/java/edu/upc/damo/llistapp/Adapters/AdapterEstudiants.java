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
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.upc.damo.llistapp.Models.ModelEstudiant;
import edu.upc.damo.llistapp.Objectes.Estudiant;
import edu.upc.damo.llistapp.R;
import edu.upc.damo.llistapp.Utils.Utils;

public class AdapterEstudiants extends RecyclerView.Adapter<AdapterEstudiants.ViewHolderEstudiants>
        implements Filterable {

    private List<Estudiant> estudiants;
    private List<Estudiant> estudiantsCopy;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onDeleteClick(int position);
        void onEditClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){ this.listener = listener; }

    static class ViewHolderEstudiants extends RecyclerView.ViewHolder {

        private CircleImageView cv_avatar;
        private ImageButton ib_delete;
        private ImageButton ib_edit;
        private TextView tv_nom, tv_dni;

        ViewHolderEstudiants(View itemView, final OnItemClickListener listener){
            super(itemView);
            cv_avatar = itemView.findViewById(R.id.imatge_estudiant);
            tv_nom = itemView.findViewById(R.id.tv_nomEstudiant);
            tv_dni = itemView.findViewById(R.id.tv_dniEstudiant);
            ib_delete = itemView.findViewById(R.id.ib_delete);
            ib_edit = itemView.findViewById(R.id.ib_edit);

            ib_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });

            ib_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onEditClick(position);
                        }
                    }
                }
            });
        }
    }

    public AdapterEstudiants(ModelEstudiant model) {
        estudiants = model.getListEstudiants();
        estudiantsCopy = new ArrayList<>(estudiants);
    }

    public void updateFullList() { estudiantsCopy = new ArrayList<>(estudiants);}

    @Override
    public ViewHolderEstudiants onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.layuout_list_estudiants, parent,false);
        return new ViewHolderEstudiants(view, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolderEstudiants holder, int position) {
        Estudiant est = estudiants.get(position);
        String nomComplet = est.getNom() + " " + est.getCognoms();
        holder.tv_nom.setText(nomComplet);
        holder.tv_dni.setText(est.getDni());
        holder.cv_avatar.setImageBitmap(Utils.byteArraytoBitmap(est.getFoto()));
    }

    @Override
    public int getItemCount() { return estudiants.size();}

    @Override
    public Filter getFilter() { return llistaFilter; }

    private Filter llistaFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Estudiant> filteredList = new ArrayList<>();
            if(constraint == null || constraint.length() == 0) {
                //Retornem la llista sencera. No s'ha escrit res en el editText de cerca.
                filteredList.addAll(estudiantsCopy);
            }
            else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(Estudiant item : estudiantsCopy){
                    if(item.getNom().toLowerCase().contains(filterPattern) ||
                            item.getCognoms().toLowerCase().contains(filterPattern) ||
                            item.getDni().toLowerCase().contains(filterPattern)) {
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
            estudiants.clear();
            estudiants.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
