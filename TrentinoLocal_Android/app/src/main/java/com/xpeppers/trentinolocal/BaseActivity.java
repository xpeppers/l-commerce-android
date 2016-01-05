package com.xpeppers.trentinolocal;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.maps.model.LatLng;
import com.mikepenz.iconics.context.IconicsLayoutInflater;
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
        LayoutInflaterCompat.setFactory(getLayoutInflater(), new IconicsLayoutInflater(getDelegate()));
        FacebookSdk.sdkInitialize(getApplicationContext());

        global = (Global) getApplicationContext();
        callbackManager = CallbackManager.Factory.create();

        super.onCreate(savedInstanceState);
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

    protected void callNumber(String number) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + number));
        startActivity(callIntent);
    }

    protected void openWeb(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    protected void sendEmail(String emailTo) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { emailTo });
        intent.putExtra(Intent.EXTRA_SUBJECT, "Richiesta d'informazioni");
        //intent.putExtra(Intent.EXTRA_TEXT, "mail body");
        startActivity(Intent.createChooser(intent, ""));
    }

    protected void openNavigator(String address, LatLng position) {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?daddr="+ position.latitude +"," + position.longitude));
        startActivity(intent);

        /*
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" +position.latitude+","+position.longitude));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        */
    }
}
