package com.xpeppers.servicelib.utils;

public class Constants {
    //Properties File
    public final static String SERVICES_PROPERTIES = "trentino.properties";
    public static final String BASE_URL = "base_url";

    //REMOTE DOMAIN
    public final static String DEFAULT_PATH = "http://private-b3ea5d-linkingcommerceapi.apiary-mock.com/api";

    // API PATH
    public final static String OFFERS = "/offers";
    public final static String OFFER =  "/offers/{id}";

    public final static String USERS = "/users";
    public final static String AUTH = "/auth";
    public final static String PROFILE = "/profile";

    public final static String ORDERS = "/orders";
    public final static String ORDER =  "/orders/{id}";

    public final static String PAYMENT = "/orders/{id}/payments";

    public final static String BOUGHT_BY_USERS = "/bought_offers";
    public final static String BOUGHT_BY_USER = "/bought_offers/{id}";

}
