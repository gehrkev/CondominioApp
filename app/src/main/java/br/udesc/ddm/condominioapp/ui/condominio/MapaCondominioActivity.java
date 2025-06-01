package br.udesc.ddm.condominioapp.ui.condominio;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import br.udesc.ddm.condominioapp.R;
import br.udesc.ddm.condominioapp.data.api.ApiConfig;
import br.udesc.ddm.condominioapp.data.api.GoogleMapsApiService;
import br.udesc.ddm.condominioapp.data.api.RetrofitClient;
import br.udesc.ddm.condominioapp.data.api.GeocodingResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapaCondominioActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private String endereco;
    private String nome;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_condominio);

        endereco = getIntent().getStringExtra("endereco");
        nome = getIntent().getStringExtra("nome");

        ImageButton btnVoltar = findViewById(R.id.btnVoltarMapa);
        btnVoltar.setOnClickListener(v -> finish());

        // Inicializa o mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        buscarCoordenadas();
    }

    private void buscarCoordenadas() {
        GoogleMapsApiService mapsApiService = RetrofitClient.getGoogleMapsApiService();
        Call<GeocodingResponse> call = mapsApiService.getGeocodingResult(endereco, ApiConfig.GOOGLE_MAPS_API);

        call.enqueue(new Callback<GeocodingResponse>() {
            @Override
            public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().getResults().isEmpty()) {
                    double lat = response.body().getLat();
                    double lng = response.body().getLng();
                    mostrarNoMapa(lat, lng);
                } else {
                    Toast.makeText(MapaCondominioActivity.this, "Endereço não encontrado!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GeocodingResponse> call, Throwable t) {
                Toast.makeText(MapaCondominioActivity.this, "Erro: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarNoMapa(double latitude, double longitude) {
        LatLng localizacao = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(localizacao).title(nome));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(localizacao, 16f));
    }
}
