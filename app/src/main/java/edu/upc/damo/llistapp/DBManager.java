package edu.upc.damo.llistapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBManager {

    private DBHelper dbHelper;
    private static final String TAG = "DBManager";

    DBManager(Context context){
        this.dbHelper = new DBHelper(context);
    }

    /* Operacions CRUD del cada Taula de la Base de dades */

    // Inserts
    public boolean insereixEstudiant(Estudiant e){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.Table1.COLUMN_NAME_COL1,e.getNom());
        values.put(DBContract.Table1.COLUMN_NAME_COL2,e.getCognoms());
        values.put(DBContract.Table1.COLUMN_NAME_COL3,e.getDni());
        values.put(DBContract.Table1.COLUMN_NAME_COL4,e.getMail());
        long res = db.insert(DBContract.Table1.TABLE_NAME,null,values);

        Log.d(TAG,"insereixEstudiant" + e.getNom() + "a" + DBContract.Table1.TABLE_NAME);
        //Retornem cert si l'estudiant s'ha inserit correctament en la DB, altrment retornem fals.
        return res != -1;
    }

    public void insereixAssignatura(Assignatura a, List<Estudiant> llistaEst){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.Table2.COLUMN_NAME_COL1,a.getId());
        values.put(DBContract.Table2.COLUMN_NAME_COL2,a.getNom());
        //Inserim la nova fila a la taula
        db.insert(DBContract.Table2.TABLE_NAME,null,values);

        //Assignem Estudiants a la nova assignatura
        for (Estudiant e : llistaEst){
            insereixEstudiantsAssignatura(e.getDni(), a.getId());
        }
    }

    private void insereixEstudiantsAssignatura(String dniEstudiant, int idAssignatura){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.Table12.COLUMN_NAME_COL1,dniEstudiant);
        values.put(DBContract.Table12.COLUMN_NAME_COL2,idAssignatura);
        //Inserim la nova fila a la taula
        db.insert(DBContract.Table12.TABLE_NAME,null,values);
    }

    public void insereixAssistencia(Assistencia ast, Assignatura a, List<Estudiant> llistaEst){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.Table3.COLUMN_NAME_COL1, ast.getData());
        values.put(DBContract.Table3.COLUMN_NAME_COL2, a.getId());
        //Inserim la nova fila a la taula
        db.insert(DBContract.Table3.TABLE_NAME,null,values);

        //Assignem la relacio entre els Estudiants i l'assistència
        for (Estudiant e : llistaEst){
            insereixEstudiantsAssistencia(e.getDni(), ast.getData());
        }
    }

    private void insereixEstudiantsAssistencia(String idEstudiant, long idAssistencia){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.Table13.COLUMN_NAME_COL1,idEstudiant);
        values.put(DBContract.Table13.COLUMN_NAME_COL2,idAssistencia);
        //Inserim la nova fila a la taula
        db.insert(DBContract.Table3.TABLE_NAME,null,values);
    }

    //Updates

    /* Com que es poden modificar diferents camps d'un Estudiant, modifiquem directament tots els
    camps passant un nou objecte Estudiant */
    public void updateEstudiant(Estudiant old, Estudiant nou){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DBContract.Table1.COLUMN_NAME_COL1, nou.getNom());
        values.put(DBContract.Table1.COLUMN_NAME_COL2, nou.getCognoms());
        values.put(DBContract.Table1.COLUMN_NAME_COL3, nou.getDni());
        values.put(DBContract.Table1.COLUMN_NAME_COL4, nou.getMail());

        //Actualitzem la Base de dades amb els nous valors.
        db.update(DBContract.Table1.TABLE_NAME, values,
                DBContract.Table1.COLUMN_NAME_COL3 + "=?",
                new String[] {old.getDni()} );
    }

    public void updateAssignatura(String nom, int id){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DBContract.Table2.COLUMN_NAME_COL2, nom);

        //Actualitzem la Base de dades amb els nous valors.
        db.update(DBContract.Table2.TABLE_NAME, values,
                DBContract.Table2.COLUMN_NAME_COL1 + "=?",
                new String[] {Integer.toString(id)} );
    }

    public void updateEstudiantAssistencia(String idEstudiant, long idAssistencia, Boolean present){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DBContract.Table13.COLUMN_NAME_COL3, present ? 1 : 0);

        //Actualitzem la Base de dades amb els nous valors.
        db.update(DBContract.Table13.TABLE_NAME, values,
                DBContract.Table13.COLUMN_NAME_COL1 + "=? AND "
                        + DBContract.Table13.COLUMN_NAME_COL2 + "=?", new String[]
                        {idEstudiant, Long.toString(idAssistencia)} );
    }

    // Deletes

    public void deleteEstudiant(String dniEstudiant){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DBContract.Table1.TABLE_NAME,DBContract.Table1.COLUMN_NAME_COL3 + "=?",
                new String[] {dniEstudiant});

        db.close();
    }

    public void deleteAssignatura(int idAssignatura){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DBContract.Table2.TABLE_NAME,DBContract.Table2.COLUMN_NAME_COL1 + "=?",
                new String[] {Integer.toString(idAssignatura)});

        db.close();
    }

    public void deleteAssistencia(long idAssistencia){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DBContract.Table3.TABLE_NAME,DBContract.Table3.COLUMN_NAME_COL1 + "=?",
                new String[] {Long.toString(idAssistencia)});

        db.close();
    }

    //Queries
    public Estudiant getEstudiant(String dniEstudiant) {
        Estudiant e = new Estudiant();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBContract.Table1.TABLE_NAME, null,
                DBContract.Table1.COLUMN_NAME_COL3 + "=?", new String[]{dniEstudiant},
                null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            e = new Estudiant();
            e.setNom(cursor.getString(cursor.getColumnIndex(DBContract.Table1.COLUMN_NAME_COL1)));
            e.setCognoms(cursor.getString(cursor.getColumnIndex(DBContract.Table1.COLUMN_NAME_COL2)));
            e.setDni(cursor.getString(cursor.getColumnIndex(DBContract.Table1.COLUMN_NAME_COL3)));
            e.setMail(cursor.getString(cursor.getColumnIndex(DBContract.Table1.COLUMN_NAME_COL4)));
        }
        return e;
    }

    public Map<String, Estudiant> getEstudiants(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Map<String, Estudiant> mapEst = new HashMap<String, Estudiant>();

        Cursor cursor = db.query(DBContract.Table1.TABLE_NAME, null, null,
                null, null, null, "dni ASC");

        if (cursor != null){
            if (cursor.moveToFirst()){
                do {
                    Estudiant e = new Estudiant();
                    e.setNom(cursor.getString(cursor.getColumnIndex(DBContract.Table1.COLUMN_NAME_COL1)));
                    e.setCognoms(cursor.getString(cursor.getColumnIndex(DBContract.Table1.COLUMN_NAME_COL2)));
                    e.setDni(cursor.getString(cursor.getColumnIndex(DBContract.Table1.COLUMN_NAME_COL3)));
                    e.setMail(cursor.getString(cursor.getColumnIndex(DBContract.Table1.COLUMN_NAME_COL4)));
                    mapEst.put(e.getDni(), e);
                } while (cursor.moveToNext());
            }
        }
        return mapEst;
    }

    public List<Estudiant> getLlistaEstudiants(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Estudiant> list = new ArrayList<Estudiant>();

        Cursor cursor = db.query(DBContract.Table1.TABLE_NAME, null, null,
                null, null, null, "dni ASC");
        if (cursor != null){
            if (cursor.moveToFirst()){
                do {
                    Estudiant e = new Estudiant();
                    e.setNom(cursor.getString(cursor.getColumnIndex(DBContract.Table1.COLUMN_NAME_COL1)));
                    e.setCognoms(cursor.getString(cursor.getColumnIndex(DBContract.Table1.COLUMN_NAME_COL2)));
                    e.setDni(cursor.getString(cursor.getColumnIndex(DBContract.Table1.COLUMN_NAME_COL3)));
                    e.setMail(cursor.getString(cursor.getColumnIndex(DBContract.Table1.COLUMN_NAME_COL4)));
                    list.add(e);
                } while (cursor.moveToNext());
            }
        }
        return list;

    }

    public List<String> getEstudiantsAssignatura(int idAssignatura){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<String> list = new ArrayList<String>();

        Cursor cursor = db.query(DBContract.Table12.TABLE_NAME, new String[] {
                DBContract.Table12.COLUMN_NAME_COL1}, DBContract.Table12.COLUMN_NAME_COL2 +
                "=?", new String[] {Integer.toString(idAssignatura)}, null, null,null);

        if (cursor != null){
            if (cursor.moveToFirst()){
                do {
                    String value = cursor.getString(cursor.getColumnIndex(DBContract.Table12.COLUMN_NAME_COL1));
                    list.add(value);
                } while (cursor.moveToNext());
            }
        }
        return list;
    }

    public Map<Integer, Assignatura> getAssignatures(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Map<Integer, Assignatura> mapAssig = new HashMap<Integer, Assignatura>();

        Cursor cursor = db.query(DBContract.Table2.TABLE_NAME, null, null,
                null, null, null, "nom ASC");

        if (cursor != null){
            if (cursor.moveToFirst()){
                do {
                    Assignatura a = new Assignatura();
                    a.setId(cursor.getInt(cursor.getColumnIndex(DBContract.Table2.COLUMN_NAME_COL1)));
                    a.setNom(cursor.getString(cursor.getColumnIndex(DBContract.Table2.COLUMN_NAME_COL2)));
                    mapAssig.put(a.getId(), a);
                } while (cursor.moveToNext());
            }
        }
        return mapAssig;
    }

    public List<Long> getAssistenciesAssignatura(int idAssignatura) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Long> list = new ArrayList<Long>();

        Cursor cursor = db.query(DBContract.Table3.TABLE_NAME, new String[]{
                DBContract.Table3.COLUMN_NAME_COL1}, DBContract.Table3.COLUMN_NAME_COL2 +
                "=?", new String[]{Integer.toString(idAssignatura)}, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Long value = Integer.toUnsignedLong(cursor.getInt(cursor.getColumnIndex(DBContract.Table3.COLUMN_NAME_COL1)));
                    list.add(value);
                } while (cursor.moveToNext());
            }
        }
        return list;
    }
}
