package com.xpeppers.servicelib.ws;

import com.xpeppers.servicelib.bean.Auth;
import com.xpeppers.servicelib.bean.Offer;
import com.xpeppers.servicelib.bean.OfferBought;
import com.xpeppers.servicelib.bean.Order;
import com.xpeppers.servicelib.bean.OrderRequest;
import com.xpeppers.servicelib.bean.Payment;
import com.xpeppers.servicelib.bean.PaymentRequest;
import com.xpeppers.servicelib.bean.Reseller;
import com.xpeppers.servicelib.bean.User;
import com.xpeppers.servicelib.bean.UserRequest;
import com.xpeppers.servicelib.utils.Constants;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * @author Emilio Frusciante - FEj (efrusciante AT wish-op DOT com)
 * @since 07/04/15
 */
public interface ApiService {
    @GET(Constants.OFFERS)
    public List<Offer> getOffers();

    @GET(Constants.OFFER)
    public Offer getOffer(@Path("id") long id);

    @POST(Constants.USERS)
    public User createUser(@Body UserRequest userRequest);

    @POST(Constants.AUTH)
    public Auth authUser(@Body UserRequest userRequest);

    @GET(Constants.PROFILE)
    public User getProfile(@Header("Authorization") String auth);

    @POST(Constants.ORDERS)
    public Order createOrder(@Header("Authorization") String auth, @Body OrderRequest orderRequest);

    @GET(Constants.ORDER)
    public Order getOrder(@Header("Authorization") String auth, @Path("id") long id);

    @POST(Constants.PAYMENT)
    public Payment pay(@Header("Authorization") String auth, @Path("id") long id, @Body PaymentRequest paymentRequest);

    @GET(Constants.BOUGHT_BY_USERS)
    public List<OfferBought> getOffersBoughtByUsers(@Header("Authorization") String auth);

    @GET(Constants.BOUGHT_BY_USER)
    public OfferBought getOfferBoughtByUser(@Header("Authorization") String auth, @Path("id") long id);

    @GET(Constants.RESELLER)
    public Reseller getReseller();
}
