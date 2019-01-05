package edu.upc.damo.llistapp.Objectes;

import java.util.ArrayList;

public class Assignatura {

    private String nom;
    private String alias;
    private ArrayList<String> matriculats;

    //Constructors
    public Assignatura(){}

    public Assignatura(String nom, String alias, ArrayList<String> matriculats){
        this.nom = nom;
        this.alias = alias;
        this.matriculats = matriculats;
    }

    //Getters i Setters
    public String getNom() { return nom; }
    public String getAlias() { return alias; }
    public ArrayList<String> getMatriculats() { return matriculats; }

    public void setNom(String nom) { this.nom = nom; }
    public void setAlias(String alias) { this.alias = alias; }

    @Override
    public String toString(){
        return alias;
    }
}
