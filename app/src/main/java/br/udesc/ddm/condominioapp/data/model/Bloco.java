package br.udesc.ddm.condominioapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Bloco implements Parcelable {
    private long id;
    private long condominioId;
    private String nome;
    private int andares;
    private int vagasDeGaragem;

    public Bloco() {
    }

    public Bloco(long id, long condominioId, String nome, int andares, int vagasDeGaragem) {
        this.id = id;
        this.condominioId = condominioId;
        this.nome = nome;
        this.andares = andares;
        this.vagasDeGaragem = vagasDeGaragem;
    }

    public Bloco(long condominioId, String nome, int andares, int vagasDeGaragem) {
        this.condominioId = condominioId;
        this.nome = nome;
        this.andares = andares;
        this.vagasDeGaragem = vagasDeGaragem;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCondominioId() {
        return condominioId;
    }

    public void setCondominioId(long condominioId) {
        this.condominioId = condominioId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getAndares() {
        return andares;
    }

    public void setAndares(int andares) {
        this.andares = andares;
    }

    public int getVagasDeGaragem() {
        return vagasDeGaragem;
    }

    public void setVagasDeGaragem(int vagasDeGaragem) {
        this.vagasDeGaragem = vagasDeGaragem;
    }

    protected Bloco(Parcel in) {
        id = in.readLong();
        condominioId = in.readLong();
        nome = in.readString();
        andares = in.readInt();
        vagasDeGaragem = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(condominioId);
        dest.writeString(nome);
        dest.writeInt(andares);
        dest.writeInt(vagasDeGaragem);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Bloco> CREATOR = new Creator<Bloco>() {
        @Override
        public Bloco createFromParcel(Parcel in) {
            return new Bloco(in);
        }

        @Override
        public Bloco[] newArray(int size) {
            return new Bloco[size];
        }
    };

    @Override
    public String toString() {
        return nome + "\n"
                + "Quant. de Andares: " + andares + "\n"
                + "Vagas De Garagem: " + vagasDeGaragem;
    }
}
