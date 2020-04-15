package com.wildchild.locationpickermodule.locationpickermodule.DBSynchronisation.Models;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.annotations.SerializedName;
import com.wildchild.locationpickermodule.locationpickermodule.ViewHolders.Interfaces.RowType;
import com.wildchild.locationpickermodule.locationpickermodule.ViewHolders.WatchViewHolderFactory;

public class Bracelet implements RowType {

    @SerializedName("_id")
    private String id_qr;
    private String model;
    private String couleur;
    private int version;
    private ViewType viewType ;

    public Bracelet(String id_qr, String model, String couleur, ViewType viewType) {
        this.id_qr = id_qr;
        this.model = model;
        this.couleur = couleur;
        this.viewType = viewType;
    }

    public Bracelet(String id_qr, String model, String couleur) {
        this.id_qr = id_qr;
        this.model = model;
        this.couleur = couleur;
    }

    public void setid_qr(String id_qr) {
        this.id_qr = id_qr;
    }

    public void setmodel(String model) {
        this.model = model;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    public String getid_qr() {
        return id_qr;
    }

    public String getmodel() {
        return model;
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
    public void setItemViewType(ViewType v) {
        this.viewType = v;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder) {
        switch (this.viewType){
            case v1: {
                WatchViewHolderFactory.WatchViewHolderV1 textViewHolder = (WatchViewHolderFactory.WatchViewHolderV1) viewHolder;
                textViewHolder.textView.setText(this.model);
                break;
            }

            case v2: {
                WatchViewHolderFactory.WatchViewHolderV2 textViewHolder = (WatchViewHolderFactory.WatchViewHolderV2) viewHolder;
                textViewHolder.textView.setText(this.model);
                break;
            }
        }

        //textViewHolder.textView1.setText(text);
    }
}
