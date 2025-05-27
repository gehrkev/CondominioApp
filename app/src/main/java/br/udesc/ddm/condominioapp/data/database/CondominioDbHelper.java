package br.udesc.ddm.condominioapp.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CondominioDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "condominio.db";
    private static final int DATABASE_VERSION = 2;

    private static CondominioDbHelper sInstance;
    public static synchronized CondominioDbHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new CondominioDbHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private static final String SQL_CREATE_CONDOMINIO_TABLE =
            "CREATE TABLE " + CondominioContract.CondominioEntry.TABLE_NAME + " (" +
                    CondominioContract.CondominioEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    CondominioContract.CondominioEntry.COLUMN_NOME + " TEXT NOT NULL," +
                    CondominioContract.CondominioEntry.COLUMN_CEP + " TEXT," +
                    CondominioContract.CondominioEntry.COLUMN_LOGRADOURO + " TEXT," +
                    CondominioContract.CondominioEntry.COLUMN_COMPLEMENTO + " TEXT," +
                    CondominioContract.CondominioEntry.COLUMN_BAIRRO + " TEXT," +
                    CondominioContract.CondominioEntry.COLUMN_LOCALIDADE + " TEXT," +
                    CondominioContract.CondominioEntry.COLUMN_UF + " TEXT," +
                    CondominioContract.CondominioEntry.COLUMN_TAXA_MENSAL + " REAL NOT NULL," +
                    CondominioContract.CondominioEntry.COLUMN_FATOR_METRAGEM + " REAL NOT NULL," +
                    CondominioContract.CondominioEntry.COLUMN_VALOR_GARAGEM + " REAL NOT NULL)";

    private static final String SQL_CREATE_BLOCO_TABLE =
            "CREATE TABLE " + CondominioContract.BlocoEntry.TABLE_NAME + " (" +
                    CondominioContract.BlocoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    CondominioContract.BlocoEntry.COLUMN_NOME + " TEXT NOT NULL," +
                    CondominioContract.BlocoEntry.COLUMN_ANDARES + " INTEGER NOT NULL," +
                    CondominioContract.BlocoEntry.COLUMN_VAGAS_GARAGEM + " INTEGER NOT NULL," +
                    CondominioContract.BlocoEntry.COLUMN_CONDOMINIO_ID + " INTEGER NOT NULL," +
                    "FOREIGN KEY (" + CondominioContract.BlocoEntry.COLUMN_CONDOMINIO_ID + ") " +
                    "REFERENCES " + CondominioContract.CondominioEntry.TABLE_NAME + "(" + CondominioContract.CondominioEntry._ID + ") " +
                    "ON DELETE CASCADE)";

    private static final String SQL_CREATE_APARTAMENTO_TABLE =
            "CREATE TABLE " + CondominioContract.ApartamentoEntry.TABLE_NAME + " (" +
                    CondominioContract.ApartamentoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    CondominioContract.ApartamentoEntry.COLUMN_NUMERO + " TEXT NOT NULL," +
                    CondominioContract.ApartamentoEntry.COLUMN_METRAGEM + " REAL NOT NULL," +
                    CondominioContract.ApartamentoEntry.COLUMN_VAGAS_GARAGEM + " INTEGER NOT NULL," +
                    CondominioContract.ApartamentoEntry.COLUMN_VALOR_ALUGUEL + " REAL NOT NULL," +
                    CondominioContract.ApartamentoEntry.COLUMN_BLOCO_ID + " INTEGER NOT NULL," +
                    "FOREIGN KEY (" + CondominioContract.ApartamentoEntry.COLUMN_BLOCO_ID + ") " +
                    "REFERENCES " + CondominioContract.BlocoEntry.TABLE_NAME + "(" + CondominioContract.BlocoEntry._ID + ") " +
                    "ON DELETE CASCADE)";

    private static final String SQL_CREATE_LOCATARIO_TABLE =
            "CREATE TABLE " + CondominioContract.LocatarioEntry.TABLE_NAME + " (" +
                    CondominioContract.LocatarioEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    CondominioContract.LocatarioEntry.COLUMN_NOME + " TEXT NOT NULL," +
                    CondominioContract.LocatarioEntry.COLUMN_CPF + " TEXT NOT NULL," +
                    CondominioContract.LocatarioEntry.COLUMN_TELEFONE + " TEXT NOT NULL," +
                    CondominioContract.LocatarioEntry.COLUMN_EMAIL + " TEXT NOT NULL," +
                    CondominioContract.LocatarioEntry.COLUMN_APARTAMENTO_ID + " INTEGER," +
                    "FOREIGN KEY (" + CondominioContract.LocatarioEntry.COLUMN_APARTAMENTO_ID + ") " +
                    "REFERENCES " + CondominioContract.ApartamentoEntry.TABLE_NAME + "(" + CondominioContract.ApartamentoEntry._ID + ") " +
                    "ON DELETE SET NULL)";

    private static final String SQL_ADD_CEP_COLUMNS =
            "ALTER TABLE " + CondominioContract.CondominioEntry.TABLE_NAME + " ADD COLUMN " +
                    CondominioContract.CondominioEntry.COLUMN_CEP + " TEXT";

    private static final String SQL_ADD_LOGRADOURO_COLUMNS =
            "ALTER TABLE " + CondominioContract.CondominioEntry.TABLE_NAME + " ADD COLUMN " +
                    CondominioContract.CondominioEntry.COLUMN_LOGRADOURO + " TEXT";

    private static final String SQL_ADD_COMPLEMENTO_COLUMNS =
            "ALTER TABLE " + CondominioContract.CondominioEntry.TABLE_NAME + " ADD COLUMN " +
                    CondominioContract.CondominioEntry.COLUMN_COMPLEMENTO + " TEXT";

    private static final String SQL_ADD_BAIRRO_COLUMNS =
            "ALTER TABLE " + CondominioContract.CondominioEntry.TABLE_NAME + " ADD COLUMN " +
                    CondominioContract.CondominioEntry.COLUMN_BAIRRO + " TEXT";

    private static final String SQL_ADD_LOCALIDADE_COLUMNS =
            "ALTER TABLE " + CondominioContract.CondominioEntry.TABLE_NAME + " ADD COLUMN " +
                    CondominioContract.CondominioEntry.COLUMN_LOCALIDADE + " TEXT";

    private static final String SQL_ADD_UF_COLUMNS =
            "ALTER TABLE " + CondominioContract.CondominioEntry.TABLE_NAME + " ADD COLUMN " +
                    CondominioContract.CondominioEntry.COLUMN_UF + " TEXT";

    private static final String SQL_DELETE_CONDOMINIO_TABLE =
            "DROP TABLE IF EXISTS " + CondominioContract.CondominioEntry.TABLE_NAME;

    private static final String SQL_DELETE_BLOCO_TABLE =
            "DROP TABLE IF EXISTS " + CondominioContract.BlocoEntry.TABLE_NAME;

    private static final String SQL_DELETE_APARTAMENTO_TABLE =
            "DROP TABLE IF EXISTS " + CondominioContract.ApartamentoEntry.TABLE_NAME;

    private static final String SQL_DELETE_LOCATARIO_TABLE =
            "DROP TABLE IF EXISTS " + CondominioContract.LocatarioEntry.TABLE_NAME;

    private CondominioDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys = ON;");

        db.execSQL(SQL_CREATE_CONDOMINIO_TABLE);
        db.execSQL(SQL_CREATE_BLOCO_TABLE);
        db.execSQL(SQL_CREATE_APARTAMENTO_TABLE);
        db.execSQL(SQL_CREATE_LOCATARIO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            try {
                db.execSQL(SQL_ADD_CEP_COLUMNS);
                db.execSQL(SQL_ADD_LOGRADOURO_COLUMNS);
                db.execSQL(SQL_ADD_COMPLEMENTO_COLUMNS);
                db.execSQL(SQL_ADD_BAIRRO_COLUMNS);
                db.execSQL(SQL_ADD_LOCALIDADE_COLUMNS);
                db.execSQL(SQL_ADD_UF_COLUMNS);
            } catch (Exception e) {
                db.execSQL(SQL_DELETE_LOCATARIO_TABLE);
                db.execSQL(SQL_DELETE_APARTAMENTO_TABLE);
                db.execSQL(SQL_DELETE_BLOCO_TABLE);
                db.execSQL(SQL_DELETE_CONDOMINIO_TABLE);
                onCreate(db);
            }
        }
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }
}