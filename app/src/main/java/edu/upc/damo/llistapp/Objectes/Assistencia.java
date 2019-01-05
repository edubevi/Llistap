package edu.upc.damo.llistapp.Objectes;

public class Assistencia {

    private long date; //Unix time. Temps en segons desde 1970-01-01: 00:00:00 UTC
    private int id;
    private String id_assignatura;

    public Assistencia(){}
    public Assistencia(String id_assignatura, long date) {
        this.date = date;
        this.id_assignatura = id_assignatura;
    }

    /* Getters i Setters */
    public long getDate() { return date; }
    public int getId(){ return id; }
    public String getId_assignatura() { return id_assignatura; }

    public void setDate(long date) { this.date = date; }
    public void setId_assignatura(String id_assignatura) { this.id_assignatura = id_assignatura; }
    public void setId(int id) { this.id = id; }

}
