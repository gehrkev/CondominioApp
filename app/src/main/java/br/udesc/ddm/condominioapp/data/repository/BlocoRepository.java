package br.udesc.ddm.condominioapp.data.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import br.udesc.ddm.condominioapp.data.database.CondominioContract;
import br.udesc.ddm.condominioapp.data.database.CondominioDbHelper;
import br.udesc.ddm.condominioapp.data.model.Bloco;

import java.util.ArrayList;
import java.util.List;

public class BlocoRepository {
    private final CondominioDbHelper dbHelper;

    public BlocoRepository(Context context) {
        dbHelper = CondominioDbHelper.getInstance(context);
    }

    public long inserir(Bloco bloco) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CondominioContract.BlocoEntry.COLUMN_NOME, bloco.getNome());
        values.put(CondominioContract.BlocoEntry.COLUMN_ANDARES, bloco.getAndares());
        values.put(CondominioContract.BlocoEntry.COLUMN_VAGAS_GARAGEM, bloco.getVagasDeGaragem());
        values.put(CondominioContract.BlocoEntry.COLUMN_CONDOMINIO_ID, bloco.getCondominioId());

        long id = db.insert(CondominioContract.BlocoEntry.TABLE_NAME, null, values);

        if (id != -1) {
            bloco.setId(id);
        }

        return id;
    }

    public int atualizar(Bloco bloco) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CondominioContract.BlocoEntry.COLUMN_NOME, bloco.getNome());
        values.put(CondominioContract.BlocoEntry.COLUMN_ANDARES, bloco.getAndares());
        values.put(CondominioContract.BlocoEntry.COLUMN_VAGAS_GARAGEM, bloco.getVagasDeGaragem());
        values.put(CondominioContract.BlocoEntry.COLUMN_CONDOMINIO_ID, bloco.getCondominioId());

        String selection = CondominioContract.BlocoEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(bloco.getId())};

        return db.update(
                CondominioContract.BlocoEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    public int remover(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String selection = CondominioContract.BlocoEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        return db.delete(
                CondominioContract.BlocoEntry.TABLE_NAME,
                selection,
                selectionArgs);
    }

    public Bloco buscarPorId(long id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                CondominioContract.BlocoEntry._ID,
                CondominioContract.BlocoEntry.COLUMN_NOME,
                CondominioContract.BlocoEntry.COLUMN_ANDARES,
                CondominioContract.BlocoEntry.COLUMN_VAGAS_GARAGEM,
                CondominioContract.BlocoEntry.COLUMN_CONDOMINIO_ID
        };

        String selection = CondominioContract.BlocoEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor cursor = db.query(
                CondominioContract.BlocoEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        Bloco bloco = null;

        if (cursor != null && cursor.moveToFirst()) {
            bloco = cursorToBloco(cursor);
            cursor.close();
        }

        return bloco;
    }

    public List<Bloco> listarPorCondominio(long condominioId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                CondominioContract.BlocoEntry._ID,
                CondominioContract.BlocoEntry.COLUMN_NOME,
                CondominioContract.BlocoEntry.COLUMN_ANDARES,
                CondominioContract.BlocoEntry.COLUMN_VAGAS_GARAGEM,
                CondominioContract.BlocoEntry.COLUMN_CONDOMINIO_ID
        };

        String selection = CondominioContract.BlocoEntry.COLUMN_CONDOMINIO_ID + " = ?";
        String[] selectionArgs = {String.valueOf(condominioId)};

        String sortOrder = CondominioContract.BlocoEntry.COLUMN_NOME + " ASC";

        Cursor cursor = db.query(
                CondominioContract.BlocoEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        List<Bloco> blocos = new ArrayList<>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                blocos.add(cursorToBloco(cursor));
            }
            cursor.close();
        }

        return blocos;
    }

    public List<Bloco> listarTodos() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                CondominioContract.BlocoEntry._ID,
                CondominioContract.BlocoEntry.COLUMN_NOME,
                CondominioContract.BlocoEntry.COLUMN_ANDARES,
                CondominioContract.BlocoEntry.COLUMN_VAGAS_GARAGEM,
                CondominioContract.BlocoEntry.COLUMN_CONDOMINIO_ID
        };

        String sortOrder = CondominioContract.BlocoEntry.COLUMN_NOME + " ASC";

        Cursor cursor = db.query(
                CondominioContract.BlocoEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        List<Bloco> blocos = new ArrayList<>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                blocos.add(cursorToBloco(cursor));
            }
            cursor.close();
        }

        return blocos;
    }

    /**
     * Converte um cursor em um objeto Bloco
     * @param cursor Cursor posicionado na linha desejada
     * @return Objeto Bloco preenchido com os dados do cursor
     */
    private Bloco cursorToBloco(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndexOrThrow(CondominioContract.BlocoEntry._ID));
        String nome = cursor.getString(cursor.getColumnIndexOrThrow(CondominioContract.BlocoEntry.COLUMN_NOME));
        int andares = cursor.getInt(cursor.getColumnIndexOrThrow(CondominioContract.BlocoEntry.COLUMN_ANDARES));
        int vagasGaragem = cursor.getInt(cursor.getColumnIndexOrThrow(CondominioContract.BlocoEntry.COLUMN_VAGAS_GARAGEM));
        long condominioId = cursor.getLong(cursor.getColumnIndexOrThrow(CondominioContract.BlocoEntry.COLUMN_CONDOMINIO_ID));

        return new Bloco(id, condominioId, nome, andares, vagasGaragem);
    }
}