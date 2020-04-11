package com.shivtechs.locationpickermodule.ViewHolders;

import androidx.recyclerview.widget.RecyclerView;

public interface RowType {

    int v1 = 0;
    int v2 = 1;

    int getItemViewType();

    void onBindViewHolder(RecyclerView.ViewHolder viewHolder);
}