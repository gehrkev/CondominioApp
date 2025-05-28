package br.udesc.ddm.condominioapp.data.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CepApiService {

    @GET("ws/{cep}/json/")
    Call<CepResponse> consultarCep(@Path("cep") String cep);
}