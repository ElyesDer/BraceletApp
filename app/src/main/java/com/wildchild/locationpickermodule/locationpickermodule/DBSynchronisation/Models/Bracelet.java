package com.wildchild.locationpickermodule.locationpickermodule.DBSynchronisation.Models;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.annotations.SerializedName;
import com.squareup.picasso.Picasso;
import com.wildchild.locationpickermodule.R;
import com.wildchild.locationpickermodule.locationpickermodule.ViewHolders.Interfaces.RowType;
import com.wildchild.locationpickermodule.locationpickermodule.ViewHolders.WatchViewHolderFactory;

import java.io.Serializable;


public class Bracelet implements RowType, Serializable {

    @SerializedName("_id")
    private String id_qr;
    private BModel model;

    public String getId_qr() {
        return id_qr;
    }

    public void setId_qr(String id_qr) {
        this.id_qr = id_qr;
    }

    public BModel getModel() {
        return model;
    }

    public void setModel(BModel model) {
        this.model = model;
    }

    private ViewType viewType;

    @Override
    public int getItemViewType() {
        switch (this.viewType) {
            case v1: {
                return 0;
            }
            case v2:
                return 1;
        }
        return 0;
    }

    @Override
    public void setItemViewType(ViewType v) {
        this.viewType = v;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder) {
        switch (this.viewType) {
            case v1: {
                WatchViewHolderFactory.WatchViewHolderV1 textViewHolder = (WatchViewHolderFactory.WatchViewHolderV1) viewHolder;
                textViewHolder.textView.setText(this.model.getName());

                Picasso.get().load(this.model.getUrl()).into(textViewHolder.imageView);
//                switch (this.model.getVersion()) {
//                    case v1: {
//                        textViewHolder.imageView.setImageDrawable(textViewHolder.context.getDrawable(R.drawable.v1));
//                        break;
//                    }
//                    case v2: {
//                        textViewHolder.imageView.setImageDrawable(textViewHolder.context.getDrawable(R.drawable.v3));
//                        break;
//                    }
//                }
                break;
            }

            case v2: {
                WatchViewHolderFactory.WatchViewHolderV2 textViewHolder = (WatchViewHolderFactory.WatchViewHolderV2) viewHolder;
                textViewHolder.textView.setText(this.model.getName());
                Picasso.get().load(this.model.getUrl()).into(textViewHolder.imageView);

//                switch (this.model.getVersion()) {
//                    case v1: {
//                        textViewHolder.imageView.setImageDrawable(textViewHolder.context.getDrawable(R.drawable.v1));
//                        break;
//                    }
//                    case v2: {
//                        textViewHolder.imageView.setImageDrawable(textViewHolder.context.getDrawable(R.drawable.v3));
//                        break;
//                    }
//                }

                break;
            }
        }

        //textViewHolder.textView1.setText(text);
    }
}
