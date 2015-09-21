package com.xpeppers.servicelib.bean;

import java.io.Serializable;

/**
 * @author Emilio Frusciante - FEj (efrusciante AT wish-op DOT com)
 * @since 2015/09/17
 */
public class Payment implements Serializable {
    private double id;
    private String paypal_payment_token;

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public String getPaypal_payment_token() {
        return paypal_payment_token;
    }

    public void setPaypal_payment_token(String paypal_payment_token) {
        this.paypal_payment_token = paypal_payment_token;
    }

    @Override
    public String toString() {
        String msg = "";

        msg += "id: " + getId() + "\n";
        msg += "paypal_payment_token: " + getPaypal_payment_token() + "\n";
        msg += "\n\n";

        return msg;
    }
}
