package com.example.projekat.ui.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.*;
import com.example.projekat.R;
import java.util.*;
import com.example.projekat.model.NewsItem;
import com.example.projekat.model.Trade;

import android.widget.TextView;



public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.VH>{

    public interface OnClick {
        void onClick(NewsItem n);
    }
    private final List<NewsItem> items = new ArrayList<>();
    private final OnClick onClick;

    public NewsAdapter(OnClick onClick){
        this.onClick = onClick;
    }


    @NonNull
    @Override
    public NewsAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.VH holder, int position) {
        NewsItem n = items.get(position);
        holder.title.setText(n.title);
        holder.source.setText(n.source);
        holder.itemView.setOnClickListener(v -> onClick.onClick(n));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder{
        TextView title, source;
        VH(@NonNull View item) {
            super(item);
            title = item.findViewById(R.id.tvTitle);
            source = item.findViewById(R.id.tvSource);
        }

    }
    public void submitList(List<NewsItem> newTrades) {
        this.items.clear();
        if(newTrades != null) {
            this.items.addAll(newTrades);
        }
        notifyDataSetChanged();
    }
}
