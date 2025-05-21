package br.udesc.ddm.condominioapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Apartamento implements Parcelable {
    private long id;
    private long blocoId;
    private String numero;
    private double metragem;
    private int vagasDeGaragem;
    private double valorAluguel;
    private Locatario locatario;

    public Apartamento() {
    }

    public Apartamento(long id, long blocoId, String numero, double metragem, int vagasDeGaragem, double valorAluguel) {
        this.id = id;
        this.blocoId = blocoId;
        this.numero = numero;
        this.metragem = metragem;
        this.vagasDeGaragem = vagasDeGaragem;
        this.valorAluguel = valorAluguel;
    }

    public Apartamento(long blocoId, String numero, double metragem, int vagasDeGaragem, double valorAluguel) {
        this.blocoId = blocoId;
        this.numero = numero;
        this.metragem = metragem;
        this.vagasDeGaragem = vagasDeGaragem;
        this.valorAluguel = valorAluguel;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getBlocoId() {
        return blocoId;
    }

    public void setBlocoId(long blocoId) {
        this.blocoId = blocoId;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public double getMetragem() {
        return metragem;
    }

    public void setMetragem(double metragem) {
        this.metragem = metragem;
    }

    public int getVagasDeGaragem() {
        return vagasDeGaragem;
    }

    public void setVagasDeGaragem(int vagasDeGaragem) {
        this.vagasDeGaragem = vagasDeGaragem;
    }

    public double getValorAluguel() {
        return valorAluguel;
    }

    public void setValorAluguel(double valorAluguel) {
        this.valorAluguel = valorAluguel;
    }

    public Locatario getLocatario() {
        return locatario;
    }

    public void setLocatario(Locatario locatario) {
        this.locatario = locatario;
    }

    public double calcularAluguel(Condominio condominio) {
        return (condominio.getTaxaMensalCondominio() +
                ((getMetragem() * condominio.getFatorMultiplicadorDeMetragem()) +
                        getVagasDeGaragem() * condominio.getValorVagaGaragem()));
    }

    // Implementação de Parcelable para passar objetos entre Activities
    protected Apartamento(Parcel in) {
        id = in.readLong();
        blocoId = in.readLong();
        numero = in.readString();
        metragem = in.readDouble();
        vagasDeGaragem = in.readInt();
        valorAluguel = in.readDouble();
        locatario = in.readParcelable(Locatario.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(blocoId);
        dest.writeString(numero);
        dest.writeDouble(metragem);
        dest.writeInt(vagasDeGaragem);
        dest.writeDouble(valorAluguel);
        dest.writeParcelable(locatario, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Apartamento> CREATOR = new Creator<Apartamento>() {
        @Override
        public Apartamento createFromParcel(Parcel in) {
            return new Apartamento(in);
        }

        @Override
        public Apartamento[] newArray(int size) {
            return new Apartamento[size];
        }
    };

    @Override
    public String toString() {
        String locatarioNome = locatario != null ? locatario.getNome() : "Sem morador";
        return "Apto nº: " + numero + "\n"
                + "Locatário: " + locatarioNome + "\n"
                + "Metragem: " + metragem + "m²\n"
                + "Vagas de Garagem: " + vagasDeGaragem + "\n"
                + "Valor do Aluguel: R$ " + valorAluguel;
    }
}
