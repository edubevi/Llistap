package edu.upc.damo.llistapp.Objectes;

import android.support.annotation.NonNull;

import java.util.ArrayList;

public class Assignatura implements Comparable<Assignatura> {

    private String nom;
    private String alias;
    private ArrayList<String> dni_matriculats;

    //Constructors
    public Assignatura(){}

    public Assignatura(String nom, String alias, ArrayList<String> dni_matriculats){
        this.nom = nom;
        this.alias = alias;
        this.dni_matriculats = dni_matriculats;
    }

    //Getters i Setters
    public String getNom() { return nom; }
    public String getAlias() { return alias; }
    public ArrayList<String> getDni_matriculats() { return dni_matriculats; }

    public void setNom(String nom) { this.nom = nom; }
    public void setAlias(String alias) { this.alias = alias; }
    public void setDni_matriculats(ArrayList<String> dni_matriculats) { this.dni_matriculats = dni_matriculats; }

    @Override
    public String toString(){
        return alias;
    }

    @Override
    public int compareTo(@NonNull Assignatura o) {
        return this.nom.compareToIgnoreCase(o.getNom());
    }
}
