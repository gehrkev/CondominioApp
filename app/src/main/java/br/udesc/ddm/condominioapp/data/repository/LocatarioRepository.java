package br.udesc.ddm.condominioapp.data.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import br.udesc.ddm.condominioapp.data.database.CondominioContract;
import br.udesc.ddm.condominioapp.data.database.CondominioDbHelper;
import br.udesc.ddm.condominioapp.data.model.Locatario;

import java.util.ArrayList;
import java.util.List;

public class LocatarioRepository {
    private final CondominioDbHelper dbHelper;

    public LocatarioRepository(Context context) {
        dbHelper = CondominioDbHelper.getInstance(context);
    }

    public long inserir(Locatario locatario) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CondominioContract.LocatarioEntry.COLUMN_NOME, locatario.getNome());
        values.put(CondominioContract.LocatarioEntry.COLUMN_CPF, locatario.getCpf());
        values.put(CondominioContract.LocatarioEntry.COLUMN_TELEFONE, locatario.getTelefone());
        values.put(CondominioContract.LocatarioEntry.COLUMN_EMAIL, locatario.getEmail());

        if (locatario.getApartamentoId() > 0) {
            values.put(CondominioContract.LocatarioEntry.COLUMN_APARTAMENTO_ID, locatario.getApartamentoId());
        } else {
            values.putNull(CondominioContract.LocatarioEntry.COLUMN_APARTAMENTO_ID);
        }

        long id = db.insert(CondominioContract.LocatarioEntry.TABLE_NAME, null, values);

        if (id != -1) {
            locatario.setId(id);
        }

        return id;
    }

    public int atualizar(Locatario locatario) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CondominioContract.LocatarioEntry.COLUMN_NOME, locatario.getNome());
        values.put(CondominioContract.LocatarioEntry.COLUMN_CPF, locatario.getCpf());
        values.put(CondominioContract.LocatarioEntry.COLUMN_TELEFONE, locatario.getTelefone());
        values.put(CondominioContract.LocatarioEntry.COLUMN_EMAIL, locatario.getEmail());

        if (locatario.getApartamentoId() > 0) {
            values.put(CondominioContract.LocatarioEntry.COLUMN_APARTAMENTO_ID, locatario.getApartamentoId());
        } else {
            values.putNull(CondominioContract.LocatarioEntry.COLUMN_APARTAMENTO_ID);
        }

        String selection = CondominioContract.LocatarioEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(locatario.getId())};

        return db.update(
                CondominioContract.LocatarioEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    public int associarApartamento(long locatarioId, long apartamentoId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CondominioContract.LocatarioEntry.COLUMN_APARTAMENTO_ID, apartamentoId);

        String selection = CondominioContract.LocatarioEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(locatarioId)};

        return db.update(
                CondominioContract.LocatarioEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    public int desassociarApartamento(long locatarioId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.putNull(CondominioContract.LocatarioEntry.COLUMN_APARTAMENTO_ID);

        String selection = CondominioContract.LocatarioEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(locatarioId)};

        return db.update(
                CondominioContract.LocatarioEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    public int remover(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String selection = CondominioContract.LocatarioEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        return db.delete(
                CondominioContract.LocatarioEntry.TABLE_NAME,
                selection,
                selectionArgs);
    }

    public Locatario buscarPorId(long id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                CondominioContract.LocatarioEntry._ID,
                CondominioContract.LocatarioEntry.COLUMN_NOME,
                CondominioContract.LocatarioEntry.COLUMN_CPF,
                CondominioContract.LocatarioEntry.COLUMN_TELEFONE,
                CondominioContract.LocatarioEntry.COLUMN_EMAIL,
                CondominioContract.LocatarioEntry.COLUMN_APARTAMENTO_ID
        };

        String selection = CondominioContract.LocatarioEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor cursor = db.query(
                CondominioContract.LocatarioEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        Locatario locatario = null;

        if (cursor != null && cursor.moveToFirst()) {
            locatario = cursorToLocatario(cursor);
            cursor.close();
        }

        return locatario;
    }

    public Locatario buscarPorApartamento(long apartamentoId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                CondominioContract.LocatarioEntry._ID,
                CondominioContract.LocatarioEntry.COLUMN_NOME,
                CondominioContract.LocatarioEntry.COLUMN_CPF,
                CondominioContract.LocatarioEntry.COLUMN_TELEFONE,
                CondominioContract.LocatarioEntry.COLUMN_EMAIL,
                CondominioContract.LocatarioEntry.COLUMN_APARTAMENTO_ID
        };

        String selection = CondominioContract.LocatarioEntry.COLUMN_APARTAMENTO_ID + " = ?";
        String[] selectionArgs = {String.valueOf(apartamentoId)};

        Cursor cursor = db.query(
                CondominioContract.LocatarioEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        Locatario locatario = null;

        if (cursor != null && cursor.moveToFirst()) {
            locatario = cursorToLocatario(cursor);
            cursor.close();
        }

        return locatario;
    }

    public List<Locatario> listarTodos() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                CondominioContract.LocatarioEntry._ID,
                CondominioContract.LocatarioEntry.COLUMN_NOME,
                CondominioContract.LocatarioEntry.COLUMN_CPF,
                CondominioContract.LocatarioEntry.COLUMN_TELEFONE,
                CondominioContract.LocatarioEntry.COLUMN_EMAIL,
                CondominioContract.LocatarioEntry.COLUMN_APARTAMENTO_ID
        };

        String sortOrder = CondominioContract.LocatarioEntry.COLUMN_NOME + " ASC";

        Cursor cursor = db.query(
                CondominioContract.LocatarioEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        List<Locatario> locatarios = new ArrayList<>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                locatarios.add(cursorToLocatario(cursor));
            }
            cursor.close();
        }

        return locatarios;
    }

    public List<Locatario> listarSemApartamento() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                CondominioContract.LocatarioEntry._ID,
                CondominioContract.LocatarioEntry.COLUMN_NOME,
                CondominioContract.LocatarioEntry.COLUMN_CPF,
                CondominioContract.LocatarioEntry.COLUMN_TELEFONE,
                CondominioContract.LocatarioEntry.COLUMN_EMAIL,
                CondominioContract.LocatarioEntry.COLUMN_APARTAMENTO_ID
        };

        String selection = CondominioContract.LocatarioEntry.COLUMN_APARTAMENTO_ID + " IS NULL";

        String sortOrder = CondominioContract.LocatarioEntry.COLUMN_NOME + " ASC";

        Cursor cursor = db.query(
                CondominioContract.LocatarioEntry.TABLE_NAME,
                projection,
                selection,
                null,
                null,
                null,
                sortOrder
        );

        List<Locatario> locatarios = new ArrayList<>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                locatarios.add(cursorToLocatario(cursor));
            }
            cursor.close();
        }

        return locatarios;
    }

    /**
     * Converte um cursor em um objeto Locatario
     * @param cursor Cursor posicionado na linha desejada
     * @return Objeto Locatario preenchido com os dados do cursor
     */
    private Locatario cursorToLocatario(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndexOrThrow(CondominioContract.LocatarioEntry._ID));
        String nome = cursor.getString(cursor.getColumnIndexOrThrow(CondominioContract.LocatarioEntry.COLUMN_NOME));
        String cpf = cursor.getString(cursor.getColumnIndexOrThrow(CondominioContract.LocatarioEntry.COLUMN_CPF));
        String telefone = cursor.getString(cursor.getColumnIndexOrThrow(CondominioContract.LocatarioEntry.COLUMN_TELEFONE));
        String email = cursor.getString(cursor.getColumnIndexOrThrow(CondominioContract.LocatarioEntry.COLUMN_EMAIL));

        int apartamentoIdIndex = cursor.getColumnIndexOrThrow(CondominioContract.LocatarioEntry.COLUMN_APARTAMENTO_ID);
        long apartamentoId = cursor.isNull(apartamentoIdIndex) ? -1 : cursor.getLong(apartamentoIdIndex);

        return new Locatario(id, nome, cpf, telefone, email, apartamentoId);
    }
}