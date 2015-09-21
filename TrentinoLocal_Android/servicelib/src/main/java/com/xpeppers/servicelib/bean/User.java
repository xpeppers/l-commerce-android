package com.xpeppers.servicelib.bean;

import java.io.Serializable;

/**
 * @author Emilio Frusciante - FEj (efrusciante AT wish-op DOT com)
 * @since 2015/08/31
 */
public class User implements Serializable {
    private double id;
    private String email;

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        String msg = "";

        msg += "id: " + getId() + "\n";
        msg += "email: " + getEmail() + "\n";
        msg += "\n\n";

        return msg;
    }
}
