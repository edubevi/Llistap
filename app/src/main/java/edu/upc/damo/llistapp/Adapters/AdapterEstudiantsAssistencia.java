package edu.upc.damo.llistapp.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.upc.damo.llistapp.DB.DBManager;
import edu.upc.damo.llistapp.Objectes.Estudiant;
import edu.upc.damo.llistapp.R;

public class AdapterEstudiantsAssistencia extends RecyclerView.Adapter<
        AdapterEstudiantsAssistencia.ViewHolderEstudiantsAssistencia> {

    private List<Estudiant> mListEstudiants;
    private OnItemClickListener mListener;
    private int mIdAssistencia;
    private Context mContext;

    public interface OnItemClickListener {
        void onCheckBox(int position, boolean checked);
    }

    public void setOnItemClickListener(OnItemClickListener listener) { this.mListener = listener; }

    public static class ViewHolderEstudiantsAssistencia extends RecyclerView.ViewHolder {

        private CircleImageView mCVavatar;
        private CheckBox mCheck;
        private TextView mTVnom, mTVdni;

        public ViewHolderEstudiantsAssistencia(View itemView, final OnItemClickListener listener) {
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

    public AdapterEstudiantsAssistencia(List<Estudiant> list, Context context, int idAssistencia) {
        this.mListEstudiants = list;
        this.mContext = context;
        this.mIdAssistencia = idAssistencia;
    }

    @Override
    public ViewHolderEstudiantsAssistencia onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.layout_list_estudiants_checkbox, parent, false);
        return new ViewHolderEstudiantsAssistencia(view, mListener);
    }

    @Override
    public void onBindViewHolder(ViewHolderEstudiantsAssistencia holder, int position) {
        Estudiant est = mListEstudiants.get(position);
        String nomComplet = est.getNom() + " " + est.getCognoms();
        holder.mTVnom.setText(nomComplet);
        holder.mTVdni.setText(est.getDni());
        holder.mCVavatar.setImageBitmap(byteArraytoBitmap(est.getFoto()));
        /* Si tenim context vol dir que cridarem al adaptador en una activity en el que editarem.
        Per tant, necessitem recuperar la informació prèvia. */
        if(mContext != null && mIdAssistencia > 0){
            DBManager conn = new DBManager(mContext);
            /* Comprovem si el item del recyclerview correspon a un estudiant marcat com a present
            en l'assistència en qüestió. En cas afirmatiu, marquem el seu checkbox. */
            if(conn.checkPresenciaEstudiant(mIdAssistencia, est.getDni())){
                holder.mCheck.setChecked(true);
            }
        }
    }

    @Override
    public int getItemCount() { return mListEstudiants.size(); }

    //Funcions Auxiliars
    private Bitmap byteArraytoBitmap(byte[] byteArray){
        return BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
    }
}
