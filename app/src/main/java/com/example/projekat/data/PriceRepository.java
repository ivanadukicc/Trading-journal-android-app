package com.example.projekat.data;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.projekat.remote.MarketApi;
import com.example.projekat.remote.MarketApiClient;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

public class PriceRepository {
    private final MutableLiveData<Map<String, Double>> prices = new MutableLiveData<>(new HashMap<>());
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> task;
    private final String apiKey;

    public PriceRepository(String apiKey) {
        this.apiKey = apiKey;
    }
    public LiveData<Map<String, Double>> livePrices() {
        return prices;
    }

    private String normalizeForex(String s) {
        if (s != null && s.length() == 6 && !s.contains("/")) {
            return s.substring(0,3) + "/" + s.substring(3);
        }
        return s;
    }

    public void startPolling(Collection<String> symbols) {
        stop();
        if (symbols == null || symbols.isEmpty()) return;
        Set<String> set = new HashSet<>(symbols);

        task = scheduler.scheduleWithFixedDelay(() -> {
            for (String s : set) {
                String q = normalizeForex(s);

                Call<MarketApi.PriceResponse> call = MarketApiClient.get().getPrice(q, apiKey);

                call.enqueue(new retrofit2.Callback<MarketApi.PriceResponse>() {

                    @Override
                    public void onResponse(Call<MarketApi.PriceResponse> call, Response<MarketApi.PriceResponse> response) {
                        if(response.isSuccessful() && response.body() != null && !"error".equalsIgnoreCase(response.body().status)) {
                            double p = response.body().asDouble();
                            if(!Double.isNaN(p)) {
                                Map<String, Double> cur = new HashMap<>(prices.getValue());
                                cur.put(s,p);
                                prices.postValue(cur);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MarketApi.PriceResponse> call, Throwable throwable) {
                        Log.e("PriceRepo", "API error", throwable);
                    }
                });
            }
        }, 0, 5, TimeUnit.SECONDS);

    }

    public void stop() {
        if (task != null) {
            task.cancel(true);
            task = null;
        }
    }

}
