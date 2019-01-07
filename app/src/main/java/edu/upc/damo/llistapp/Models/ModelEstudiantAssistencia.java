package edu.upc.damo.llistapp.Models;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.upc.damo.llistapp.DB.DBManager;
import edu.upc.damo.llistapp.Objectes.Estudiant;

public class ModelEstudiantAssistencia {

    private List<Estudiant> estudiants_assignatura;
    private Set<String> estudiants_presents = new HashSet<>();
    private int id_assistencia;

    public ModelEstudiantAssistencia(Context context, String nomAssignatura, int id_assistencia){
        DBManager conn = new DBManager(context);
        estudiants_assignatura = conn.getEstudiantsAssignaturaList(nomAssignatura);
        this.id_assistencia = id_assistencia;
        if(id_assistencia != 0) {
            estudiants_presents.addAll(conn.getDniEstudiantsPresentsList(id_assistencia));
        }
    }

    public ArrayList<String> getDniPresents(){ return new ArrayList<>(estudiants_presents); }
    public ArrayList<String> getDniAbsents(){
        ArrayList<String> absents = new ArrayList<>();
        for(Estudiant e : estudiants_assignatura) {
            if(!estudiants_presents.contains(e.getDni())) absents.add(e.getDni());
        }
        return absents;
    }

    public void addAssistent(int index){
        estudiants_presents.add(estudiants_assignatura.get(index).getDni());
    }
    public void removeAssistent(int index){
        estudiants_presents.remove(estudiants_assignatura.get(index).getDni());
    }

    public int getNumEstudiantsAssignatura(){ return estudiants_assignatura.size(); }
    public int getNumPresents(){ return estudiants_presents.size(); }
    public int getNumAbsents(){ return estudiants_assignatura.size() - estudiants_presents.size(); }
    public int getId_assistencia(){ return id_assistencia; }
    public List<Estudiant> getEstudiants_assignatura(){ return estudiants_assignatura; }
}
