package com.xpeppers.servicelib.services;

import android.content.Context;

import com.xpeppers.servicelib.bean.Offer;
import com.xpeppers.servicelib.utils.CallBack;
import com.xpeppers.servicelib.ws.RestClient;

import java.util.List;

/**
 * @author Emilio Frusciante - FEj (efrusciante AT wish-op DOT com)
 * @since 07/04/15
 */
public class OffersService {
    private static OffersService instance;
    private Context context;

    public static OffersService getInstance(Context context) {
        if(instance == null)
            instance = new OffersService();

        instance.context = context;

        return instance;
    }

    public void getAll(final CallBack callBack) {
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    RestClient restClient = new RestClient(instance.context);
                    List<Offer> offers = restClient.getApiService().getOffers();
                    callBack.onSuccess(offers);

                } catch (Exception e) {
                    callBack.onFailure(e);
                }
            }
        };
        t.start();
    }

    public void get(final long id, final CallBack callBack) {
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    RestClient restClient = new RestClient(instance.context);
                    Offer offer = restClient.getApiService().getOffer(id);
                    callBack.onSuccess(offer);

                } catch (Exception e) {
                    callBack.onFailure(e);
                }
            }
        };
        t.start();
    }
}
