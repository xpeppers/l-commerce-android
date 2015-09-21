package com.xpeppers.servicelib.bean;

import java.io.Serializable;

/**
 * @author Emilio Frusciante - FEj (efrusciante AT wish-op DOT com)
 * @since 2015/09/17
 */
public class PaymentRequest implements Serializable{
    private String paypal_payment_token;

    public String getPaypal_payment_token() {
        return paypal_payment_token;
    }

    public void setPaypal_payment_token(String paypal_payment_token) {
        this.paypal_payment_token = paypal_payment_token;
    }
}
