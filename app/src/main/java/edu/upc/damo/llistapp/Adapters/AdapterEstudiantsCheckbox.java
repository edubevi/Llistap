package edu.upc.damo.llistapp.Adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.upc.damo.llistapp.Entitats.Estudiant;
import edu.upc.damo.llistapp.R;

public class AdapterEstudiantsCheckbox extends RecyclerView.Adapter<AdapterEstudiantsCheckbox.ViewHolderEstudiantsCheckbox> {

    private List<Estudiant> mLlistaEstudiants;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onCheckBox(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) { this.mListener = listener; }


    public static class ViewHolderEstudiantsCheckbox extends RecyclerView.ViewHolder{

        public CircleImageView mCVavatar;
        public CheckBox mCheck;
        public TextView mTVnom, mTVdni;
        public CardView mCardViewLayout;


        public ViewHolderEstudiantsCheckbox(View itemView, final OnItemClickListener listener) {
            super(itemView);
            mCVavatar = (CircleImageView) itemView.findViewById(R.id.ci_listEstCheck);
            mCheck = (CheckBox) itemView.findViewById(R.id.cb_listEstCheck);
            mTVnom = (TextView) itemView.findViewById(R.id.tv_nomEstCheck);
            mTVdni = (TextView) itemView.findViewById(R.id.tv_dniEstCheck);
            mCardViewLayout = (CardView) itemView.findViewById(R.id.cv_listEstCheck);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) listener.onItemClick(position);
                    }
                }
            });

            mCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) listener.onCheckBox(position);
                    }
                }
            });
        }
    }

    public AdapterEstudiantsCheckbox(List<Estudiant> list) { this.mLlistaEstudiants = list; }

    @Override
    public ViewHolderEstudiantsCheckbox onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.layout_list_estudiants_checkbox, parent, false);
        return new ViewHolderEstudiantsCheckbox(view,mListener);
    }

    @Override
    public void onBindViewHolder(ViewHolderEstudiantsCheckbox holder, int position) {
        Estudiant est = mLlistaEstudiants.get(position);
        String nomComplet = est.getNom() + " " + est.getCognoms();
        holder.mTVnom.setText(nomComplet);
        holder.mTVdni.setText(est.getDni());
        holder.mCVavatar.setImageBitmap(byteArraytoBitmap(est.getFoto()));
    }

    @Override
    public int getItemCount() { return mLlistaEstudiants.size(); }

    //Funcions Auxiliars
    public Bitmap byteArraytoBitmap(byte[] byteArray){
        Bitmap toBitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
        return toBitmap;
    }
}
