package com.example.projekat.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.projekat.data.TradeRepository;
import com.example.projekat.model.Trade;

import java.util.List;

public class TradeViewModel extends AndroidViewModel {
    private final TradeRepository repo;


    private LiveData<List<Trade>> closed;

    public TradeViewModel(@NonNull Application app) {
        super(app);
        repo = new TradeRepository(app);
    }

    public void insert(Trade t) {
        repo.insert(t);
    }

    public void update(Trade t) {
        repo.update(t);
    }

    public LiveData<List<Trade>> getOpenTrades() {
        return repo.getOpenTrades();
    }
    public void closeTrade(long id, double exitPrice) {
        repo.closeTrade(id, exitPrice);
    }

    public LiveData<List<Trade>> getClosed() {
        if(closed == null) {
            closed = repo.getClosedTrades();
        }
        return closed;
    }

}
