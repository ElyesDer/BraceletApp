package com.wildchild.locationpickermodule.locationpickermodule.DBSynchronisation.Models;

import com.google.gson.annotations.SerializedName;

public class History {

    @SerializedName("_id")
    private String id;
    private String bracelet;
    private String createdAt;
    private String latitude;
    private String longitude;
    private String place;
    private String updatedAt;
    private String user;

    public History(String id, String createdAt, String latitude, String longitude) {
        this.id = id;
        this.createdAt = createdAt;
        this.latitude = latitude;
        this.longitude = longitude;
        this.place = place;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setBracelet(String bracelet) {
        this.bracelet = bracelet;
    }

    public String getBracelet() {
        return this.bracelet;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLatitude() {
        return this.latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLongitude() {
        return this.longitude;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPlace() {
        return this.place;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return this.user;
    }

}
