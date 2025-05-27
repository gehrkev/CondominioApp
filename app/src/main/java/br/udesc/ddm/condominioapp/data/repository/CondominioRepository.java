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
        values.put(CondominioContract.CondominioEntry.COLUMN_CEP, condominio.getCep());
        values.put(CondominioContract.CondominioEntry.COLUMN_LOGRADOURO, condominio.getLogradouro());
        values.put(CondominioContract.CondominioEntry.COLUMN_COMPLEMENTO, condominio.getComplemento());
        values.put(CondominioContract.CondominioEntry.COLUMN_BAIRRO, condominio.getBairro());
        values.put(CondominioContract.CondominioEntry.COLUMN_LOCALIDADE, condominio.getLocalidade());
        values.put(CondominioContract.CondominioEntry.COLUMN_UF, condominio.getUf());
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
        values.put(CondominioContract.CondominioEntry.COLUMN_CEP, condominio.getCep());
        values.put(CondominioContract.CondominioEntry.COLUMN_LOGRADOURO, condominio.getLogradouro());
        values.put(CondominioContract.CondominioEntry.COLUMN_COMPLEMENTO, condominio.getComplemento());
        values.put(CondominioContract.CondominioEntry.COLUMN_BAIRRO, condominio.getBairro());
        values.put(CondominioContract.CondominioEntry.COLUMN_LOCALIDADE, condominio.getLocalidade());
        values.put(CondominioContract.CondominioEntry.COLUMN_UF, condominio.getUf());
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
                CondominioContract.CondominioEntry.COLUMN_CEP,
                CondominioContract.CondominioEntry.COLUMN_LOGRADOURO,
                CondominioContract.CondominioEntry.COLUMN_COMPLEMENTO,
                CondominioContract.CondominioEntry.COLUMN_BAIRRO,
                CondominioContract.CondominioEntry.COLUMN_LOCALIDADE,
                CondominioContract.CondominioEntry.COLUMN_UF,
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
                CondominioContract.CondominioEntry.COLUMN_CEP,
                CondominioContract.CondominioEntry.COLUMN_LOGRADOURO,
                CondominioContract.CondominioEntry.COLUMN_COMPLEMENTO,
                CondominioContract.CondominioEntry.COLUMN_BAIRRO,
                CondominioContract.CondominioEntry.COLUMN_LOCALIDADE,
                CondominioContract.CondominioEntry.COLUMN_UF,
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

        String cep = cursor.getString(cursor.getColumnIndexOrThrow(CondominioContract.CondominioEntry.COLUMN_CEP));
        String logradouro = cursor.getString(cursor.getColumnIndexOrThrow(CondominioContract.CondominioEntry.COLUMN_LOGRADOURO));
        String complemento = cursor.getString(cursor.getColumnIndexOrThrow(CondominioContract.CondominioEntry.COLUMN_COMPLEMENTO));
        String bairro = cursor.getString(cursor.getColumnIndexOrThrow(CondominioContract.CondominioEntry.COLUMN_BAIRRO));
        String localidade = cursor.getString(cursor.getColumnIndexOrThrow(CondominioContract.CondominioEntry.COLUMN_LOCALIDADE));
        String uf = cursor.getString(cursor.getColumnIndexOrThrow(CondominioContract.CondominioEntry.COLUMN_UF));

        double taxaMensal = cursor.getDouble(cursor.getColumnIndexOrThrow(CondominioContract.CondominioEntry.COLUMN_TAXA_MENSAL));
        double fatorMetragem = cursor.getDouble(cursor.getColumnIndexOrThrow(CondominioContract.CondominioEntry.COLUMN_FATOR_METRAGEM));
        double valorGaragem = cursor.getDouble(cursor.getColumnIndexOrThrow(CondominioContract.CondominioEntry.COLUMN_VALOR_GARAGEM));

        return new Condominio(id, nome,
                cep != null ? cep : "",
                logradouro != null ? logradouro : "",
                complemento != null ? complemento : "",
                bairro != null ? bairro : "",
                localidade != null ? localidade : "",
                uf != null ? uf : "",
                taxaMensal, fatorMetragem, valorGaragem);
    }
}