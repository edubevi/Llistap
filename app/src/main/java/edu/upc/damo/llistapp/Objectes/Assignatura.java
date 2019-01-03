package edu.upc.damo.llistapp.Objectes;

import java.util.ArrayList;

public class Assignatura {

    private String nom;
    private String alias;
    private ArrayList<String> dniMatriculats;

    //Constructors
    public Assignatura(){}
    public Assignatura(String nom, String alias, ArrayList<String> dniMatriculats ){
        this.nom = nom;
        this.alias = alias;
        this.dniMatriculats = new ArrayList<>(dniMatriculats);
    }
    public Assignatura(Assignatura a){
        this.nom = a.getNom();
        this.alias = a.getAlias();
        this.dniMatriculats = new ArrayList<>(a.getMatriculats());
    }

    //Getters i Setters
    public String getNom() { return nom; }
    public String getAlias() { return alias; }
    public ArrayList<String> getMatriculats() { return this.dniMatriculats; }

    public void setNom(String nom) { this.nom = nom; }
    public void setAlias(String alias) { this.alias = alias; }
    public void setMatriculats(ArrayList<String> matriculats) {
        this.dniMatriculats = new ArrayList<>(matriculats);
    }

    @Override
    public String toString(){
        return alias;
    }
}
