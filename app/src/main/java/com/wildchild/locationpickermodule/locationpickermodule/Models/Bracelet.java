package com.wildchild.locationpickermodule.locationpickermodule.Models;

import androidx.recyclerview.widget.RecyclerView;

import com.wildchild.locationpickermodule.locationpickermodule.ViewHolders.Interfaces.RowType;
import com.wildchild.locationpickermodule.locationpickermodule.ViewHolders.WatchViewHolderFactory;

public class Bracelet implements RowType {
    private int qr_id;
    private String modelName;
    private String couleur;

    private ViewType viewType ;

    public Bracelet(int qr_id, String modelName, String couleur, ViewType viewType) {
        this.qr_id = qr_id;
        this.modelName = modelName;
        this.couleur = couleur;
        this.viewType = viewType;
    }

    public Bracelet(int qr_id, String modelName, String couleur) {
        this.qr_id = qr_id;
        this.modelName = modelName;
        this.couleur = couleur;
    }

    public void setQr_id(int qr_id) {
        this.qr_id = qr_id;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    public int getQr_id() {
        return qr_id;
    }

    public String getModelName() {
        return modelName;
    }

    public String getCouleur() {
        return couleur;
    }

    @Override
    public int getItemViewType() {
        switch (this.viewType){
            case v1:
            {
                return 0;
            }
            case v2: return 1;
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder) {
        switch (this.viewType){
            case v1: {
                WatchViewHolderFactory.WatchViewHolderV1 textViewHolder = (WatchViewHolderFactory.WatchViewHolderV1) viewHolder;
                textViewHolder.textView.setText(this.modelName);
                break;
            }

            case v2: {
                WatchViewHolderFactory.WatchViewHolderV2 textViewHolder = (WatchViewHolderFactory.WatchViewHolderV2) viewHolder;
                textViewHolder.textView.setText(this.modelName);
                break;
            }
        }

        //textViewHolder.textView1.setText(text);
    }
}
