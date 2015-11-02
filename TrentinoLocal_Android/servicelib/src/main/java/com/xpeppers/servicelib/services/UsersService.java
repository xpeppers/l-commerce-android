package com.xpeppers.servicelib.services;

import android.content.Context;

import com.xpeppers.servicelib.bean.Auth;
import com.xpeppers.servicelib.bean.User;
import com.xpeppers.servicelib.bean.UserRequest;
import com.xpeppers.servicelib.utils.CallBack;
import com.xpeppers.servicelib.ws.RestClient;

/**
 * @author Emilio Frusciante - FEj (efrusciante AT wish-op DOT com)
 * @since 07/04/15
 */
public class UsersService {
    private static UsersService instance;
    private Context context;

    public static UsersService getInstance(Context context) {
        if(instance == null)
            instance = new UsersService();

        instance.context = context;

        return instance;
    }

    public void login(final String provider, final String providerToken, final CallBack callBack) {
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    RestClient restClient = new RestClient(instance.context);
                    UserRequest userRequest = new UserRequest();
                    userRequest.setProvider(provider);
                    userRequest.setProvider_token(providerToken);
                    Auth auth = restClient.getApiService().authUser(userRequest);
                    callBack.onSuccess(auth);

                } catch (Exception e) {
                    callBack.onFailure(e);
                }
            }
        };
        t.start();
    }

    public void create(final String email, final String provider, final String providerToken, final CallBack callBack) {
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    RestClient restClient = new RestClient(instance.context);
                    UserRequest userRequest = new UserRequest();
                    userRequest.setEmail(email);
                    userRequest.setProvider(provider);
                    userRequest.setProvider_token(providerToken);
                    User user = restClient.getApiService().createUser(userRequest);
                    callBack.onSuccess(user);

                } catch (Exception e) {
                    callBack.onFailure(e);
                }
            }
        };
        t.start();
    }

    public void getProfile(final String auth, final CallBack callBack) {
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    RestClient restClient = new RestClient(instance.context);
                    String token = "Token token=" + auth;
                    User user = restClient.getApiService().getProfile(token);
                    callBack.onSuccess(user);

                } catch (Exception e) {
                    callBack.onFailure(e);
                }
            }
        };
        t.start();
    }
}
