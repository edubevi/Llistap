package edu.upc.damo.llistapp.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import edu.upc.damo.llistapp.Entitats.Estudiant;
import edu.upc.damo.llistapp.R;

public class AdapterEstudiants extends RecyclerView.Adapter<AdapterEstudiants.ViewHolderEstudiants> implements Filterable {

    private static final String TAG = "AdapterEstudiants";
    private List<Estudiant> mListEstudiants;
    private List<Estudiant> mListEstudiantsFull;
    private OnItemClickListener mListener;
    private Context mContext;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
        void onEditClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){ this.mListener = listener; }

    public static class ViewHolderEstudiants extends RecyclerView.ViewHolder {

        public CircleImageView mCVavatar;
        public ImageButton mIBdelete;
        public ImageButton mIBedit;
        public TextView mTVnom, mTVdni;
        public CardView mCardLayout;

        public ViewHolderEstudiants(View itemView, final OnItemClickListener listener){
            super(itemView);
            mCVavatar = (CircleImageView) itemView.findViewById(R.id.imatge_estudiant);
            mTVnom = (TextView) itemView.findViewById(R.id.tv_nomEstudiant);
            mTVdni = (TextView) itemView.findViewById(R.id.tv_dniEstudiant);
            mCardLayout = (CardView) itemView.findViewById(R.id.cardLayout);
            mIBdelete = (ImageButton) itemView.findViewById(R.id.ib_delete);
            mIBedit = (ImageButton) itemView.findViewById(R.id.ib_edit);

/*            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });*/

            mIBdelete.setOnClickListener(new View.OnClickListener() {
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

            mIBedit.setOnClickListener(new View.OnClickListener() {
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

    public AdapterEstudiants(List<Estudiant> llista, Context context) {
        this.mListEstudiants = llista; //Referencia a la llista de GestioEstudiants
        this.mListEstudiantsFull = new ArrayList<>(mListEstudiants);
        this.mContext = context;
    }

    @Override
    public ViewHolderEstudiants onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.layuout_list_estudiants, parent,false);
        return new ViewHolderEstudiants(view, mListener);
    }

    @Override
    public void onBindViewHolder(ViewHolderEstudiants holder, int position) {
        Log.d(TAG,"onBindViewHolder: called");
        Estudiant est = mListEstudiants.get(position);
        String nomComplet = est.getNom() + " " + est.getCognoms();
        holder.mTVnom.setText(nomComplet);
        holder.mTVdni.setText(est.getDni());
        holder.mCVavatar.setImageBitmap(byteArraytoBitmap(est.getFoto()));
    }

    public void updateLlistaPlena() {
        this.mListEstudiantsFull = new ArrayList<>(mListEstudiants);
    }

    @Override
    public int getItemCount() { return mListEstudiants.size();}

    @Override
    public Filter getFilter() { return llistaFilter; }

    private Filter llistaFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Estudiant> filteredList = new ArrayList<Estudiant>();
            if(constraint == null || constraint.length() == 0) {
                //Retornem la llista sencera. No s'ha escrit res en el editText de cerca.
                filteredList.addAll(mListEstudiantsFull);
            }
            else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(Estudiant item : mListEstudiantsFull){
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
            mListEstudiants.clear();
            mListEstudiants.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };

    //Funcions Auxiliars
    public Bitmap byteArraytoBitmap(byte[] byteArray){
        Bitmap toBitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
        return toBitmap;
    }
}
