package com.example.projekat.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.projekat.BuildConfig;
import com.example.projekat.data.PriceRepository;

import java.util.Collection;
import java.util.Map;

public class PriceViewModel extends AndroidViewModel {
    private final PriceRepository repo;
    public PriceViewModel(@NonNull Application application) {
        super(application);
        String apiKey = BuildConfig.MY_API_KEY;
        repo = new PriceRepository(apiKey);

    }

    public LiveData<Map<String, Double>> prices() {
        return repo.livePrices();
    }
    public void start(Collection<String> symbols) {
        repo.startPolling(symbols);
    }
    public void stop() {
        repo.stop();
    }
    public static double calcPnl(String direction, double entry, double size, Double last) {
        if (last == null) return 0.0;
        boolean isBuy = "BUY".equalsIgnoreCase(direction);
        double diff = isBuy ? (last - entry) : (entry - last);
        return diff * size;
    }

}
