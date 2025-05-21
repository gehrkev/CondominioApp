package br.udesc.ddm.condominioapp.data.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import br.udesc.ddm.condominioapp.data.database.CondominioContract;
import br.udesc.ddm.condominioapp.data.database.CondominioDbHelper;
import br.udesc.ddm.condominioapp.data.model.Apartamento;

import java.util.ArrayList;
import java.util.List;

public class ApartamentoRepository {
    private final CondominioDbHelper dbHelper;
    private final LocatarioRepository locatarioRepository;

    public ApartamentoRepository(Context context) {
        dbHelper = CondominioDbHelper.getInstance(context);
        locatarioRepository = new LocatarioRepository(context);
    }

    public long inserir(Apartamento apartamento) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CondominioContract.ApartamentoEntry.COLUMN_NUMERO, apartamento.getNumero());
        values.put(CondominioContract.ApartamentoEntry.COLUMN_METRAGEM, apartamento.getMetragem());
        values.put(CondominioContract.ApartamentoEntry.COLUMN_VAGAS_GARAGEM, apartamento.getVagasDeGaragem());
        values.put(CondominioContract.ApartamentoEntry.COLUMN_VALOR_ALUGUEL, apartamento.getValorAluguel());
        values.put(CondominioContract.ApartamentoEntry.COLUMN_BLOCO_ID, apartamento.getBlocoId());

        long id = db.insert(CondominioContract.ApartamentoEntry.TABLE_NAME, null, values);

        if (id != -1) {
            apartamento.setId(id);
        }

        return id;
    }

    public int atualizar(Apartamento apartamento) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CondominioContract.ApartamentoEntry.COLUMN_NUMERO, apartamento.getNumero());
        values.put(CondominioContract.ApartamentoEntry.COLUMN_METRAGEM, apartamento.getMetragem());
        values.put(CondominioContract.ApartamentoEntry.COLUMN_VAGAS_GARAGEM, apartamento.getVagasDeGaragem());
        values.put(CondominioContract.ApartamentoEntry.COLUMN_VALOR_ALUGUEL, apartamento.getValorAluguel());
        values.put(CondominioContract.ApartamentoEntry.COLUMN_BLOCO_ID, apartamento.getBlocoId());

        String selection = CondominioContract.ApartamentoEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(apartamento.getId())};

        return db.update(
                CondominioContract.ApartamentoEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    public int remover(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String selection = CondominioContract.ApartamentoEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        return db.delete(
                CondominioContract.ApartamentoEntry.TABLE_NAME,
                selection,
                selectionArgs);
    }

    public Apartamento buscarPorId(long id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                CondominioContract.ApartamentoEntry._ID,
                CondominioContract.ApartamentoEntry.COLUMN_NUMERO,
                CondominioContract.ApartamentoEntry.COLUMN_METRAGEM,
                CondominioContract.ApartamentoEntry.COLUMN_VAGAS_GARAGEM,
                CondominioContract.ApartamentoEntry.COLUMN_VALOR_ALUGUEL,
                CondominioContract.ApartamentoEntry.COLUMN_BLOCO_ID
        };

        String selection = CondominioContract.ApartamentoEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor cursor = db.query(
                CondominioContract.ApartamentoEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        Apartamento apartamento = null;

        if (cursor != null && cursor.moveToFirst()) {
            apartamento = cursorToApartamento(cursor);

            apartamento.setLocatario(locatarioRepository.buscarPorApartamento(apartamento.getId()));

            cursor.close();
        }

        return apartamento;
    }

    public List<Apartamento> listarPorBloco(long blocoId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                CondominioContract.ApartamentoEntry._ID,
                CondominioContract.ApartamentoEntry.COLUMN_NUMERO,
                CondominioContract.ApartamentoEntry.COLUMN_METRAGEM,
                CondominioContract.ApartamentoEntry.COLUMN_VAGAS_GARAGEM,
                CondominioContract.ApartamentoEntry.COLUMN_VALOR_ALUGUEL,
                CondominioContract.ApartamentoEntry.COLUMN_BLOCO_ID
        };

        String selection = CondominioContract.ApartamentoEntry.COLUMN_BLOCO_ID + " = ?";
        String[] selectionArgs = {String.valueOf(blocoId)};

        String sortOrder = CondominioContract.ApartamentoEntry.COLUMN_NUMERO + " ASC";

        Cursor cursor = db.query(
                CondominioContract.ApartamentoEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        List<Apartamento> apartamentos = new ArrayList<>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Apartamento apartamento = cursorToApartamento(cursor);

                apartamento.setLocatario(locatarioRepository.buscarPorApartamento(apartamento.getId()));

                apartamentos.add(apartamento);
            }
            cursor.close();
        }

        return apartamentos;
    }

    public List<Apartamento> listarTodos() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                CondominioContract.ApartamentoEntry._ID,
                CondominioContract.ApartamentoEntry.COLUMN_NUMERO,
                CondominioContract.ApartamentoEntry.COLUMN_METRAGEM,
                CondominioContract.ApartamentoEntry.COLUMN_VAGAS_GARAGEM,
                CondominioContract.ApartamentoEntry.COLUMN_VALOR_ALUGUEL,
                CondominioContract.ApartamentoEntry.COLUMN_BLOCO_ID
        };

        String sortOrder = CondominioContract.ApartamentoEntry.COLUMN_NUMERO + " ASC";

        Cursor cursor = db.query(
                CondominioContract.ApartamentoEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        List<Apartamento> apartamentos = new ArrayList<>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Apartamento apartamento = cursorToApartamento(cursor);

                apartamento.setLocatario(locatarioRepository.buscarPorApartamento(apartamento.getId()));

                apartamentos.add(apartamento);
            }
            cursor.close();
        }

        return apartamentos;
    }

    /**
     * Converte um cursor em um objeto Apartamento
     * @param cursor Cursor posicionado na linha desejada
     * @return Objeto Apartamento preenchido com os dados do cursor
     */
    private Apartamento cursorToApartamento(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndexOrThrow(CondominioContract.ApartamentoEntry._ID));
        String numero = cursor.getString(cursor.getColumnIndexOrThrow(CondominioContract.ApartamentoEntry.COLUMN_NUMERO));
        double metragem = cursor.getDouble(cursor.getColumnIndexOrThrow(CondominioContract.ApartamentoEntry.COLUMN_METRAGEM));
        int vagasGaragem = cursor.getInt(cursor.getColumnIndexOrThrow(CondominioContract.ApartamentoEntry.COLUMN_VAGAS_GARAGEM));
        double valorAluguel = cursor.getDouble(cursor.getColumnIndexOrThrow(CondominioContract.ApartamentoEntry.COLUMN_VALOR_ALUGUEL));
        long blocoId = cursor.getLong(cursor.getColumnIndexOrThrow(CondominioContract.ApartamentoEntry.COLUMN_BLOCO_ID));

        return new Apartamento(id, blocoId, numero, metragem, vagasGaragem, valorAluguel);
    }
}