package com.xpeppers.trentinolocal;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.xpeppers.servicelib.bean.Auth;
import com.xpeppers.servicelib.services.UsersService;
import com.xpeppers.servicelib.utils.CallBack;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Emilio Frusciante - FEj (efrusciante AT wish-op DOT com)
 * @since 07/04/15
 */
public class BaseActivity extends AppCompatActivity {
    protected Global global;
    protected CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        global = (Global) getApplicationContext();

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
    }

    @Override
    protected void onResume() {
        super.onResume();
        global.setCurrentActivity(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        clearReferences();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearReferences();
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void clearReferences(){
        Activity currActivity = global.getCurrentActivity();
        if (currActivity != null && currActivity.equals(this))
            global.setCurrentActivity(null);
    }

    protected int getActionBarSize() {
        TypedValue typedValue = new TypedValue();
        int[] textSizeAttr = new int[]{R.attr.actionBarSize};
        int indexOfAttrTextSize = 0;
        TypedArray a = obtainStyledAttributes(typedValue.data, textSizeAttr);
        int actionBarSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
        a.recycle();
        return actionBarSize;
    }

    protected int getScreenHeight() {
        return findViewById(android.R.id.content).getHeight();
    }

    protected void asyncDialog(final String title, final String message) {
        asyncDialog(title, message, false);
    }

    protected void asyncDialog(final String title, final String message, final boolean finishActivity) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                global.showErrorDialog(title, message);
            }
        });
    }


    protected void apiLogin() {
        String token = global.getAccessToken().getToken();

        UsersService.getInstance(getApplicationContext()).login(Global.PROVIDER_FB, token, new CallBack() {
            @Override
            public void onSuccess(Object result) {
                Auth auth = (Auth) result;
                global.setApiAuth(auth.getToken());
            }

            @Override
            public void onFailure(Throwable caught) {

            }
        });
    }

    protected void getEmailFromFacebook() {
        getEmailFromFacebook(null);
    }

    protected void getEmailFromFacebook(final CallBack<String> callBack) {
        GraphRequest request = GraphRequest.newMeRequest(global.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String email = object.getString("email");
                    global.setUserEmailByFacebook(email);
                    if(callBack != null)
                        callBack.onSuccess(email);
                } catch (JSONException e) {
                    if(callBack != null)
                        callBack.onFailure(e);
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "email");
        request.setParameters(parameters);
        request.executeAsync();
    }
}
