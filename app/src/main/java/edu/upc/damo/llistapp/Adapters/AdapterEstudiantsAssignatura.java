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
import edu.upc.damo.llistapp.Models.ModelEstudiantAssignatura;
import edu.upc.damo.llistapp.Objectes.Estudiant;
import edu.upc.damo.llistapp.R;
import edu.upc.damo.llistapp.Utils.Utils;

public class AdapterEstudiantsAssignatura extends RecyclerView.Adapter<
        AdapterEstudiantsAssignatura.ViewHolderEstudiantsAssignatura> {

    private ArrayList<String> matriculats;
    private List<Estudiant> estudiants;
    private OnItemClickListener listener;


    public interface OnItemClickListener {
        void onCheckBox(int position, boolean checked);
    }

    public void setOnItemClickListener(OnItemClickListener listener) { this.listener = listener; }

    static class ViewHolderEstudiantsAssignatura extends RecyclerView.ViewHolder{

        private CircleImageView cv_avatar;
        private CheckBox check;
        private TextView tv_nom, tv_dni;

        ViewHolderEstudiantsAssignatura(View itemView, final OnItemClickListener listener) {
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

    public AdapterEstudiantsAssignatura(ModelEstudiantAssignatura model) {
        estudiants = model.getEstudiants();
        matriculats = model.getDniMatriculatsList();
    }

    @Override
    public ViewHolderEstudiantsAssignatura onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.layout_list_estudiants_checkbox, parent, false);
        return new ViewHolderEstudiantsAssignatura(view, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolderEstudiantsAssignatura holder, int position) {

        Estudiant estudiant = estudiants.get(position);
        String nomComplet = estudiant.getNom() + " " + estudiant.getCognoms();
        holder.tv_nom.setText(nomComplet);
        holder.tv_dni.setText(estudiant.getDni());
        holder.cv_avatar.setImageBitmap(Utils.byteArraytoBitmap(estudiant.getFoto()));
        /* Si la mida de l'atribut matriculats és major a 0 vol dir que cridarem al adaptador en una activity
        d'edició assignatura. */
        if(matriculats.size() > 0){
            /* Comprovem si el item del recyclerview correspon a un estudiant matriculat a
            l'assignatura. En cas afirmatiu, marquem el seu checkbox */
            if((matriculats.contains(estudiant.getDni()))) holder.check.setChecked(true);

        }
    }

    @Override
    public int getItemCount() { return estudiants.size(); }
}
