package com.example.projekat.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.projekat.model.Trade;

import java.util.List;

@Dao
public interface TradeDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    long insert(Trade t);

    @Update
    int update(Trade t);

    @Query("SELECT * FROM trade ORDER BY timestamp DESC")
    LiveData<List<Trade>> getAll();

    @Query("SELECT * FROM trade WHERE exit IS NULL ORDER BY timestamp DESC")
    LiveData<List<Trade>> getOpenTrades();
    @Query("UPDATE trade SET exit = :exitPrice WHERE id = :id")
    int closeTrade(long id, double exitPrice);

    @Query("SELECT * FROM trade WHERE exit IS NOT NULL ORDER BY timestamp DESC")
    LiveData<List<Trade>> getClosedTrades();
}
