package com.shivtechs.locationpickermodule.Adapters;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shivtechs.locationpickermodule.Models.Bracelet;
import com.shivtechs.locationpickermodule.ViewHolders.RowType;
import com.shivtechs.locationpickermodule.ViewHolders.WatchViewHolderFactory;

import java.util.List;

public class WatchAdapter extends RecyclerView.Adapter {

    private List<RowType> dataSet;

    public WatchAdapter(List<RowType> dataSet) {
        this.dataSet = dataSet;
    }

    @Override
    public int getItemViewType(int position) {
        return dataSet.get(position).getItemViewType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return WatchViewHolderFactory.create(parent, viewType);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        System.out.println("CREATTTTIN "+dataSet.get(position).getItemViewType());
        dataSet.get(position).onBindViewHolder(holder);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}