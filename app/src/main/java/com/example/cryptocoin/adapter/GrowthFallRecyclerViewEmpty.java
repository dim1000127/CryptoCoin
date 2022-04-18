package com.example.cryptocoin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cryptocoin.R;

public class GrowthFallRecyclerViewEmpty extends RecyclerView.Adapter<GrowthFallRecyclerViewEmpty.ViewHolder>{

    @NonNull
    @Override
    public GrowthFallRecyclerViewEmpty.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty_recyclerview_home, parent, false);
        return new  GrowthFallRecyclerViewEmpty.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GrowthFallRecyclerViewEmpty.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
