package com.example.projekat.ui.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.projekat.R;
import com.example.projekat.model.Trade;
import com.example.projekat.ui.viewmodel.TradeViewModel;
import com.google.android.material.textfield.TextInputEditText;


public class AddTradeFragment extends Fragment {

    private AutoCompleteTextView ddSymbol, ddDirection;
    private TextInputEditText etEntry, etSize, etSl, etTp;
    private Button btnSave;
    private TradeViewModel vm;

    private final String[] symbols = {"XAUUSD", "EURUSD","BTCUSD", "SOLUSD", "ETHUSD", "XRPUSD", "XAGUSD"};
    private final String[] sides = {"BUY", "SELL"};

    public AddTradeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_trade, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ddSymbol = view.findViewById(R.id.dropdown_symbol);
        ddDirection = view.findViewById(R.id.dropdown_side);
        etEntry = view.findViewById(R.id.input_entry);
        etSize = view.findViewById(R.id.input_size);
        etSl = view.findViewById(R.id.input_sl);
        etTp = view.findViewById(R.id.input_tp);
        btnSave = view.findViewById(R.id.btn_save_trade);

        vm = new ViewModelProvider(requireActivity()).get(TradeViewModel.class);

        ArrayAdapter<String> symbolAdapter = new ArrayAdapter<>(requireContext(), R.layout.list_item_dropdown, symbols);
        ArrayAdapter<String> dirAdapter = new ArrayAdapter<>(requireContext(), R.layout.list_item_dropdown, sides);

        ddSymbol.setAdapter(symbolAdapter);
        ddDirection.setAdapter(dirAdapter);

        btnSave.setOnClickListener(view1 -> {
            String symbol    = safeText(ddSymbol);
            String direction = safeText(ddDirection);
            String entryStr  = safeText(etEntry);
            String sizeStr   = safeText(etSize);
            String slStr     = safeText(etSl);
            String tpStr     = safeText(etTp);

            if(TextUtils.isEmpty(symbol) || TextUtils.isEmpty(direction) ||
                    TextUtils.isEmpty(entryStr) || TextUtils.isEmpty(sizeStr)
                    || TextUtils.isEmpty(slStr) || TextUtils.isEmpty(tpStr)) {
                Toast.makeText(requireContext(), "Popuni obavezna polja.", Toast.LENGTH_SHORT).show();
                return;
            }
            double entry = parseDouble(entryStr);
            double size  = parseDouble(sizeStr);
            double sl    = parseDouble(slStr);
            double tp    = parseDouble(tpStr);


            boolean isBuy = "BUY".equalsIgnoreCase(direction);
            if (isBuy && (sl >= entry || tp <= entry)) {
                Toast.makeText(requireContext(), "BUY: SL < entry, TP > entry.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!isBuy && (sl <= entry || tp >= entry)) {
                Toast.makeText(requireContext(), "SELL: SL > entry, TP < entry.", Toast.LENGTH_SHORT).show();
                return;
            }

            Trade t = new Trade();
            t.symbol = symbol;
            t.entry = entry;
            t.positionSize = size;
            t.direction = direction;
            t.sl = sl;
            t.tp = tp;
            t.exit = null;
            t.timestamp = System.currentTimeMillis();

            vm.insert(t);
            Toast.makeText(requireContext(), "Trade dodat uspešno.", Toast.LENGTH_SHORT).show();

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new TradesFragment())
                    .commit();
        });
    }
    private static String safeText(TextInputEditText et) {
        return et.getText() == null ? "" : et.getText().toString().trim();
    }

    private static String safeText(AutoCompleteTextView tv) {
        return tv.getText() == null ? "" : tv.getText().toString().trim();
    }

    private static double parseDouble(String s) {
        try { return Double.parseDouble(s); }
        catch (Exception e) { return 0.0; }
    }

}
