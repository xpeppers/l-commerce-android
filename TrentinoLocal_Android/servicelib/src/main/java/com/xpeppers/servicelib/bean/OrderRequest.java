package com.xpeppers.servicelib.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author Emilio Frusciante - FEj (efrusciante AT wish-op DOT com)
 * @since 2015/09/17
 */
public class OrderRequest implements Serializable {
    private List<Long> offer_ids;

    public List<Long> getOffer_ids() {
        return offer_ids;
    }

    public void setOffer_ids(List<Long> offer_ids) {
        this.offer_ids = offer_ids;
    }
}
