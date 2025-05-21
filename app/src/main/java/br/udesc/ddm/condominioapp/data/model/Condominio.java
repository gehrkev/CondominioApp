package br.udesc.ddm.condominioapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Condominio implements Parcelable {
    private long id;
    private String nome;
    private String endereco;
    private double taxaMensalCondominio;
    private double fatorMultiplicadorDeMetragem;
    private double valorVagaGaragem;

    public Condominio() {
    }

    public Condominio(long id, String nome, String endereco, double taxaMensalCondominio,
                      double fatorMultiplicadorDeMetragem, double valorVagaGaragem) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.taxaMensalCondominio = taxaMensalCondominio;
        this.fatorMultiplicadorDeMetragem = fatorMultiplicadorDeMetragem;
        this.valorVagaGaragem = valorVagaGaragem;
    }

    public Condominio(String nome, String endereco, double taxaMensalCondominio,
                      double fatorMultiplicadorDeMetragem, double valorVagaGaragem) {
        this.nome = nome;
        this.endereco = endereco;
        this.taxaMensalCondominio = taxaMensalCondominio;
        this.fatorMultiplicadorDeMetragem = fatorMultiplicadorDeMetragem;
        this.valorVagaGaragem = valorVagaGaragem;
    }

    // Getters e Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public double getTaxaMensalCondominio() {
        return taxaMensalCondominio;
    }

    public void setTaxaMensalCondominio(double taxaMensalCondominio) {
        this.taxaMensalCondominio = taxaMensalCondominio;
    }

    public double getFatorMultiplicadorDeMetragem() {
        return fatorMultiplicadorDeMetragem;
    }

    public void setFatorMultiplicadorDeMetragem(double fatorMultiplicadorDeMetragem) {
        this.fatorMultiplicadorDeMetragem = fatorMultiplicadorDeMetragem;
    }

    public double getValorVagaGaragem() {
        return valorVagaGaragem;
    }

    public void setValorVagaGaragem(double valorVagaGaragem) {
        this.valorVagaGaragem = valorVagaGaragem;
    }

    // Implementação de Parcelable para passar objetos entre Activities
    protected Condominio(Parcel in) {
        id = in.readLong();
        nome = in.readString();
        endereco = in.readString();
        taxaMensalCondominio = in.readDouble();
        fatorMultiplicadorDeMetragem = in.readDouble();
        valorVagaGaragem = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(nome);
        dest.writeString(endereco);
        dest.writeDouble(taxaMensalCondominio);
        dest.writeDouble(fatorMultiplicadorDeMetragem);
        dest.writeDouble(valorVagaGaragem);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Condominio> CREATOR = new Creator<Condominio>() {
        @Override
        public Condominio createFromParcel(Parcel in) {
            return new Condominio(in);
        }

        @Override
        public Condominio[] newArray(int size) {
            return new Condominio[size];
        }
    };

    @Override
    public String toString() {
        return "Condominio: " + nome + "\n"
                + "Endereço: " + endereco + "\n"
                + "Taxa de condomínio: R$ " + taxaMensalCondominio + "\n"
                + "Valor/M²: R$ " + fatorMultiplicadorDeMetragem + "\n"
                + "Mensalidade Vaga de Garagem: R$ " + valorVagaGaragem;
    }
}
