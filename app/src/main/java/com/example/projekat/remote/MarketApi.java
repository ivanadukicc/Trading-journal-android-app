package com.example.projekat.remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MarketApi {

    @GET("price")
    Call<PriceResponse> getPrice(@Query("symbol") String symbol, @Query("apikey") String apikey);

    class PriceResponse {
        public String price;
        public String status;

        public double asDouble() {
            try {
                return (price == null ? Double.NaN : Double.parseDouble(price));
            } catch (Exception e) {
                return Double.NaN;
            }
        }
    }
}
