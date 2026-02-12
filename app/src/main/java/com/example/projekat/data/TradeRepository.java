package com.example.projekat.data;

import static com.example.projekat.db.TradeRoomDatabase.databaseWriteExecutor;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.projekat.model.Trade;
import com.example.projekat.db.TradeDao;
import com.example.projekat.db.TradeRoomDatabase;

import java.util.List;

public class TradeRepository {
    private final TradeDao dao;

    public TradeRepository(Context context) {
        dao = TradeRoomDatabase.getDatabase(context).tradeDao();

    }

    public void insert(Trade t) {
        databaseWriteExecutor.execute(() -> dao.insert(t));
    }

    public void update(Trade t) {
        databaseWriteExecutor.execute(() -> dao.update(t));
    }

    public LiveData<List<Trade>> getOpenTrades() {
        return dao.getOpenTrades();
    }
    public void closeTrade(long id, double exitPrice) {
        databaseWriteExecutor.execute(() -> dao.closeTrade(id, exitPrice));

    }
    public LiveData<List<Trade>> getClosedTrades() {
        return dao.getClosedTrades();
    }
}
