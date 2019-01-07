package edu.upc.damo.llistapp.Models;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import edu.upc.damo.llistapp.DB.DBManager;
import edu.upc.damo.llistapp.Objectes.Assignatura;
import edu.upc.damo.llistapp.Objectes.Assistencia;
import edu.upc.damo.llistapp.Utils.Utils;

public class ModelAssistencia {

    private List<Assistencia> assistencies = new ArrayList<>();
    private DBManager conn;
    private Context context;
    private String nom_assignatura = "";
    private String alias_assignatura = "";

    public ModelAssistencia(Context context){
        this.context = context;
        conn = new DBManager(context);
        if(nom_assignatura.length() > 0){
            assistencies = conn.getAssistenciesAssignaturaList(nom_assignatura);
        }
    }

    public boolean addAssistencia(Assistencia a, ArrayList<String> dniPresents,
                                  ArrayList<String> dniAbsents){
        long row_id = conn.insereixAssistencia(a, a.getId_assignatura(), dniPresents, dniAbsents);
        //si row_id == -1 no s'ha inserit l'assistència a la base de dades
        if(row_id != -1) {
            a.setId((int)row_id);
            assistencies.add(a);
            Utils.toastMessage("Assistència afegida", context);
        }
        else Utils.toastMessage("ERROR al afegir Assistència a la base de dades", context);
        return row_id != -1;
    }

    public void editAssistencia(int index, Assistencia a, ArrayList<String> dniPresents,
                                ArrayList<String> dniAbsents){
        boolean updated = conn.updateAssistencia(a.getId(), dniPresents, dniAbsents);
        if(updated){
            assistencies.set(index,a);
            Utils.toastMessage("Assistència modificada", context);
        }
        else Utils.toastMessage("ERROR al actualitzar l'Assistència a la base de dades", context);
    }

    public boolean deleteAssistencia(int index){
        boolean deleted = conn.deleteAssistencia(assistencies.get(index).getId());
        if(deleted) {
            assistencies.remove(index);
            Utils.toastMessage("Assistèncie eliminada", context);
        }
        return deleted;
    }

    public String getNom_assignatura() { return nom_assignatura; }
    public String getAlias_assignatura() { return alias_assignatura; }
    public List<Assistencia> getAssistencies() { return assistencies; }
    public Assistencia getAssistencia(int index) { return assistencies.get(index); }
    public List<Assignatura> getAssignatures() { return conn.getAssignaturesList(); }

    public void setNom_assignatura(String nom) {
        nom_assignatura = nom;
        assistencies = conn.getAssistenciesAssignaturaList(nom_assignatura);
    }
    public void setAlias_assignatura(String alias) { alias_assignatura = alias; }


}
