package com.xpeppers.servicelib.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author Emilio Frusciante - FEj (efrusciante AT wish-op DOT com)
 * @since 07/04/15
 */
public class Order implements Serializable {
    private double id;
    private double user_id;
    private List<Offer> offers;
    private String status;
    private Coupon coupon;

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public double getUser_id() {
        return user_id;
    }

    public void setUser_id(double user_id) {
        this.user_id = user_id;
    }

    public List<Offer> getOffers() {
        return offers;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    @Override
    public String toString() {
        String msg = "";

        msg += "id: " + getId() + "\n";
        msg += "user_id: " + getUser_id() + "\n";
        msg += "offers: " + getOffers().toString() + "\n";
        msg += "status: " + getStatus() + "\n";
        msg += "coupon: " + getCoupon() + "\n";
        msg += "\n\n";

        return msg;
    }
}
