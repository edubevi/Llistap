package edu.upc.damo.llistapp.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.upc.damo.llistapp.Entitats.Assignatura;
import edu.upc.damo.llistapp.Entitats.Estudiant;

public class DBManager {

    private DBHelper dbHelper;
    private static final String TAG = "DBManager";

    public DBManager(Context context){
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
        values.put(DBContract.Table1.COLUMN_NAME_COL4,e.getFoto());
        values.put(DBContract.Table1.COLUMN_NAME_COL5,e.getMail());
        long res = db.insert(DBContract.Table1.TABLE_NAME,null,values);

        Log.d(TAG,"inseritEstudiant" + e.getNom() + " a " + DBContract.Table1.TABLE_NAME);
        //Retornem cert si l'estudiant s'ha inserit correctament en la DB, altrment retornem fals.
        return res != -1;
    }

    public boolean insereixAssignatura(Assignatura a, ArrayList<String> dniEstudiants){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.Table2.COLUMN_NAME_COL1,a.getNom());
        values.put(DBContract.Table2.COLUMN_NAME_COL2,a.getAlias());
        //Inserim la nova fila a la taula
        long res = db.insert(DBContract.Table2.TABLE_NAME,null,values);

        //Assignem Estudiants a la nova assignatura
        for (String dni : dniEstudiants){
            insereixEstudiantsAssignatura(dni, a.getNom());
        }
        Log.d(TAG,"inseridaAssignatura" + a.getNom() + " a " + DBContract.Table2.TABLE_NAME);
        return res != -1;
    }

    private boolean insereixEstudiantsAssignatura(String dniEstudiant, String nomAssignatura){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.Table12.COLUMN_NAME_COL1,dniEstudiant);
        values.put(DBContract.Table12.COLUMN_NAME_COL2,nomAssignatura);

        //Inserim la nova fila a la taula
        long res = db.insert(DBContract.Table12.TABLE_NAME,null,values);
        Log.d(TAG,"inseritEstudiantAssignatura" + dniEstudiant + "-" + nomAssignatura + " a "
                + DBContract.Table12.TABLE_NAME);

        return res != -1;
   }

   /* public void insereixAssistencia(Assistencia ast, Assignatura a, List<Estudiant> llistaEst){
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
    }*/

/*    private void insereixEstudiantsAssistencia(String idEstudiant, long idAssistencia){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.Table13.COLUMN_NAME_COL1,idEstudiant);
        values.put(DBContract.Table13.COLUMN_NAME_COL2,idAssistencia);
        //Inserim la nova fila a la taula
        db.insert(DBContract.Table3.TABLE_NAME,null,values);
    }*/
    //Updates

    /* Com que es poden modificar diferents camps d'un Estudiant, modifiquem directament tots els
    camps passant un nou objecte Estudiant */
    public boolean updateEstudiant(Estudiant old_est, Estudiant new_est){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DBContract.Table1.COLUMN_NAME_COL1, new_est.getNom());
        values.put(DBContract.Table1.COLUMN_NAME_COL2, new_est.getCognoms());
        values.put(DBContract.Table1.COLUMN_NAME_COL3, new_est.getDni());
        values.put(DBContract.Table1.COLUMN_NAME_COL4, new_est.getFoto());
        values.put(DBContract.Table1.COLUMN_NAME_COL5, new_est.getMail());

        //Actualitzem la Base de dades amb els nous valors.
        long res = db.update(DBContract.Table1.TABLE_NAME, values,
                DBContract.Table1.COLUMN_NAME_COL3 + "=?",
                new String[] {old_est.getDni()} );

        Log.d(TAG,"updateEstudiant" + new_est.getDni() + "a" + DBContract.Table1.TABLE_NAME);
        return res != -1;
    }

    public boolean updateAssignatura(Assignatura old_ass, Assignatura new_ass) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DBContract.Table2.COLUMN_NAME_COL1, new_ass.getNom());
        values.put(DBContract.Table2.COLUMN_NAME_COL2, new_ass.getAlias());

        /* Actualitzem les realacions de la taula EstudiantsAssignatura:
            1 - Eliminen el conjunt de files idEstudiant-idAssignatura on idAssignatura sigui el nom
            de l'assignatura old_ass. */
        long res = db.delete(DBContract.Table12.TABLE_NAME, DBContract.Table12.COLUMN_NAME_COL2 + "=?",
                new String[]{old_ass.getNom()});

        if (res != -1){
            //Actualitzem la Base de dades amb els nous valors nous.
            res = db.update(DBContract.Table2.TABLE_NAME, values,
                    DBContract.Table2.COLUMN_NAME_COL1 + "=?", new String[]{old_ass.getNom()});

        }
        /*2 - Generem les noves files amb el conjunt de relacions entre idEstudiant-idAssignatura
        on idAssignatura sigui el nom de l'assignatura new_ass.*/
        if (res != -1) {
            boolean succes;
            for (String dni : new_ass.getMatriculats()) {
                succes = insereixEstudiantsAssignatura(dni, new_ass.getNom());
                if (!succes) {
                    res = -1;
                    break;
                }
            }
        }
        Log.d(TAG, "updateEstudiant" + new_ass.getNom() + "a" + DBContract.Table1.TABLE_NAME);
        return res != -1;
    }

   /* public void updateEstudiantAssistencia(String idEstudiant, long idAssistencia, Boolean present){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DBContract.Table13.COLUMN_NAME_COL3, present ? 1 : 0);

        //Actualitzem la Base de dades amb els nous valors.
        db.update(DBContract.Table13.TABLE_NAME, values,
                DBContract.Table13.COLUMN_NAME_COL1 + "=? AND "
                        + DBContract.Table13.COLUMN_NAME_COL2 + "=?", new String[]
                        {idEstudiant, Long.toString(idAssistencia)} );
    }*/

    // Deletes

    public void deleteEstudiant(String dniEstudiant){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DBContract.Table1.TABLE_NAME, DBContract.Table1.COLUMN_NAME_COL3 + "=?",
                new String[] {dniEstudiant});

        db.close();
    }

    public void deleteAssignatura(String nomAssignatura){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DBContract.Table2.TABLE_NAME,DBContract.Table2.COLUMN_NAME_COL1 + "=?",
                new String[] {nomAssignatura});

        db.close();
    }

    /*public void deleteAssistencia(long idAssistencia){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DBContract.Table3.TABLE_NAME,DBContract.Table3.COLUMN_NAME_COL1 + "=?",
                new String[] {Long.toString(idAssistencia)});

        db.close();
    }*/

    //Queries
    public boolean checkEstudiantInAssignatura(String nomAssignatura, String dniEstudiant){
        boolean estudiantInAssignatura = false;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT idEstudiant FROM EstudiantsAssignatura WHERE idEstudiant=? AND idAssignatura=?";
        Cursor cursor = db.rawQuery(sql,new String[] {dniEstudiant, nomAssignatura});
        if(cursor.getCount() > 0 ) estudiantInAssignatura = true;
        cursor.close();
        return estudiantInAssignatura;
    }

    public Estudiant getEstudiant(String dniEstudiant) {
        Estudiant e = new Estudiant();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBContract.Table1.TABLE_NAME, null,
                DBContract.Table1.COLUMN_NAME_COL3 + "=?", new String[]{dniEstudiant},
                null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
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

    public List<Estudiant> getEstudiantsList(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Estudiant> list = new ArrayList<Estudiant>();

        Cursor cursor = db.query(DBContract.Table1.TABLE_NAME, null, null,
                null, null, null, "cognoms ASC");
        if (cursor != null){
            if (cursor.moveToFirst()){
                do {
                    Estudiant e = new Estudiant();
                    e.setNom(cursor.getString(cursor.getColumnIndex(DBContract.Table1.COLUMN_NAME_COL1)));
                    e.setCognoms(cursor.getString(cursor.getColumnIndex(DBContract.Table1.COLUMN_NAME_COL2)));
                    e.setDni(cursor.getString(cursor.getColumnIndex(DBContract.Table1.COLUMN_NAME_COL3)));
                    byte[] image = cursor.getBlob(cursor.getColumnIndex(DBContract.Table1.COLUMN_NAME_COL4));
                    e.setFoto(image);
                    //e.setFoto(cursor.getBlob(cursor.getColumnIndex(DBContract.Table1.COLUMN_NAME_COL4)));
                    e.setMail(cursor.getString(cursor.getColumnIndex(DBContract.Table1.COLUMN_NAME_COL5)));
                    list.add(e);
                } while (cursor.moveToNext());
            }
        }
        Collections.sort(list); //Ordenem la llista per ordre de cognoms
        return list;

    }

    public List<Assignatura> getAssignaturesList(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Assignatura> list = new ArrayList<Assignatura>();

        Cursor cursor = db.query(DBContract.Table2.TABLE_NAME, null, null,
                null, null, null, "nom ASC");

        if (cursor != null){
            if (cursor.moveToFirst()){
                do {
                    Assignatura a = new Assignatura();
                    a.setNom(cursor.getString(cursor.getColumnIndex(DBContract.Table2.COLUMN_NAME_COL1)));
                    a.setAlias(cursor.getString(cursor.getColumnIndex(DBContract.Table2.COLUMN_NAME_COL2)));
                    list.add(a);
                } while (cursor.moveToNext());
            }
        }
        return list;
    }

    public List<Estudiant> getEstudiantsAssignaturaList(String nomAssignatura){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Estudiant> list = new ArrayList<Estudiant>();

        Cursor cursor = db.query(DBContract.Table12.TABLE_NAME, new String[] {
                DBContract.Table12.COLUMN_NAME_COL1}, DBContract.Table12.COLUMN_NAME_COL2 +
                "=?", new String[] {nomAssignatura}, null, null,"idEstudiant ASC");

        if (cursor != null){
            if (cursor.moveToFirst()){
                do {
                    Estudiant e = getEstudiant(cursor.getString(cursor.getColumnIndex(DBContract.Table12.COLUMN_NAME_COL1)));
                    list.add(e);
                } while (cursor.moveToNext());
            }
        }
        return list;
    }

    public List<String> getDniEstudiantsAssignaturaList(String nomAssignatura){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<String> list = new ArrayList<>();

        Cursor cursor = db.query(DBContract.Table12.TABLE_NAME, new String[] {
                DBContract.Table12.COLUMN_NAME_COL1}, DBContract.Table12.COLUMN_NAME_COL2 +
                "=?", new String[] {nomAssignatura}, null, null,"idEstudiant ASC");

        if (cursor != null){
            if (cursor.moveToFirst()){
                do {
                    String dni = cursor.getString(cursor.getColumnIndex(DBContract.Table12.COLUMN_NAME_COL1));
                    list.add(dni);
                } while (cursor.moveToNext());
            }
        }
        return list;
    }

   /* public List<Long> getAssistenciesAssignatura(int idAssignatura) {
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
    }*/
}
