package com.wildchild.locationpickermodule.locationpickermodule.DBSynchronisation.Models;

import com.google.gson.annotations.SerializedName;

import org.json.*;


public class User {

    @SerializedName("_id")
    private String id;
    private String createdAt;
    private String email;
    private String firstname;
    private String lastname;
    private String password;
    private boolean type;
    private boolean verified;

    public static User currentUser;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getLastname() {
        return this.lastname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public boolean isType() {
        return this.type;
    }


    /**
     * Instantiate the instance using the passed jsonBracelet to set the properties values
     */
    public User(JSONObject jsonBracelet) {
        if (jsonBracelet == null) {
            return;
        }
        id = jsonBracelet.optString("_id");
        //bracelets = jsonBracelet.optJSONObject()[]("bracelets");
        createdAt = jsonBracelet.optString("createdAt");
        email = jsonBracelet.optString("email");
        firstname = jsonBracelet.optString("firstname");
        lastname = jsonBracelet.optString("lastname");
        password = jsonBracelet.optString("password");
        type = jsonBracelet.optBoolean("type");
    }

    /**
     * Returns all the available property values in the form of JSONBracelet instance where the key is the approperiate json key and the value is the value of the corresponding field
     */
    public JSONObject toJsonObject() {
        JSONObject jsonBracelet = new JSONObject();
        try {
            jsonBracelet.put("_id", id);
            jsonBracelet.put("createdAt", createdAt);
            jsonBracelet.put("email", email);
            jsonBracelet.put("firstname", firstname);
            jsonBracelet.put("lastname", lastname);
            jsonBracelet.put("password", password);
            jsonBracelet.put("type", type);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jsonBracelet;
    }

}