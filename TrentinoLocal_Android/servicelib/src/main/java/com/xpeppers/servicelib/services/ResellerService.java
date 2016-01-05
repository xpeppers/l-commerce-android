package com.xpeppers.servicelib.services;

import android.content.Context;

import com.xpeppers.servicelib.bean.Reseller;
import com.xpeppers.servicelib.utils.CallBack;
import com.xpeppers.servicelib.ws.RestClient;

/**
 * @author Emilio Frusciante - FEj (efrusciante AT wish-op DOT com)
 * @since 2015/12/28
 */
public class ResellerService {
    private static ResellerService instance;
    private Context context;

    public static ResellerService getInstance(Context context) {
        if(instance == null)
            instance = new ResellerService();

        instance.context = context;

        return instance;
    }

    public void getReseller(final CallBack callBack) {
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    RestClient restClient = new RestClient(instance.context);
                    Reseller reseller = restClient.getApiService().getReseller();
                    callBack.onSuccess(reseller);

                } catch (Exception e) {
                    callBack.onFailure(e);
                }
            }
        };
        t.start();
    }
}
