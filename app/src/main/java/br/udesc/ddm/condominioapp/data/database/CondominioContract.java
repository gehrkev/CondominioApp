package br.udesc.ddm.condominioapp.data.database;

import android.provider.BaseColumns;

public final class CondominioContract {

    private CondominioContract() {}

    public static class CondominioEntry implements BaseColumns {
        public static final String TABLE_NAME = "condominio";
        public static final String COLUMN_NOME = "nome";
        public static final String COLUMN_CEP = "cep";
        public static final String COLUMN_LOGRADOURO = "logradouro";
        public static final String COLUMN_COMPLEMENTO = "complemento";
        public static final String COLUMN_BAIRRO = "bairro";
        public static final String COLUMN_LOCALIDADE = "localidade";
        public static final String COLUMN_UF = "uf";
        public static final String COLUMN_TAXA_MENSAL = "taxa_mensal_condominio";
        public static final String COLUMN_FATOR_METRAGEM = "fator_multiplicador_metragem";
        public static final String COLUMN_VALOR_GARAGEM = "valor_vaga_garagem";
    }

    public static class BlocoEntry implements BaseColumns {
        public static final String TABLE_NAME = "bloco";
        public static final String COLUMN_NOME = "nome";
        public static final String COLUMN_ANDARES = "andares";
        public static final String COLUMN_VAGAS_GARAGEM = "vagas_de_garagem";
        public static final String COLUMN_CONDOMINIO_ID = "id_condominio";
    }

    public static class ApartamentoEntry implements BaseColumns {
        public static final String TABLE_NAME = "apartamento";
        public static final String COLUMN_NUMERO = "numero";
        public static final String COLUMN_METRAGEM = "metragem";
        public static final String COLUMN_VAGAS_GARAGEM = "vagas_de_garagem";
        public static final String COLUMN_VALOR_ALUGUEL = "valor_aluguel";
        public static final String COLUMN_BLOCO_ID = "id_bloco";
    }

    public static class LocatarioEntry implements BaseColumns {
        public static final String TABLE_NAME = "locatario";
        public static final String COLUMN_NOME = "nome";
        public static final String COLUMN_CPF = "cpf";
        public static final String COLUMN_TELEFONE = "telefone";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_APARTAMENTO_ID = "id_apartamento";
    }
}