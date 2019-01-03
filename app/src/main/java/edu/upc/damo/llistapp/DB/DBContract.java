package edu.upc.damo.llistapp.DB;

import android.provider.BaseColumns;

/**
 * Classe Contract. Defineix constants per la base de dades.
 */
public final class DBContract {

    // Inicialització de les variables comunes que s'utilitzaran per la creació de les taules
    public static final String TEXT_TYPE = "TEXT";
    public static final String INTEGER_TYPE = "INTEGER";
    public static final String BLOB_TYPE = "BLOB";
    public static final String COMMA_SEP = ",";

    //Constructor
    private DBContract(){}

    //Classe Table1 (Estudiants)
    public static abstract class Table1 implements BaseColumns {
        public static final String TABLE_NAME = "Estudiants";
        public static final String COLUMN_NAME_COL1 = "nom";
        public static final String COLUMN_NAME_COL2 = "cognoms";
        public static final String COLUMN_NAME_COL3 = "dni";
        public static final String COLUMN_NAME_COL4 = "foto";
        public static final String COLUMN_NAME_COL5 = "mail";

        // String que proporciona la creació de la taula "Table1"
        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_NAME_COL1 + " " + TEXT_TYPE + " NOT NULL" + COMMA_SEP +
                COLUMN_NAME_COL2 + " " + TEXT_TYPE + " NOT NULL" + COMMA_SEP +
                COLUMN_NAME_COL3 + " " + TEXT_TYPE + " NOT NULL" + COMMA_SEP +
                COLUMN_NAME_COL4 + " " + BLOB_TYPE + " NOT NULL" + COMMA_SEP +
                COLUMN_NAME_COL5 + " " + TEXT_TYPE + " NOT NULL UNIQUE" + COMMA_SEP +
                "PRIMARY KEY(" + COLUMN_NAME_COL3 + "))";

        // String que proporciona l'eliminació de la taula "Table1"
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    //Class Table2 (Assignatures)
    public static abstract class Table2 implements BaseColumns {
        public static final String TABLE_NAME = "Assignatures";
        public static final String COLUMN_NAME_COL1 = "nom";
        public static final String COLUMN_NAME_COL2 = "alias";

        // String que proporciona la creació de la taula "Table2"
        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_NAME_COL1 + " " + TEXT_TYPE + " NOT NULL" + COMMA_SEP +
                COLUMN_NAME_COL2 + " " + TEXT_TYPE + "NOT NULL UNIQUE" + COMMA_SEP +
                "PRIMARY KEY(" + COLUMN_NAME_COL1 + "))";

        // String que proporciona l'eliminació de la taula "Table2"
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    // Classe Table12 (interrelació many to many entre Alumne i Assignatura).
    public static abstract class Table12 implements BaseColumns {
        public static final String TABLE_NAME = "EstudiantsAssignatura";
        public static final String COLUMN_NAME_COL1 = "idEstudiant";
        public static final String COLUMN_NAME_COL2 = "idAssignatura";

        // String que proporciona la creació de la taula "Table12"
        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_NAME_COL1 + " " + TEXT_TYPE + " NOT NULL" + COMMA_SEP +
                COLUMN_NAME_COL2 + " " + TEXT_TYPE + " NOT NULL" + COMMA_SEP +
                "FOREIGN KEY (" + COLUMN_NAME_COL1 + ") REFERENCES Estudiants(dni) " +
                "ON UPDATE CASCADE ON DELETE CASCADE" + COMMA_SEP +
                "FOREIGN KEY (" + COLUMN_NAME_COL2 + ") REFERENCES Assignatures(nom) " +
                "ON UPDATE CASCADE ON DELETE CASCADE " + COMMA_SEP +
                "PRIMARY KEY (" + COLUMN_NAME_COL1 + COMMA_SEP + COLUMN_NAME_COL2 + "))";

        // String que proporciona l'eliminació de la taula "Table12"
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    //Classe Table3 (Assistencia)

    public static abstract class Table3 implements BaseColumns {
        public static final String TABLE_NAME = "Assistencies";
        public static final String COLUMN_NAME_COL1 = "id";
        public static final String COLUMN_NAME_COL2 = "idAssignatura";
        public static final String COLUMN_NAME_COL3 = "data";

        // String que proporciona la creació de la taula "Table3"
        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_NAME_COL1 + " " + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
                COLUMN_NAME_COL2 + " " + TEXT_TYPE + " NOT NULL" + COMMA_SEP +
                COLUMN_NAME_COL3 + " " + INTEGER_TYPE + " NOT NULL" + COMMA_SEP +
                "FOREIGN KEY (" + COLUMN_NAME_COL2 + ") REFERENCES Assignatures(nom) " +
                "ON UPDATE CASCADE ON DELETE CASCADE)";

        // String que proporciona l'eliminació de la taula "Table3"
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    // Classe Table13 (interrelació many to many entre Alumne i Assistència).
    public static abstract class Table13 implements BaseColumns {
        public static final String TABLE_NAME = "EstudiantsAssistencia";
        public static final String COLUMN_NAME_COL1 = "idEstudiant";
        public static final String COLUMN_NAME_COL2 = "idAssistencia";
        public static final String COLUMN_NAME_COL3 = "present";

        // String que proporciona la creació de la taula "Table12"
        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_NAME_COL1 + " " + TEXT_TYPE + " NOT NULL" + COMMA_SEP +
                COLUMN_NAME_COL2 + " " + TEXT_TYPE + " NOT NULL" + COMMA_SEP +
                COLUMN_NAME_COL3 + " " + INTEGER_TYPE + " DEFAULT 0" + COMMA_SEP +
                "FOREIGN KEY (" + COLUMN_NAME_COL1 + ") REFERENCES Estudiants(dni) " +
                "ON UPDATE CASCADE ON DELETE CASCADE" + COMMA_SEP +
                "FOREIGN KEY (" + COLUMN_NAME_COL2 + ") REFERENCES Assistencies(id) " +
                "ON UPDATE CASCADE ON DELETE CASCADE" + COMMA_SEP +
                "PRIMARY KEY (" + COLUMN_NAME_COL1 + COMMA_SEP + COLUMN_NAME_COL2 + "))";

        // String que proporciona l'eliminació de la taula "Table12"
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
