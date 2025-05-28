package br.udesc.ddm.condominioapp.data.api;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class GeocodingResponse {

    @SerializedName("results")
    private List<Result> results;

    public List<Result> getResults() {
        return results;
    }

    public double getLat() {
        return results.get(0).geometry.location.lat;
    }

    public double getLng() {
        return results.get(0).geometry.location.lng;
    }

    public static class Result {
        Geometry geometry;
    }

    public static class Geometry {
        Location location;
    }

    public static class Location {
        double lat;
        double lng;
    }
}
