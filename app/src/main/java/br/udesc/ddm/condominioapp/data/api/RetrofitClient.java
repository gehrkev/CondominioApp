package br.udesc.ddm.condominioapp.data.api;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final Map<String, Retrofit> retrofitInstances = new HashMap<>();

    private static CepApiService cepApiService = null;
    private static GoogleMapsApiService googleMapsApiService = null;

    public static Retrofit getClient(String baseUrl) {
        if (!retrofitInstances.containsKey(baseUrl)) {
            retrofitInstances.put(baseUrl, createRetrofit(baseUrl));
        }
        return retrofitInstances.get(baseUrl);
    }

    private static Retrofit createRetrofit(String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(createOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private static OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(ApiConfig.DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(ApiConfig.DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(ApiConfig.DEFAULT_WRITE_TIMEOUT, TimeUnit.SECONDS);

        if (ApiConfig.ENABLE_LOGGING) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
        }

        builder.addInterceptor(createHeadersInterceptor());

        return builder.build();
    }

    private static Interceptor createHeadersInterceptor() {
        return chain -> {
            Request original = chain.request();
            Request.Builder requestBuilder = original.newBuilder()
                    .header("User-Agent", ApiConfig.USER_AGENT)
                    .header("Accept", ApiConfig.CONTENT_TYPE);

            return chain.proceed(requestBuilder.build());
        };
    }

    public static CepApiService getCepApiService() {
        if (cepApiService == null) {
            cepApiService = getClient(ApiConfig.VIA_CEP_BASE_URL).create(CepApiService.class);
        }
        return cepApiService;
    }

    public static GoogleMapsApiService getGoogleMapsApiService() {
        if (googleMapsApiService == null) {
            googleMapsApiService = getClient(ApiConfig.GOOGLE_MAPS_BASE_URL).create(GoogleMapsApiService.class);
        }
        return googleMapsApiService;
    }

    public static <T> T createService(Class<T> serviceClass, String baseUrl) {
        return getClient(baseUrl).create(serviceClass);
    }

    public static void clearCache() {
        retrofitInstances.clear();
        cepApiService = null;
        googleMapsApiService = null;
    }
}