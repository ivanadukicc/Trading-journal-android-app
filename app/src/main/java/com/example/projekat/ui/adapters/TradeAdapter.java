package com.example.projekat.ui.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.example.projekat.R;
import com.example.projekat.model.Trade;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.example.projekat.ui.viewmodel.PriceViewModel;

public class TradeAdapter extends RecyclerView.Adapter<TradeAdapter.VH> {



    public interface OnItemLongClick {
        void onLongClick(Trade t);
    }

    private final OnItemLongClick onItemLongClick;

    private final List<Trade> items = new ArrayList<>();

    private Map<String, Double> live = Collections.emptyMap();

    public TradeAdapter( OnItemLongClick onItemLongClick) {
        this.onItemLongClick = onItemLongClick;
    }

    public void setTradeList(List<Trade> newList) {
        List<Trade> oldList = new ArrayList<>(items);
        DiffUtil.DiffResult diff = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return oldList.size();
            }

            @Override
            public int getNewListSize() {
                return newList == null ? 0 : newList.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return oldList.get(oldItemPosition).id == newList.get(newItemPosition).id;
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                Trade o = oldList.get(oldItemPosition), n = newList.get(newItemPosition);
                return o.symbol.equals(n.symbol)
                        && o.direction.equals(n.direction)
                        && o.entry == n.entry
                        && o.sl == n.sl
                        && o.tp == n.tp
                        && (o.exit == null ? n.exit == null : o.exit.equals(n.exit))
                        && o.positionSize == n.positionSize
                        && o.timestamp == n.timestamp;
            }
        });
        items.clear();
        if(newList != null ) {
            items.addAll(newList);
        }
        diff.dispatchUpdatesTo(this);
    }
    static class VH extends RecyclerView.ViewHolder {
        final TextView tvSymbol, tvDirection, tvDate, tvEntry, tvSL, tvTP, tvRR, tvSize, tvPnL;
        VH(@NonNull View v) {
            super(v);
            tvSymbol = v.findViewById(R.id.tvSymbol);
            tvDirection = v.findViewById(R.id.tvDirection);
            tvDate = v.findViewById(R.id.tvDate);
            tvEntry = v.findViewById(R.id.tvEntry);
            tvSL = v.findViewById(R.id.tvSL);
            tvTP = v.findViewById(R.id.tvTP);
            tvRR = v.findViewById(R.id.tvRR);
            tvSize = v.findViewById(R.id.tvSize);
            tvPnL = v.findViewById(R.id.tvPnl);
        }
    }
    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trade, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {

        Trade t = items.get(position);


        h.tvSymbol.setText(t.symbol);
        h.tvDirection.setText(t.direction);

        int color = t.direction.equalsIgnoreCase("BUY")
                ? R.color.teal
                : R.color.purple;
        h.tvDirection.setTextColor(ContextCompat.getColor(h.itemView.getContext(), color));

        h.tvEntry.setText(String.format(Locale.US, "Entry: %.2f", t.entry));
        h.tvSL.setText(String.format(Locale.US, "SL: %.2f", t.sl));
        h.tvTP.setText(String.format(Locale.US, "TP: %.2f", t.tp));
        h.tvSize.setText(String.format(Locale.US, "Size: %.2f", t.positionSize));

        String rr = t.getRRString();
        h.tvRR.setText(rr != null ? "RR: " + rr : "");

        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.US)
                .format(new Date(t.timestamp));
        h.tvDate.setText(date);

        if (t.isClosed()) {
            double pnl = t.getPnL();
            h.tvPnL.setText(String.format(Locale.US, "P/L: %.2f", pnl));
            h.tvPnL.setTextColor(pnl >= 0
                    ? Color.parseColor("#2E7D32")
                    : Color.parseColor("#C62828"));
        }
        else if (live != null) {
            Double last = live.get(t.symbol);
            if (last != null) {
                h.tvSymbol.setText(t.symbol + "  " + String.format(Locale.US, "%.2f", last));

                double pnl = PriceViewModel.calcPnl(t.direction, t.entry, t.positionSize, last);
                h.tvPnL.setText(String.format(Locale.US, "P/L: %.2f", pnl));
                h.tvPnL.setTextColor(pnl >= 0
                        ? Color.parseColor("#2E7D32")
                        : Color.parseColor("#C62828"));
            } else {
                h.tvPnL.setText("P/L: --");
                h.tvPnL.setTextColor(Color.GRAY);
            }
        } else {
            h.tvPnL.setText("P/L: --");
            h.tvPnL.setTextColor(Color.GRAY);
        }

        h.itemView.setOnLongClickListener(v -> {
            if (onItemLongClick != null) {
                onItemLongClick.onLongClick(t);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setLivePrices(Map<String, Double> m) {
        this.live = m;
        notifyDataSetChanged();
    }

   public void submitList(List<Trade> newTrades) {
        this.items.clear();
        if(newTrades != null) {
            this.items.addAll(newTrades);
        }
        notifyDataSetChanged();
   }


}
