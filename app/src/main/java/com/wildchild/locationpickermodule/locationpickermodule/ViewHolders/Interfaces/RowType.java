package com.wildchild.locationpickermodule.locationpickermodule.ViewHolders.Interfaces;

import androidx.recyclerview.widget.RecyclerView;

import com.wildchild.locationpickermodule.locationpickermodule.DBSynchronisation.Models.ViewType;

public interface RowType {

    int v1 = 0;
    int v2 = 1;

    int getItemViewType();

    void setItemViewType(ViewType v);

    void onBindViewHolder(RecyclerView.ViewHolder viewHolder);
}