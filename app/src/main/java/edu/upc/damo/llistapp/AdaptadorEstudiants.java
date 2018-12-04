package edu.upc.damo.llistapp;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdaptadorEstudiants extends RecyclerView.Adapter<AdaptadorEstudiants.ViewHolderEstudiants> {

    public class ViewHolderEstudiants extends RecyclerView.ViewHolder {

        CircleImageView imatge;
        TextView nom, dni;
        ConstraintLayout parentLayout;

        public ViewHolderEstudiants(View itemView){
            super(itemView);
            imatge = (CircleImageView) itemView.findViewById(R.id.imatge_estudiant);
            nom = (TextView) itemView.findViewById(R.id.tv_nomEstudiant);
            dni = (TextView) itemView.findViewById(R.id.tv_dniEstudiant);
            parentLayout = (ConstraintLayout) itemView.findViewById(R.id.parentLayout);
        }
    }

    private static final String TAG = "AdaptadorEstudiants";
    private List<Estudiant> llistaEstudiants = new ArrayList<Estudiant>();
    private Context context;

    public AdaptadorEstudiants(List<Estudiant> llistaEstudiants, Context context) {
        this.llistaEstudiants = llistaEstudiants;
        this.context = context;
    }

    @Override
    public ViewHolderEstudiants onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.layuout_list_estudiants, parent,false);
        return new ViewHolderEstudiants(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderEstudiants holder, int position) {
        Log.d(TAG,"onBindViewHolder: called");

        Estudiant est = llistaEstudiants.get(position);
        String nomComplet = est.getNom() + " " + est.getCognoms();
        holder.nom.setText(nomComplet);
        holder.dni.setText(est.getDni());
        holder.imatge.setImageResource(est.getFoto());
    }

    @Override
    public int getItemCount() { return llistaEstudiants.size();}
}
