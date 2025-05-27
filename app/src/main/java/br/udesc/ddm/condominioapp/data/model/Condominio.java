package br.udesc.ddm.condominioapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Condominio implements Parcelable {
    private long id;
    private String nome;
    private String cep;
    private String logradouro;
    private String complemento;
    private String bairro;
    private String localidade;
    private String uf;
    private double taxaMensalCondominio;
    private double fatorMultiplicadorDeMetragem;
    private double valorVagaGaragem;

    public Condominio() {
    }

    public Condominio(long id, String nome, String cep, String logradouro, String complemento,
                      String bairro, String localidade, String uf, double taxaMensalCondominio,
                      double fatorMultiplicadorDeMetragem, double valorVagaGaragem) {
        this.id = id;
        this.nome = nome;
        this.cep = cep;
        this.logradouro = logradouro;
        this.complemento = complemento;
        this.bairro = bairro;
        this.localidade = localidade;
        this.uf = uf;
        this.taxaMensalCondominio = taxaMensalCondominio;
        this.fatorMultiplicadorDeMetragem = fatorMultiplicadorDeMetragem;
        this.valorVagaGaragem = valorVagaGaragem;
    }

    public Condominio(String nome, String cep, String logradouro, String complemento,
                      String bairro, String localidade, String uf, double taxaMensalCondominio,
                      double fatorMultiplicadorDeMetragem, double valorVagaGaragem) {
        this.nome = nome;
        this.cep = cep;
        this.logradouro = logradouro;
        this.complemento = complemento;
        this.bairro = bairro;
        this.localidade = localidade;
        this.uf = uf;
        this.taxaMensalCondominio = taxaMensalCondominio;
        this.fatorMultiplicadorDeMetragem = fatorMultiplicadorDeMetragem;
        this.valorVagaGaragem = valorVagaGaragem;
    }

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

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getLocalidade() {
        return localidade;
    }

    public void setLocalidade(String localidade) {
        this.localidade = localidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
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

    public String getEnderecoCompleto() {
        StringBuilder endereco = new StringBuilder();

        if (logradouro != null && !logradouro.trim().isEmpty()) {
            endereco.append(logradouro);
        }

        if (complemento != null && !complemento.trim().isEmpty()) {
            if (endereco.length() > 0) endereco.append(", ");
            endereco.append(complemento);
        }

        if (bairro != null && !bairro.trim().isEmpty()) {
            if (endereco.length() > 0) endereco.append(" - ");
            endereco.append(bairro);
        }

        if (localidade != null && !localidade.trim().isEmpty()) {
            if (endereco.length() > 0) endereco.append(", ");
            endereco.append(localidade);
        }

        if (uf != null && !uf.trim().isEmpty()) {
            if (endereco.length() > 0) endereco.append("/");
            endereco.append(uf);
        }

        if (cep != null && !cep.trim().isEmpty()) {
            if (endereco.length() > 0) endereco.append(" - ");
            endereco.append("CEP: ").append(cep);
        }

        return endereco.toString();
    }

    protected Condominio(Parcel in) {
        id = in.readLong();
        nome = in.readString();
        cep = in.readString();
        logradouro = in.readString();
        complemento = in.readString();
        bairro = in.readString();
        localidade = in.readString();
        uf = in.readString();
        taxaMensalCondominio = in.readDouble();
        fatorMultiplicadorDeMetragem = in.readDouble();
        valorVagaGaragem = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(nome);
        dest.writeString(cep);
        dest.writeString(logradouro);
        dest.writeString(complemento);
        dest.writeString(bairro);
        dest.writeString(localidade);
        dest.writeString(uf);
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
                + "Endereço: " + getEnderecoCompleto() + "\n"
                + "Taxa de condomínio: R$ " + taxaMensalCondominio + "\n"
                + "Valor/M²: R$ " + fatorMultiplicadorDeMetragem + "\n"
                + "Mensalidade Vaga de Garagem: R$ " + valorVagaGaragem;
    }
}