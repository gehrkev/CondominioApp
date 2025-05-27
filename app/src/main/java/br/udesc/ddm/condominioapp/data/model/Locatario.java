package br.udesc.ddm.condominioapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Locatario implements Parcelable {
    private long id;
    private String nome;
    private String cpf;
    private String telefone;
    private String email;
    private long apartamentoId;

    public Locatario() {
    }

    public Locatario(long id, String nome, String cpf, String telefone, String email, long apartamentoId) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
        this.apartamentoId = apartamentoId;
    }

    public Locatario(String nome, String cpf, String telefone, String email, long apartamentoId) {
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
        this.apartamentoId = apartamentoId;
    }

    public Locatario(String nome, String cpf, String telefone, String email) {
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
        this.apartamentoId = -1;
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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getApartamentoId() {
        return apartamentoId;
    }

    public void setApartamentoId(long apartamentoId) {
        this.apartamentoId = apartamentoId;
    }

    public boolean temApartamento() {
        return apartamentoId > 0;
    }

    protected Locatario(Parcel in) {
        id = in.readLong();
        nome = in.readString();
        cpf = in.readString();
        telefone = in.readString();
        email = in.readString();
        apartamentoId = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(nome);
        dest.writeString(cpf);
        dest.writeString(telefone);
        dest.writeString(email);
        dest.writeLong(apartamentoId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Locatario> CREATOR = new Creator<Locatario>() {
        @Override
        public Locatario createFromParcel(Parcel in) {
            return new Locatario(in);
        }

        @Override
        public Locatario[] newArray(int size) {
            return new Locatario[size];
        }
    };

    @Override
    public String toString() {
        return "Nome: " + nome + "\n" +
                "CPF: " + cpf + "\n" +
                "Telefone: " + telefone + "\n" +
                "E-mail: " + email;
    }
}