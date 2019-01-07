package edu.upc.damo.llistapp.Models;

import android.content.Context;

import java.util.Collections;
import java.util.List;

import edu.upc.damo.llistapp.DB.DBManager;
import edu.upc.damo.llistapp.Objectes.Estudiant;
import edu.upc.damo.llistapp.Utils.Utils;

public class ModelEstudiant {

    private DBManager conn;
    private Context context;
    private List<Estudiant> estudiants;

    public ModelEstudiant(Context context){
        this.context = context;
        conn = new DBManager(context);
        estudiants = conn.getEstudiantsList();
    }

    public boolean addEstudiant(Estudiant e){
        boolean inserted = conn.insereixEstudiant(e);
        if(inserted){
            estudiants.add(e);
            Collections.sort(estudiants);
            Utils.toastMessage("Estudiant Afegit", context);
        }
        else Utils.toastMessage("ERROR al afegir Estudiant a la Base de Dades", context);
        return inserted;
    }

    public boolean editEstudiant(int index, Estudiant newEstudiant){
        boolean updated = conn.updateEstudiant(estudiants.get(index), newEstudiant);
        if(updated) {
            estudiants.set(index, newEstudiant);
            Utils.toastMessage("Estudiant modificat", context);
        }
        else Utils.toastMessage("ERROR al actualitzar Estudiant a la Base de Dades", context);
        return updated;
    }

    public boolean deleteEstudiant(int index){
        boolean deleted = conn.deleteEstudiant(estudiants.get(index).getDni());
        if(deleted) {
            estudiants.remove(index);
            Utils.toastMessage("Estudiant eliminat", context);
        }
        return deleted;
    }

    public Estudiant getEstudiant(int index) { return estudiants.get(index); }
    public List<Estudiant> getListEstudiants() { return estudiants; }
}
