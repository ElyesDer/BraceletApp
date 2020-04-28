package com.wildchild.locationpickermodule.locationpickermodule.DBSynchronisation.Models;


import com.google.gson.annotations.SerializedName;

enum Versions {
    @SerializedName("1")
    v1,
    @SerializedName("2")
    v2,
    @SerializedName("3")
    v3
}

public class BModel {
    private String name;
    private String couleur;
    private String url;
    private Versions version;

    public BModel(String name, String couleur, String url, Versions version) {
        this.name = name;
        this.couleur = couleur;
        this.url = url;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCouleur() {
        return couleur;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

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
}
