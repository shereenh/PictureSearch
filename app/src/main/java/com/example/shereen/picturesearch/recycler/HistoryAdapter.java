package com.example.shereen.picturesearch.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.shereen.picturesearch.R;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    private List<String> historySearches;


    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView historyText;

        public MyViewHolder(View view) {
            super(view);

             historyText= view.findViewById(R.id.historyText);

        }
    }

    public HistoryAdapter(List<String> historySearches) {
        this.historySearches = historySearches;
    }



    @Override
    public HistoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_element, parent, false);

        return new HistoryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HistoryAdapter.MyViewHolder holder, int position) {
        String singleSearch = historySearches.get(position);

        holder.historyText.setText(singleSearch);


    }

    @Override
    public int getItemCount() {
        return historySearches.size();
    }
}
