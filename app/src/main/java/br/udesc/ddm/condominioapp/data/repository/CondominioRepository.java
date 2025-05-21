package br.udesc.ddm.condominioapp.data.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import br.udesc.ddm.condominioapp.data.database.CondominioContract;
import br.udesc.ddm.condominioapp.data.database.CondominioDbHelper;
import br.udesc.ddm.condominioapp.data.model.Condominio;

import java.util.ArrayList;
import java.util.List;

public class CondominioRepository {
    private final CondominioDbHelper dbHelper;

    public CondominioRepository(Context context) {
        dbHelper = CondominioDbHelper.getInstance(context);
    }

    public long inserir(Condominio condominio) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CondominioContract.CondominioEntry.COLUMN_NOME, condominio.getNome());
        values.put(CondominioContract.CondominioEntry.COLUMN_ENDERECO, condominio.getEndereco());
        values.put(CondominioContract.CondominioEntry.COLUMN_TAXA_MENSAL, condominio.getTaxaMensalCondominio());
        values.put(CondominioContract.CondominioEntry.COLUMN_FATOR_METRAGEM, condominio.getFatorMultiplicadorDeMetragem());
        values.put(CondominioContract.CondominioEntry.COLUMN_VALOR_GARAGEM, condominio.getValorVagaGaragem());

        long id = db.insert(CondominioContract.CondominioEntry.TABLE_NAME, null, values);

        if (id != -1) {
            condominio.setId(id);
        }

        return id;
    }

    public int atualizar(Condominio condominio) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CondominioContract.CondominioEntry.COLUMN_NOME, condominio.getNome());
        values.put(CondominioContract.CondominioEntry.COLUMN_ENDERECO, condominio.getEndereco());
        values.put(CondominioContract.CondominioEntry.COLUMN_TAXA_MENSAL, condominio.getTaxaMensalCondominio());
        values.put(CondominioContract.CondominioEntry.COLUMN_FATOR_METRAGEM, condominio.getFatorMultiplicadorDeMetragem());
        values.put(CondominioContract.CondominioEntry.COLUMN_VALOR_GARAGEM, condominio.getValorVagaGaragem());

        String selection = CondominioContract.CondominioEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(condominio.getId())};

        return db.update(
                CondominioContract.CondominioEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    public int remover(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String selection = CondominioContract.CondominioEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        return db.delete(
                CondominioContract.CondominioEntry.TABLE_NAME,
                selection,
                selectionArgs);
    }

    public Condominio buscarPorId(long id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                CondominioContract.CondominioEntry._ID,
                CondominioContract.CondominioEntry.COLUMN_NOME,
                CondominioContract.CondominioEntry.COLUMN_ENDERECO,
                CondominioContract.CondominioEntry.COLUMN_TAXA_MENSAL,
                CondominioContract.CondominioEntry.COLUMN_FATOR_METRAGEM,
                CondominioContract.CondominioEntry.COLUMN_VALOR_GARAGEM
        };

        String selection = CondominioContract.CondominioEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor cursor = db.query(
                CondominioContract.CondominioEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        Condominio condominio = null;

        if (cursor != null && cursor.moveToFirst()) {
            condominio = cursorToCondominio(cursor);
            cursor.close();
        }

        return condominio;
    }

    public List<Condominio> listarTodos() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                CondominioContract.CondominioEntry._ID,
                CondominioContract.CondominioEntry.COLUMN_NOME,
                CondominioContract.CondominioEntry.COLUMN_ENDERECO,
                CondominioContract.CondominioEntry.COLUMN_TAXA_MENSAL,
                CondominioContract.CondominioEntry.COLUMN_FATOR_METRAGEM,
                CondominioContract.CondominioEntry.COLUMN_VALOR_GARAGEM
        };

        String sortOrder = CondominioContract.CondominioEntry.COLUMN_NOME + " ASC";

        Cursor cursor = db.query(
                CondominioContract.CondominioEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        List<Condominio> condominios = new ArrayList<>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                condominios.add(cursorToCondominio(cursor));
            }
            cursor.close();
        }

        return condominios;
    }

    /**
     * Converte um cursor em um objeto Condominio
     * @param cursor Cursor posicionado na linha desejada
     * @return Objeto Condominio preenchido com os dados do cursor
     */
    private Condominio cursorToCondominio(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndexOrThrow(CondominioContract.CondominioEntry._ID));
        String nome = cursor.getString(cursor.getColumnIndexOrThrow(CondominioContract.CondominioEntry.COLUMN_NOME));
        String endereco = cursor.getString(cursor.getColumnIndexOrThrow(CondominioContract.CondominioEntry.COLUMN_ENDERECO));
        double taxaMensal = cursor.getDouble(cursor.getColumnIndexOrThrow(CondominioContract.CondominioEntry.COLUMN_TAXA_MENSAL));
        double fatorMetragem = cursor.getDouble(cursor.getColumnIndexOrThrow(CondominioContract.CondominioEntry.COLUMN_FATOR_METRAGEM));
        double valorGaragem = cursor.getDouble(cursor.getColumnIndexOrThrow(CondominioContract.CondominioEntry.COLUMN_VALOR_GARAGEM));

        return new Condominio(id, nome, endereco, taxaMensal, fatorMetragem, valorGaragem);
    }
}