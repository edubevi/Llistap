package edu.upc.damo.llistapp.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.upc.damo.llistapp.Models.ModelEstudiantAssistencia;
import edu.upc.damo.llistapp.Objectes.Estudiant;
import edu.upc.damo.llistapp.R;
import edu.upc.damo.llistapp.Utils.Utils;

public class AdapterEstudiantsAssistencia extends RecyclerView.Adapter<
        AdapterEstudiantsAssistencia.ViewHolderEstudiantsAssistencia> {

    private List<Estudiant> estudiants;
    private ArrayList<String> estudiants_presents;
    private int id_assistencia;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onCheckBox(int position, boolean checked);
    }

    public void setOnItemClickListener(OnItemClickListener listener) { this.listener = listener; }

    static class ViewHolderEstudiantsAssistencia extends RecyclerView.ViewHolder {

        private CircleImageView cv_avatar;
        private CheckBox check;
        private TextView tv_nom, tv_dni;

        ViewHolderEstudiantsAssistencia(View itemView, final OnItemClickListener listener) {
            super(itemView);
            cv_avatar = itemView.findViewById(R.id.ci_listEstCheck);
            check = itemView.findViewById(R.id.cb_listEstCheck);
            tv_nom = itemView.findViewById(R.id.tv_nomEstCheck);
            tv_dni = itemView.findViewById(R.id.tv_dniEstCheck);

            check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            if(check.isChecked()) check.setChecked(true);
                            else check.setChecked(false);
                            listener.onCheckBox(position, check.isChecked());
                        }
                    }
                }
            });
        }
    }

    public AdapterEstudiantsAssistencia(ModelEstudiantAssistencia model) {
        this.estudiants = model.getEstudiants_assignatura();
        this.id_assistencia = model.getId_assistencia();
        this.estudiants_presents = model.getDniPresents();
    }

    @Override
    public ViewHolderEstudiantsAssistencia onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.layout_list_estudiants_checkbox, parent, false);
        return new ViewHolderEstudiantsAssistencia(view, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolderEstudiantsAssistencia holder, int position) {
        Estudiant est = estudiants.get(position);
        String nomComplet = est.getNom() + " " + est.getCognoms();
        holder.tv_nom.setText(nomComplet);
        holder.tv_dni.setText(est.getDni());
        holder.cv_avatar.setImageBitmap(Utils.byteArraytoBitmap(est.getFoto()));
        /* Si tenim context vol dir que cridarem al adaptador en una activity en el que editarem.
        Per tant, necessitem recuperar la informació prèvia. */
        if(id_assistencia > 0){
            /* Comprovem si el item del recyclerview correspon a un estudiant marcat com a present
            en l'assistència en qüestió. En cas afirmatiu, marquem el seu checkbox. */
            if(estudiants_presents.contains(est.getDni())) holder.check.setChecked(true);
        }
    }

    @Override
    public int getItemCount() { return estudiants.size(); }
}
