package com.xpeppers.servicelib.bean;

import java.io.Serializable;

/**
 * @author Emilio Frusciante - FEj (efrusciante AT wish-op DOT com)
 * @since 2015/08/31
 */
public class UserRequest implements Serializable {
    private String email;
    private String provider;
    private String provider_token;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getProvider_token() {
        return provider_token;
    }

    public void setProvider_token(String provider_token) {
        this.provider_token = provider_token;
    }
}
