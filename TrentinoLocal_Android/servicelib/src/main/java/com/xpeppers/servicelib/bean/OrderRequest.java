package com.xpeppers.servicelib.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author Emilio Frusciante - FEj (efrusciante AT wish-op DOT com)
 * @since 2015/09/17
 */
public class OrderRequest implements Serializable {
    private double user_id;
    private List<Double> offer_ids;

    public double getUser_id() {
        return user_id;
    }

    public void setUser_id(double user_id) {
        this.user_id = user_id;
    }

    public List<Double> getOffer_ids() {
        return offer_ids;
    }

    public void setOffer_ids(List<Double> offer_ids) {
        this.offer_ids = offer_ids;
    }
}
