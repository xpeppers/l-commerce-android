package com.xpeppers.servicelib.bean;

import java.io.Serializable;

/**
 * @author Emilio Frusciante - FEj (efrusciante AT wish-op DOT com)
 * @since 2015/12/28
 */
public class Reseller implements Serializable {
    private String how_it_works;
    private String support;
    private String button_text;
    private String custom_url;

    public String getHow_it_works() {
        return how_it_works;
    }

    public void setHow_it_works(String how_it_works) {
        this.how_it_works = how_it_works;
    }

    public String getSupport() {
        return support;
    }

    public void setSupport(String support) {
        this.support = support;
    }

    public String getButton_text() {
        return button_text;
    }

    public void setButton_text(String button_text) {
        this.button_text = button_text;
    }

    public String getCustom_url() {
        return custom_url;
    }

    public void setCustom_url(String custom_url) {
        this.custom_url = custom_url;
    }
}
