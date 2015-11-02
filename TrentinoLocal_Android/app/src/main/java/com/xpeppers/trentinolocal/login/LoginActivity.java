package com.xpeppers.trentinolocal.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.rey.material.widget.ImageButton;
import com.xpeppers.servicelib.bean.Auth;
import com.xpeppers.servicelib.services.UsersService;
import com.xpeppers.servicelib.utils.CallBack;
import com.xpeppers.trentinolocal.BaseActivity;
import com.xpeppers.trentinolocal.Global;
import com.xpeppers.trentinolocal.R;

import java.util.Arrays;

/**
 * @author Emilio Frusciante - FEj (efrusciante AT wish-op DOT com)
 * @since 07/04/15
 */
public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initfacebookLogin();

        if(global.isFacebookLogin()) {
            loginUser();
        }

        ImageButton ibClose = (ImageButton) findViewById(R.id.ibClose);
        ibClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initfacebookLogin() {
        LoginButton loginButton = (LoginButton) findViewById(R.id.lbFacebook);
        loginButton.setReadPermissions(Arrays.asList("user_about_me", "email"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                loginUser();
            }

            @Override
            public void onCancel() {
                global.setApiAuth(null);
            }

            @Override
            public void onError(FacebookException exception) {
                global.setApiAuth(null);
            }
        });
    }

    private void loginUser() {
        String provider = Global.PROVIDER_FB;
        String token = global.getAccessToken().getToken();

        UsersService.getInstance(getApplicationContext()).login(provider, token, new CallBack() {
            @Override
            public void onSuccess(Object result) {
                Auth auth = (Auth) result;
                global.setApiAuth(auth.getToken());

                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFailure(Throwable caught) {
                createUser();
            }
        });
    }

    private void createUser() {
        getEmailFromFacebook(new CallBack<String>() {
            @Override
            public void onSuccess(String email) {
                UsersService.getInstance(getApplicationContext()).create(email, Global.PROVIDER_FB, global.getAccessToken().getToken(), new CallBack() {
                    @Override
                    public void onSuccess(Object result) {
                        loginUser();
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        global.setApiAuth(null);
                        Intent data = new Intent();
                        setResult(RESULT_CANCELED);
                        finish();
                    }
                });
            }

            @Override
            public void onFailure(Throwable caught) {
                Log.i(Global.LOG_TAG, "FACEBOOK: Login error " + caught.getCause().toString());
            }
        });
    }
}
