package com.wildchild.locationpickermodule.locationpickermodule.DBSynchronisation.Models;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.annotations.SerializedName;
import com.wildchild.locationpickermodule.R;
import com.wildchild.locationpickermodule.locationpickermodule.ViewHolders.Interfaces.RowType;
import com.wildchild.locationpickermodule.locationpickermodule.ViewHolders.WatchViewHolderFactory;

import java.io.Serializable;

enum Versions {
    @SerializedName("1")
    v1,
    @SerializedName("2")
    v2,
    @SerializedName("3")
    v3
}

public class Bracelet implements RowType, Serializable {

    @SerializedName("_id")
    private String id_qr;
    private String model;
    private String couleur;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public Versions getVersion() {
        return version;
    }

    public void setVersion(Versions version) {
        this.version = version;
    }

    private Versions version;
    private ViewType viewType;

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

    @NonNull
    @Override
    public String toString() {
        return "Bracelet : ID " + getid_qr() + " Model : " + getmodel() + " VERSION : " + getVersion().name();
    }

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
                textViewHolder.textView.setText(this.model);
                switch (version) {
                    case v1: {
                        textViewHolder.imageView.setImageDrawable(textViewHolder.context.getDrawable(R.drawable.v1));
                        break;
                    }
                    case v2: {
                        textViewHolder.imageView.setImageDrawable(textViewHolder.context.getDrawable(R.drawable.v3));
                        break;
                    }
                }
                break;
            }

            case v2: {
                WatchViewHolderFactory.WatchViewHolderV2 textViewHolder = (WatchViewHolderFactory.WatchViewHolderV2) viewHolder;
                textViewHolder.textView.setText(this.model);

                switch (version) {
                    case v1: {
                        textViewHolder.imageView.setImageDrawable(textViewHolder.context.getDrawable(R.drawable.v1));
                        break;
                    }
                    case v2: {
                        textViewHolder.imageView.setImageDrawable(textViewHolder.context.getDrawable(R.drawable.v3));
                        break;
                    }
                }

                break;
            }
        }

        //textViewHolder.textView1.setText(text);
    }
}
