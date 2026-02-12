package com.example.projekat.ui.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.projekat.databinding.TradesBinding;
import com.example.projekat.ui.adapters.TradeAdapter;
import com.example.projekat.ui.viewmodel.TradeViewModel;

public class ClosedTradesFragment extends Fragment {

    private TradesBinding binding;
    private TradeViewModel tradeVm;
    private TradeAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = TradesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        tradeVm = new ViewModelProvider(requireActivity()).get(TradeViewModel.class);

        adapter = new TradeAdapter(trade -> {});

        binding.includeContent.rvTrades.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.includeContent.rvTrades.setAdapter(adapter);

        tradeVm.getClosed().observe(getViewLifecycleOwner(), trades -> {
            adapter.submitList(trades);
            boolean empty = trades == null || trades.isEmpty();
            binding.includeContent.tvEmpty.setVisibility(empty ? view.VISIBLE : View.GONE);
            if(empty) {
                binding.includeContent.tvEmpty.setText("Nema zatvorenih trejdova");
            }
        });
    }

    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        adapter = null;
    }
}
