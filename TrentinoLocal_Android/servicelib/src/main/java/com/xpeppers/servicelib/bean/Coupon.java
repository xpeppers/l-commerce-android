package com.xpeppers.servicelib.bean;

import java.io.Serializable;

/**
 * @author Emilio Frusciante - FEj (efrusciante AT wish-op DOT com)
 * @since 2015/09/17
 */
public class Coupon implements Serializable {
    private long id;
    private String code;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        String msg = "";

        msg += "id: " + getId() + "\n";
        msg += "code: " + getCode() + "\n";
        msg += "\n\n";

        return msg;
    }
}
