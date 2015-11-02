package com.xpeppers.servicelib.bean;

import java.io.Serializable;

/**
 * @author Emilio Frusciante - FEj (efrusciante AT wish-op DOT com)
 * @since 2015/08/31
 */
public class User implements Serializable {
    private long id;
    private String email;
    private String full_name;
    private String picture_url;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getPicture_url() {
        return picture_url;
    }

    public void setPicture_url(String picture_url) {
        this.picture_url = picture_url;
    }

    @Override
    public String toString() {
        String msg = "";

        msg += "id: " + getId() + "\n";
        msg += "email: " + getEmail() + "\n";
        msg += "full_name: " + getFull_name() + "\n";
        msg += "picture_url: " + getPicture_url() + "\n";
        msg += "\n\n";

        return msg;
    }
}
