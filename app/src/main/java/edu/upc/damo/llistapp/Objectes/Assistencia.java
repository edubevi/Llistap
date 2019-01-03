package edu.upc.damo.llistapp.Objectes;

import java.util.ArrayList;

public class Assistencia {

    private long date; //Unix time. Temps en segons desde 1970-01-01: 00:00:00 UTC
    private int id;
    private String assignatura;
    private ArrayList<String> dniPresents;

    public Assistencia(){}
    public Assistencia(String assignatura, long date, ArrayList<String> dniPresents) {
        this.date = date;
        this.assignatura = assignatura;
        this.dniPresents = dniPresents;
    }

    /* Getters i Setters */
    public long getDate() { return date; }
    public int getId(){ return id; }
    public String getAssignatura() { return assignatura; }
    public ArrayList<String> getDniPresents() { return dniPresents; }

    public void setDate(long date) { this.date = date; }
    public void setAssignatura(String assignatura) { this.assignatura = assignatura; }
    public void setId(int id) { this.id = id; }
    public void setDniPresents(ArrayList<String> dniPresents) { this.dniPresents = dniPresents; }

}
