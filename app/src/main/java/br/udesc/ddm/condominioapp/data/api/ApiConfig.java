package br.udesc.ddm.condominioapp.data.api;

import br.udesc.ddm.condominioapp.BuildConfig;

public class ApiConfig {

    public static final String VIA_CEP_BASE_URL = "https://viacep.com.br/";
    public static final String GOOGLE_MAPS_BASE_URL = "https://maps.googleapis.com/";

    public static final String GOOGLE_MAPS_API = BuildConfig.GOOGLE_MAPS_API_KEY;

    public static final int DEFAULT_CONNECT_TIMEOUT = 30;
    public static final int DEFAULT_READ_TIMEOUT = 30;
    public static final int DEFAULT_WRITE_TIMEOUT = 30;

    public static final boolean ENABLE_LOGGING = true;

    public static final String USER_AGENT = "CondominioApp/1.0";
    public static final String CONTENT_TYPE = "application/json";
}