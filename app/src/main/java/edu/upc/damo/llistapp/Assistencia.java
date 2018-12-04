package edu.upc.damo.llistapp;

import java.util.Date;

public class Assistencia {

    private long data; //Unix time. Temps en segons desde 1970-01-01: 00:00:00 UTC
    private Assignatura assignatura;

    public Assistencia(Assignatura assignatura) {
        this.data = System.currentTimeMillis()/1000;
        this.assignatura = assignatura;
    }

    /* Getters i Setters */
    public long getData() { return data; }
    public Assignatura getAssignatura() { return assignatura; }
    public void setData(long data) { this.data = data; }
    public void setAssignatura(Assignatura assignatura) { this.assignatura = assignatura; }
}
