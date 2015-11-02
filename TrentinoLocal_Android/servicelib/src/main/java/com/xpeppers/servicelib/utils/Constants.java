package com.xpeppers.servicelib.utils;

/**
 * @author Emilio Frusciante - FEj (efrusciante AT wish-op DOT com)
 * @since 07/04/15
 */
public class Constants {
    //Properties File
    public final static String SERVICES_PROPERTIES = "trentino.properties";
    public final static String PROPERTIES_DEBUG_BASE_URL = "debug_base_url";
    public final static String PROPERTIES_ALPHA_BASE_URL = "alpha_base_url";
    public final static String PROPERTIES_RELEASE_BASE_URL = "release_base_url";

    //REMOTE DOMAIN
    public final static String BASE_PATH = "http://private-b3ea5d-linkingcommerceapi.apiary-mock.com/api";

    // API PATH
    public final static String OFFERS = "/offers";
    public final static String OFFER =  "/offers/{id}";

    public final static String USERS = "/users";
    public final static String AUTH = "/auth";
    public final static String MERCHANT = "/auth/merchants";
    public final static String PROFILE = "/profile";

    public final static String ORDERS = "/orders";
    public final static String ORDER =  "/orders/{id}";

    public final static String PAYMENT = "/orders/{id}/payments";

    public final static String BOUGHT_BY_USERS = "/bought_offers";
    public final static String BOUGHT_BY_USER = "/bought_offers/{id}";

    public final static String SOLD_BY_MERCHANT = "/sold_offers";
}
