package com.xpeppers.servicelib.services;

import android.content.Context;

import com.xpeppers.servicelib.bean.Order;
import com.xpeppers.servicelib.bean.OrderRequest;
import com.xpeppers.servicelib.bean.Payment;
import com.xpeppers.servicelib.bean.PaymentRequest;
import com.xpeppers.servicelib.utils.CallBack;
import com.xpeppers.servicelib.ws.RestClient;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emilio Frusciante - FEj (efrusciante AT wish-op DOT com)
 * @since 07/04/15
 */
public class OrdersService {
    private static OrdersService instance;
    private Context context;

    public static OrdersService getInstance(Context context) {
        if(instance == null)
            instance = new OrdersService();

        instance.context = context;

        return instance;
    }

    public void create(final String auth, final Long offer, final CallBack callBack) {
        List<Long> offers = new ArrayList<>();
        offers.add(offer);

        create(auth, offers, callBack);
    }

    public void create(final String auth, final List<Long> offers, final CallBack callBack) {
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    RestClient restClient = new RestClient(instance.context);
                    OrderRequest orderRequest = new OrderRequest();
                    orderRequest.setOffer_ids(offers);
                    String token = "Token token=" + auth;
                    Order order = restClient.getApiService().createOrder(token, orderRequest);
                    callBack.onSuccess(order);

                } catch (Exception e) {
                    callBack.onFailure(e);
                }
            }
        };
        t.start();
    }

    public void get(final String auth, final long id,  final CallBack callBack) {
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    RestClient restClient = new RestClient(instance.context);
                    String token = "Token token=" + auth;
                    Order order = restClient.getApiService().getOrder(token, id);
                    callBack.onSuccess(order);

                } catch (Exception e) {
                    callBack.onFailure(e);
                }
            }
        };
        t.start();
    }

    public void confirmPayment(final String auth, final long id, final String paypalPaymentToken, final CallBack callBack) {
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    RestClient restClient = new RestClient(instance.context);
                    PaymentRequest paymentRequest = new PaymentRequest();
                    paymentRequest.setPaypal_payment_token(paypalPaymentToken);
                    String token = "Token token=" + auth;
                    Payment payment = restClient.getApiService().pay(token, id, paymentRequest);
                    callBack.onSuccess(payment);

                } catch (Exception e) {
                    callBack.onFailure(e);
                }
            }
        };
        t.start();
    }
}
