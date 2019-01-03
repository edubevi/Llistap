package edu.upc.damo.llistapp.Objectes;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class Estudiant implements Parcelable,Comparable<Estudiant> {

    private String nom;
    private String cognoms;
    private String dni;
    private String mail;
    private byte[] foto;

    //Constructors
    public Estudiant(){}

    public Estudiant(String nom, String cognoms, String dni, String mail, byte[] foto){
        this.nom = nom;
        this.cognoms = cognoms;
        this.dni = dni;
        this.mail = mail;
        this.foto = foto;
    }

    public Estudiant(Estudiant e){
        this.nom = e.getNom();
        this.cognoms = e.getCognoms();
        this.dni = e.getDni();
        this.mail = e.getMail();
        this.foto = e.getFoto();
    }

    protected Estudiant(Parcel in) {
        nom = in.readString();
        cognoms = in.readString();
        dni = in.readString();
        mail = in.readString();
        foto = in.createByteArray();
    }

    public static final Creator<Estudiant> CREATOR = new Creator<Estudiant>() {
        @Override
        public Estudiant createFromParcel(Parcel in) {
            return new Estudiant(in);
        }

        @Override
        public Estudiant[] newArray(int size) {
            return new Estudiant[size];
        }
    };

    //getters i setters
    public String getNom() { return nom; }
    public String getCognoms() { return cognoms; }
    public String getDni() { return dni; }
    public String getMail() { return mail; }
    public byte[] getFoto() { return foto; }

    public void setNom(String nom) { this.nom = nom; }
    public void setCognoms(String cognoms) { this.cognoms = cognoms; }
    public void setDni(String dni) { this.dni = dni; }
    public void setMail(String mail) { this.mail = mail; }
    public void setFoto(byte[] foto) { this.foto = foto; }

    @Override
    public int compareTo(@NonNull Estudiant o) {
        int nameDiff = this.cognoms.compareToIgnoreCase(o.getCognoms());
        return nameDiff == 0 ? getDni().compareToIgnoreCase(o.getDni()) : nameDiff;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nom);
        dest.writeString(cognoms);
        dest.writeString(dni);
        dest.writeString(mail);
        dest.writeByteArray(foto);
    }
}
