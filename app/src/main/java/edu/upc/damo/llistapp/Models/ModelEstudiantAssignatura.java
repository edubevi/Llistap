package edu.upc.damo.llistapp.Models;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.upc.damo.llistapp.DB.DBManager;
import edu.upc.damo.llistapp.Objectes.Estudiant;

public class ModelEstudiantAssignatura {

    private List<Estudiant> estudiants;
    private Set<String> matriculats = new HashSet<>();

    public ModelEstudiantAssignatura(Context context, String nomAssignatura){
        DBManager con = new DBManager(context);
        estudiants = con.getEstudiantsList();
        if(nomAssignatura.length() > 0 ) {
            matriculats.addAll(con.getDniEstudiantsAssignaturaList(nomAssignatura));
        }
    }

    public List<Estudiant> getEstudiants() { return estudiants; }
    public void addMatriculat(int index) { matriculats.add(estudiants.get(index).getDni()); }
    public void removeMatriculat(int index) { matriculats.remove(estudiants.get(index).getDni()); }
    public ArrayList<String> getDniMatriculatsList() { return new ArrayList<>(matriculats); }
}
