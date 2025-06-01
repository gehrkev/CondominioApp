package br.udesc.ddm.condominioapp.data.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleMapsApiService {

    @GET("maps/api/geocode/json")
    Call<GeocodingResponse> getGeocodingResult(
            @Query("address") String address,
            @Query("key") String apiKey
    );
}
