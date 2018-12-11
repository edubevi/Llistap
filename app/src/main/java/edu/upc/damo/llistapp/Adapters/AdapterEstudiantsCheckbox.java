package edu.upc.damo.llistapp.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.upc.damo.llistapp.DB.DBManager;
import edu.upc.damo.llistapp.Entitats.Estudiant;
import edu.upc.damo.llistapp.R;

public class AdapterEstudiantsCheckbox extends RecyclerView.Adapter<AdapterEstudiantsCheckbox.ViewHolderEstudiantsCheckbox> {

    private List<Estudiant> mLlistaEstudiants;
    private OnItemClickListener mListener;
    private String mNomAssignatura;
    private Context mContext;

    public interface OnItemClickListener {
        //void onItemClick(int position);
        void onCheckBox(int position, boolean checked);
    }

    public void setOnItemClickListener(OnItemClickListener listener) { this.mListener = listener; }

    public static class ViewHolderEstudiantsCheckbox extends RecyclerView.ViewHolder{

        private CircleImageView mCVavatar;
        private CheckBox mCheck;
        private TextView mTVnom, mTVdni;

        public ViewHolderEstudiantsCheckbox(View itemView, final OnItemClickListener listener) {
            super(itemView);
            mCVavatar = itemView.findViewById(R.id.ci_listEstCheck);
            mCheck = itemView.findViewById(R.id.cb_listEstCheck);
            mTVnom = itemView.findViewById(R.id.tv_nomEstCheck);
            mTVdni = itemView.findViewById(R.id.tv_dniEstCheck);

            mCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            if(mCheck.isChecked()) mCheck.setChecked(true);
                            else mCheck.setChecked(false);
                            listener.onCheckBox(position, mCheck.isChecked());
                        }
                    }
                }
            });
        }
    }

    public AdapterEstudiantsCheckbox(List<Estudiant> list, Context context, String nomAssignatura) {
        this.mLlistaEstudiants = list;
        this.mContext = context;
        this.mNomAssignatura = nomAssignatura;
    }

    @Override
    public ViewHolderEstudiantsCheckbox onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.layout_list_estudiants_checkbox, parent, false);
        return new ViewHolderEstudiantsCheckbox(view, mListener);
    }

    @Override
    public void onBindViewHolder(ViewHolderEstudiantsCheckbox holder, int position) {

        Estudiant est = mLlistaEstudiants.get(position);
        String nomComplet = est.getNom() + " " + est.getCognoms();
        holder.mTVnom.setText(nomComplet);
        holder.mTVdni.setText(est.getDni());
        holder.mCVavatar.setImageBitmap(byteArraytoBitmap(est.getFoto()));
        //Si tenim context vol dir que cridarem al adaptador en una activity en el que editarem
        if(mContext != null && mNomAssignatura.length() > 0){
            DBManager conn = new DBManager(mContext);
            /* Comprovem si el item del recyclerview correspon a un estudiant matriculat a
            l'assignatura. En cas afirmatiu, marquem el seu checkbox */
            if(conn.checkEstudiantInAssignatura(mNomAssignatura,est.getDni())){
                holder.mCheck.setChecked(true);
            }
        }
    }

    @Override
    public int getItemCount() { return mLlistaEstudiants.size(); }

    //Funcions Auxiliars
    private Bitmap byteArraytoBitmap(byte[] byteArray){
        return BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
    }

}
