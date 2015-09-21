package com.xpeppers.servicelib.ws;

import com.xpeppers.servicelib.bean.Auth;
import com.xpeppers.servicelib.bean.Offer;
import com.xpeppers.servicelib.bean.Order;
import com.xpeppers.servicelib.bean.OrderRequest;
import com.xpeppers.servicelib.bean.Payment;
import com.xpeppers.servicelib.bean.PaymentRequest;
import com.xpeppers.servicelib.bean.User;
import com.xpeppers.servicelib.bean.UserRequest;
import com.xpeppers.servicelib.utils.Constants;

import java.util.List;

import retrofit.RetrofitError;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * @author Emilio Frusciante - FEj (efrusciante AT wish-op DOT com)
 * @since 07/04/15
 */
public interface ApiService {
    @GET(Constants.OFFERS)
    public List<Offer> getOffers() throws RetrofitError;

    @GET(Constants.OFFER)
    public Offer getOffer(@Path("id") double id) throws RetrofitError;

    @POST(Constants.USERS)
    public User createUser(@Body UserRequest userRequest) throws RetrofitError;

    @POST(Constants.AUTH)
    public Auth authUser(@Body UserRequest userRequest) throws RetrofitError;

    @POST(Constants.ORDERS)
    public Order createOrder(@Body OrderRequest orderRequest) throws RetrofitError;

    @GET(Constants.ORDER)
    public Order getOrder(@Path("id") double id) throws RetrofitError;

    @POST(Constants.PAYMENT)
    public Payment pay(@Path("id") double id, @Body PaymentRequest paymentRequest) throws RetrofitError;
}
