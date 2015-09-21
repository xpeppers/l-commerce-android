package com.xpeppers.servicelib.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author Emilio Frusciante - FEj (efrusciante AT wish-op DOT com)
 * @since 07/14/15
 */
public class Itinerary implements Serializable {
    private int id;
    private String title;
    private String description;
    private String image;
    private List<Offer> offers;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Offer> getOffers() {
        return offers;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }

    @Override
    public String toString() {
        String msg = "";

        msg += "id: " + getId() + "\n";
        msg += "title: " + getTitle() + "\n";
        msg += "description: " + getDescription() + "\n";
        msg += "image: " + getImage() + "\n";
        msg += "offers: \n";
        for(int i=0; i < getOffers().size(); i++) {
            msg += getOffers().get(i).toString();
        }
        msg += "\n\n";

        return msg;
    }
}
