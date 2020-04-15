package com.wildchild.locationpickermodule.locationpickermodule.Adapters;

import android.view.ViewGroup;

import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.wildchild.locationpickermodule.locationpickermodule.DBSynchronisation.Models.Bracelet;
import com.wildchild.locationpickermodule.locationpickermodule.ViewHolders.Interfaces.RecyclerOnItemClickListener;
import com.wildchild.locationpickermodule.locationpickermodule.ViewHolders.Interfaces.RowType;
import com.wildchild.locationpickermodule.locationpickermodule.ViewHolders.WatchViewHolderFactory;

import java.util.ArrayList;
import java.util.List;

public class WatchAdapter extends RecyclerView.Adapter {

    private List<RowType> dataSet;

    RecyclerOnItemClickListener mItemClickListener;

    public WatchAdapter(List<RowType> dataSet , RecyclerOnItemClickListener itemClickListener) {
        this.dataSet = dataSet;
        this.mItemClickListener = itemClickListener;
    }

    public void updateData(List<RowType> bracelets) {
        dataSet.clear();
        dataSet.addAll(bracelets);
        System.out.println("aaaaaaaaaaaaa");
        System.out.println(dataSet.size());
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return dataSet.get(position).getItemViewType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return WatchViewHolderFactory.create(parent, viewType , mItemClickListener);
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