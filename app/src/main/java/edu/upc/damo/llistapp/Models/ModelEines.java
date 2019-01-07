package edu.upc.damo.llistapp.Models;

import android.content.Context;

import edu.upc.damo.llistapp.DB.DBManager;
import edu.upc.damo.llistapp.Utils.Utils;

public class ModelEines {

    private String[] itemsName = new String[] {
            "Esborra dades Estudiants",
            "Esborra dades Assignatures",
            "Esborra dades Assistències" };

    private String[] itemsDesc = new String[] {
            "Esborra tots els estudiants de la base de dades",
            "Esborra totes les assignatures de la base de dades",
            "Esborra totes les assistències de la base de dades" };

    private DBManager conn;
    private Context context;

    public ModelEines(Context context){
        this.context = context;
        conn = new DBManager(context);
    }

    public String[] getItemsName() { return itemsName; }
    public String[] getItemsDesc() { return itemsDesc; }

    public void deleteDBtable(String table_name) {
        conn.deleteTable(table_name);
        if(table_name.equals("Estudiants")){
            Utils.toastMessage(table_name+" eliminats",context);
        }
        else Utils.toastMessage(table_name+" eliminades", context);
    }
}
