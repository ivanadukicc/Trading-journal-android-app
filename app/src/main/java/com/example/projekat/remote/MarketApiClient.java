package com.example.projekat.remote;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class MarketApiClient {
    private static MarketApi api;


    public static MarketApi get() {
        if(api == null) {
            HttpLoggingInterceptor log = new HttpLoggingInterceptor();
            log.setLevel(HttpLoggingInterceptor.Level.BASIC);

            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(log).build();
            Retrofit r = new Retrofit.Builder()
                    .baseUrl("https://api.twelvedata.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
            api = r.create(MarketApi.class);

        }
        return api;
    }

}

