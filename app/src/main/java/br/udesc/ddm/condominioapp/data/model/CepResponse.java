package br.udesc.ddm.condominioapp.data.model;

import com.google.gson.annotations.SerializedName;

public class CepResponse {
    @SerializedName("cep")
    private String cep;

    @SerializedName("logradouro")
    private String logradouro;

    @SerializedName("complemento")
    private String complemento;

    @SerializedName("unidade")
    private String unidade;

    @SerializedName("bairro")
    private String bairro;

    @SerializedName("localidade")
    private String localidade;

    @SerializedName("uf")
    private String uf;

    @SerializedName("estado")
    private String estado;

    @SerializedName("regiao")
    private String regiao;

    @SerializedName("ibge")
    private String ibge;

    @SerializedName("gia")
    private String gia;

    @SerializedName("ddd")
    private String ddd;

    @SerializedName("siafi")
    private String siafi;

    @SerializedName("erro")
    private boolean erro;

    public CepResponse() {
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

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getRegiao() {
        return regiao;
    }

    public void setRegiao(String regiao) {
        this.regiao = regiao;
    }

    public String getIbge() {
        return ibge;
    }

    public void setIbge(String ibge) {
        this.ibge = ibge;
    }

    public String getGia() {
        return gia;
    }

    public void setGia(String gia) {
        this.gia = gia;
    }

    public String getDdd() {
        return ddd;
    }

    public void setDdd(String ddd) {
        this.ddd = ddd;
    }

    public String getSiafi() {
        return siafi;
    }

    public void setSiafi(String siafi) {
        this.siafi = siafi;
    }

    public boolean isErro() {
        return erro;
    }

    public void setErro(boolean erro) {
        this.erro = erro;
    }

    public boolean isValid() {
        return !erro && cep != null && !cep.trim().isEmpty();
    }

    public void preencherCondominio(Condominio condominio) {
        if (isValid()) {
            condominio.setCep(this.cep);
            condominio.setLogradouro(this.logradouro != null ? this.logradouro : "");
            condominio.setComplemento(this.complemento != null ? this.complemento : "");
            condominio.setBairro(this.bairro != null ? this.bairro : "");
            condominio.setLocalidade(this.localidade != null ? this.localidade : "");
            condominio.setUf(this.uf != null ? this.uf : "");
        }
    }
}