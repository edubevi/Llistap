package edu.upc.damo.llistapp.Adapters;

import android.support.v7.widget.CardView;
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

import edu.upc.damo.llistapp.Entitats.Assignatura;
import edu.upc.damo.llistapp.R;

public class AdapterAssignatures extends RecyclerView.Adapter<AdapterAssignatures.ViewHolderAssignatures> implements Filterable {

    private List<Assignatura> mListAssignatures;
    private List<Assignatura> mListAssignaturesFull;
    private OnItemClickListener mListener;
    private Filter mFilterList = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Assignatura> filteredList = new ArrayList<Assignatura>();
            if(constraint == null || constraint.length() == 0) {
                //Si no s'escriu cap patro de filtre en el quadre de cerca, retornem totes les assignatures
                filteredList.addAll(mListAssignaturesFull);
            }
            else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(Assignatura item: mListAssignaturesFull){
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
            mListAssignatures.clear();
            mListAssignatures.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) { this.mListener = listener; }

    public static class ViewHolderAssignatures extends RecyclerView.ViewHolder {

        public TextView mTValias, mTVnom;
        public ImageButton mIBdelete;
        public CardView mCardLayout;

        public ViewHolderAssignatures(View itemView, final OnItemClickListener listener) {
            super(itemView);
            mTValias = (TextView) itemView.findViewById(R.id.tv_alias);
            mTVnom = (TextView) itemView.findViewById(R.id.tv_nomAssignatura);
            mCardLayout = (CardView) itemView.findViewById(R.id.cardLayoutAssig);
            mIBdelete = (ImageButton) itemView.findViewById(R.id.ib_deleteAssignatura);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) listener.onItemClick(position);
                    }
                }
            });

            mIBdelete.setOnClickListener(new View.OnClickListener() {
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

    public AdapterAssignatures(List<Assignatura> list) {
        this.mListAssignatures = list; //Referencia a la llista de GestioAssignatures
        this.mListAssignaturesFull = new ArrayList<Assignatura>(mListAssignatures);
    }

    @Override
    public ViewHolderAssignatures onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.layout_list_assignatures, parent,false);
        return new ViewHolderAssignatures(view, mListener);
    }

    @Override
    public void onBindViewHolder(ViewHolderAssignatures holder, int position) {
        Assignatura assig = mListAssignatures.get(position);
        holder.mTValias.setText(assig.getAlias());
        holder.mTVnom.setText(assig.getNom());
    }

    @Override
    public int getItemCount() { return mListAssignatures.size(); }

    @Override
    public Filter getFilter() { return mFilterList; }

}
