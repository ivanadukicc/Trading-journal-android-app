package com.example.projekat.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Locale;


@Entity(tableName = "trade")
public class Trade {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long id;

    @NonNull
    public String symbol;
    @NonNull
    public String direction;

    public double entry;
    public double sl;
    public double tp;

    @Nullable
    public Double exit;

    public double positionSize;
    public long timestamp;

    public Trade() {}

    @Ignore
    public Trade(@NonNull String symbol, @NonNull String direction,
                 double entry, double sl, double tp, @Nullable Double exit,
                 double positionSize, long timestamp) {
        this.symbol = symbol;
        this.direction = direction;
        this.entry = entry;
        this.sl = sl;
        this.tp = tp;
        this.exit = exit;
        this.positionSize = positionSize;
        this.timestamp = timestamp;

    }

    public boolean isClosed() {
        return exit != null;
    }

    public double getRR() {
        double risk = Math.abs(entry - sl);
        double reward = Math.abs(tp - entry);
        return risk == 0 ? 0 : reward / risk;
    }

    public double getPnL() {
        if (exit == null) return 0;
        double diff = direction.equalsIgnoreCase("LONG")
                ? exit - entry
                : entry - exit;
        return diff * positionSize;
    }

    public String getRRString() {

        double risk = 0;
        double reward = 0;

        if (direction.equalsIgnoreCase("BUY")) {
            risk = entry - sl;
            reward = tp - entry;
        } else if (direction.equalsIgnoreCase("SELL")) {
            risk = sl - entry;
            reward = entry - tp;
        }

        if (risk <= 0 || reward <= 0) {
            return "–";
        }

        return String.format(Locale.US,"%.1f:%.1f", risk, reward);
    }
}
