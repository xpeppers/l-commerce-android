package com.xpeppers.servicelib.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author Emilio Frusciante - FEj (efrusciante AT wish-op DOT com)
 * @since 07/04/15
 */
public class Offer implements Serializable {
    private long id;
    private String title;
    private String description;
    private double original_price;
    private double price;
    private double reservation_price;
    private String image_url;
    private double latitude;
    private double longitude;

    private List<String> image_gallery;

    private String merchant;
    private Address address;
    private String telephone;
    private String email;
    private String web_site;

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public double getOriginal_price() {
        return original_price;
    }

    public void setOriginal_price(double original_price) {
        this.original_price = original_price;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getReservation_price() {
        return reservation_price;
    }

    public void setReservation_price(double reservation_price) {
        this.reservation_price = reservation_price;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public List<String> getImage_gallery() {
        return image_gallery;
    }

    public void setImage_gallery(List<String> image_gallery) {
        this.image_gallery = image_gallery;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWeb_site() {
        return web_site;
    }

    public void setWeb_site(String web_site) {
        this.web_site = web_site;
    }

    @Override
    public String toString() {
        String msg = "";

        msg += "id: " + getId() + "\n";
        msg += "title: " + getTitle() + "\n";
        msg += "description: " + getDescription() + "\n";
        msg += "original_price: " + getOriginal_price() + "\n";
        msg += "price: " + getPrice() + "\n";
        msg += "reservation_price: " + getReservation_price() + "\n";
        msg += "image_url: " + getImage_url() + "\n";
        msg += "latitude: " + getLatitude() + "\n";
        msg += "longitude: " + getLongitude() + "\n";
        msg += "merchant: " + getMerchant() + "\n";
        msg += "address: " + getAddress().toString() + "\n";
        msg += "telephone: " + getTelephone() + "\n";
        msg += "email: " + getEmail() + "\n";
        msg += "web_site: " + getWeb_site() + "\n";
        msg += "\n\n";

        return msg;
    }
}
