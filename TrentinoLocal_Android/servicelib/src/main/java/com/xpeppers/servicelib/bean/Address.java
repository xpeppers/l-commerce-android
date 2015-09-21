package com.xpeppers.servicelib.bean;

import java.io.Serializable;

/**
 * @author Emilio Frusciante - FEj (efrusciante AT wish-op DOT com)
 * @since 2015/09/17
 */
public class Address implements Serializable {
    private String street;
    private String zip_code;
    private String city;
    private double latitude;
    private double longitude;

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZip_code() {
        return zip_code;
    }

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public void setLatitude(String latitude) {
        this.latitude = Double.valueOf(latitude);
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = Double.valueOf(longitude);
    }

    @Override
    public String toString() {
        String msg = "";

        msg += "street: " + getStreet() + "\n";
        msg += "zip_code: " + getZip_code() + "\n";
        msg += "city: " + getCity() + "\n";
        msg += "latitude: " + getLatitude() + "\n";
        msg += "longitude: " + getLongitude() + "\n";
        msg += "\n\n";

        return msg;
    }

}
