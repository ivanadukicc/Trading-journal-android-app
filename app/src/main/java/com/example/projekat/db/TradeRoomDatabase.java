package com.example.projekat.db;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.projekat.model.Trade;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = { Trade.class }, version = 1, exportSchema = false)
public abstract class TradeRoomDatabase extends RoomDatabase{
    public abstract TradeDao tradeDao();
     private static volatile TradeRoomDatabase INSTANCE;

     private static final int NUMBER_OF_THREADS = 4;
     public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

     public static TradeRoomDatabase getDatabase(final Context context) {
         if(INSTANCE == null) {
             synchronized (TradeRoomDatabase.class) {
                 if(INSTANCE == null) {
                     INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                     TradeRoomDatabase.class,"trading_journal.db")
                             .build();
                 }
             }
         }
         return INSTANCE;
     }



}
