package edu.upc.damo.llistapp.Models;

import android.content.Context;

import java.util.List;

import edu.upc.damo.llistapp.DB.DBManager;
import edu.upc.damo.llistapp.Objectes.Assignatura;
import edu.upc.damo.llistapp.Utils.Utils;

public class ModelAssignatura {

    private DBManager conn;
    private Context context;
    private List<Assignatura> assignaturas;

    public ModelAssignatura(Context context){
        this.context = context;
        conn = new DBManager(this.context);
        assignaturas = conn.getAssignaturesList();
    }

    public boolean addAssignatura(Assignatura a){
        boolean inserted = conn.insereixAssignatura(a);
        if(inserted){
            assignaturas.add(a);
            Utils.toastMessage("Assignatura afegida", context);
        }
        else Utils.toastMessage("ERROR al afegir Assignatura a la base de dades", context);
        return inserted;
    }

    public boolean editAssignatura(int index, Assignatura a){
        boolean updated = conn.updateAssignatura(assignaturas.get(index).getNom(), a);
        if(updated){
            assignaturas.set(index, a);
            Utils.toastMessage("Assignatura modificada", context);
        }
        else Utils.toastMessage("ERROR al actualitzar Assignatura a la base de dades", context);
        return updated;
    }

    public boolean deleteAssignatura(int index){
        boolean deleted = conn.deleteAssignatura(assignaturas.get(index).getNom());
        if(deleted) {
            assignaturas.remove(index);
            Utils.toastMessage("Assignatura eliminada", context);
        }
        return deleted;
    }

    public Assignatura getAssignatura(int index){ return assignaturas.get(index); }

    public List<Assignatura> getListAssignatures() { return assignaturas; }

}
