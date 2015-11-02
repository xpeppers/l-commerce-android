package com.xpeppers.servicelib.services;

import android.content.Context;

import com.xpeppers.servicelib.bean.OfferBought;
import com.xpeppers.servicelib.utils.CallBack;
import com.xpeppers.servicelib.ws.RestClient;

import java.util.List;

/**
 * @author Emilio Frusciante - FEj (efrusciante AT wish-op DOT com)
 * @since 07/04/15
 */
public class OffersBoughtService {
    private static OffersBoughtService instance;
    private Context context;

    public static OffersBoughtService getInstance(Context context) {
        if(instance == null)
            instance = new OffersBoughtService();

        instance.context = context;

        return instance;
    }

    public void getAll(final String auth, final CallBack callBack) {
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    RestClient restClient = new RestClient(instance.context);

                    String token = "Token token=" + auth;
                    List<OfferBought> offerBoughts = restClient.getApiService().getOffersBoughtByUsers(token);
                    callBack.onSuccess(offerBoughts);

                } catch (Exception e) {
                    callBack.onFailure(e);
                }
            }
        };
        t.start();
    }

    public void get(final String auth, final long id, final CallBack callBack) {
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    RestClient restClient = new RestClient(instance.context);
                    String token = "Token token=" + auth;
                    OfferBought offerBought = restClient.getApiService().getOfferBoughtByUser(token, id);
                    callBack.onSuccess(offerBought);

                } catch (Exception e) {
                    callBack.onFailure(e);
                }
            }
        };
        t.start();
    }
}
