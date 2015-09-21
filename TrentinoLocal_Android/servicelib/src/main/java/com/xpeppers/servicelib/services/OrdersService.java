package com.xpeppers.servicelib.services;

import android.content.Context;

import com.xpeppers.servicelib.bean.Order;
import com.xpeppers.servicelib.bean.OrderRequest;
import com.xpeppers.servicelib.bean.Payment;
import com.xpeppers.servicelib.bean.PaymentRequest;
import com.xpeppers.servicelib.utils.CallBack;
import com.xpeppers.servicelib.ws.RestClient;

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

    public void create(final double userId, final List<Double> offers, final CallBack callBack) {
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    RestClient restClient = new RestClient(instance.context);
                    OrderRequest orderRequest = new OrderRequest();
                    orderRequest.setUser_id(userId);
                    orderRequest.setOffer_ids(offers);
                    Order order = restClient.getApiService().createOrder(orderRequest);
                    callBack.onSuccess(order);

                } catch (Exception e) {
                    callBack.onFailure(e);
                }
            }
        };
        t.start();
    }

    public void get(final double id, final CallBack callBack) {
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    RestClient restClient = new RestClient(instance.context);
                    Order order = restClient.getApiService().getOrder(id);
                    callBack.onSuccess(order);

                } catch (Exception e) {
                    callBack.onFailure(e);
                }
            }
        };
        t.start();
    }

    public void pay(final double id, final String paypalPaymentToken, final CallBack callBack) {
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    RestClient restClient = new RestClient(instance.context);
                    PaymentRequest paymentRequest = new PaymentRequest();
                    paymentRequest.setPaypal_payment_token(paypalPaymentToken);
                    Payment payment = restClient.getApiService().pay(id, paymentRequest);
                    callBack.onSuccess(payment);

                } catch (Exception e) {
                    callBack.onFailure(e);
                }
            }
        };
        t.start();
    }
}
