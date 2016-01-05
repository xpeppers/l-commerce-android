package com.xpeppers.servicelib.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Emilio Frusciante - FEj (efrusciante AT wish-op DOT com)
 * @since 07/04/15
 */
public class OfferBought implements Serializable {
    private long id;
    private String title;
    private String description;
    private String image_url;
    private String status;

    private Coupon coupon;

    private String user_fullname;
    private Date purchase_date;

    private double price;
    private double reservation_price;

    private List<String>  image_gallery;
    private String merchant;
    private Address address;
    private String telephone;
    private String email;
    private String web_site;

    private String facebook;
    private String twitter;

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

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public String getUser_fullname() {
        return user_fullname;
    }

    public void setUser_fullname(String user_fullname) {
        this.user_fullname = user_fullname;
    }

    public Date getPurchase_date() {
        return purchase_date;
    }

    public void setPurchase_date(Date purchase_date) {
        this.purchase_date = purchase_date;
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

    public double getReservation_price() {
        return reservation_price;
    }

    public void setReservation_price(double reservation_price) {
        this.reservation_price = reservation_price;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    @Override
    public String toString() {
        String msg = "";

        msg += "id: " + getId() + "\n";
        msg += "title: " + getTitle() + "\n";
        msg += "description: " + getDescription() + "\n";
        msg += "image_url: " + getImage_url() + "\n";
        msg += "status: " + getStatus() + "\n";
        msg += "coupon: " + getCoupon().toString() + "\n";
        msg += "\n\n";

        return msg;
    }
}
