package com.example.projekat.ui.fragments;


import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.projekat.R;
import com.example.projekat.databinding.TradesBinding;
import com.example.projekat.model.Trade;
import com.example.projekat.ui.adapters.TradeAdapter;
import com.example.projekat.ui.viewmodel.PriceViewModel;
import com.example.projekat.ui.viewmodel.TradeViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;


public class TradesFragment extends Fragment {


    private TradesBinding mBinding;
    private TradeAdapter mAdapter;
    private TradeViewModel tradeVm;
    private PriceViewModel priceVm;
    private Map<String, Double> livePrices = Collections.emptyMap();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.trades, container, false);

        mAdapter = new TradeAdapter(this::showCloseDialog);

        mBinding.includeContent.rvTrades.setLayoutManager(new LinearLayoutManager(requireContext()));
        mBinding.includeContent.rvTrades.setAdapter(mAdapter);
        mBinding.includeContent.rvTrades.addItemDecoration(
                new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        );

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tradeVm = new ViewModelProvider(requireActivity()).get(TradeViewModel.class);
        priceVm = new ViewModelProvider(requireActivity()).get(PriceViewModel.class);

        tradeVm.getOpenTrades().observe(getViewLifecycleOwner(), trades -> {
            mAdapter.setTradeList(trades);
            if (trades == null || trades.isEmpty()) {
                mBinding.includeContent.tvEmpty.setVisibility(View.VISIBLE);
            } else {
                mBinding.includeContent.tvEmpty.setVisibility(View.GONE);
            }

            Set<String> symbols = new HashSet<>();
            for (Trade t : trades) symbols.add(t.symbol);
            priceVm.start(symbols);
        });

        priceVm.prices().observe(getViewLifecycleOwner(), prices -> {
            livePrices = prices;
            mAdapter.setLivePrices(prices);
        });

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (priceVm != null) {
            priceVm.stop();
        }

        mBinding = null;
        mAdapter = null;
    }


    private void showCloseDialog(Trade trade) {
        Double last = livePrices.get(trade.symbol);
        if (last == null) {
            Toast.makeText(requireContext(), "Nema live cene za " + trade.symbol, Toast.LENGTH_SHORT).show();
            return;
        }

        double pnl = PriceViewModel.calcPnl(trade.direction, trade.entry, trade.positionSize, last);

        String msg = String.format(
                Locale.US,
                "Symbol: %s\nCurrent: %.5f\nEntry: %.5f\nSize: %.2f\n\nP/L: %.2f\n\nZatvoriti po ovoj ceni?",
                trade.symbol, last, trade.entry, trade.positionSize, pnl
        );

        new AlertDialog.Builder(requireContext())
                .setTitle("Close Trade")
                .setMessage(msg)
                .setPositiveButton("Close", (dialog, which) ->
                        tradeVm.closeTrade(trade.id, last))
                .setNegativeButton("Cancel", null)
                .show();
    }

}




