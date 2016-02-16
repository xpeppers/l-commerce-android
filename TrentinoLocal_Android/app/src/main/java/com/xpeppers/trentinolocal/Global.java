package com.xpeppers.trentinolocal;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.crashlytics.android.Crashlytics;
import com.facebook.AccessToken;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.xpeppers.servicelib.bean.Offer;
import com.xpeppers.servicelib.bean.OfferBought;
import com.xpeppers.servicelib.bean.Order;
import com.xpeppers.servicelib.bean.Reseller;
import com.xpeppers.servicelib.bean.User;

import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Emilio Frusciante - FEj (efrusciante AT wish-op DOT com)
 * @since 07/04/15
 */
public class Global extends Application {
    public static final int REQUEST_CODE_PAYPAL = 799;
    public static final int REQUEST_CODE_FACEBOOK_LOGIN = 798;

    public static final String PROVIDER_FB = "facebook";
    public static String LOG_TAG = "TRENTINO_LOCAL";

    public static GoogleAnalytics analytics;
    public static Tracker tracker;

    private Activity currentActivity;
    private boolean apiAuthenticated;
    private String apiToken;

    private List<Offer> offers = new ArrayList<>();
    private Offer selectedOffer = null;

    private List<Order> orders = new ArrayList<>();
    private Order selectedOrder = null;

    private List<OfferBought> offerBoughts = new ArrayList<>();
    private OfferBought selectedOfferBought = null;

    private User user;
    private String userEmailByFacebook;

    private Reseller reseller;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        analytics = GoogleAnalytics.getInstance(this);
        analytics.setLocalDispatchPeriod(1800);

        tracker = analytics.newTracker(getResources().getString(R.string.google_analytics_tracking_id));
        tracker.enableExceptionReporting(true);
        tracker.enableAdvertisingIdCollection(true);

        registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                //Log.d(LOG_TAG, "Tracking Activity Created: " + activity.getLocalClassName());
            }

            @Override
            public void onActivityStarted(Activity activity) {
                //Log.d(LOG_TAG, "Tracking Activity Started: " + activity.getLocalClassName());
            }

            @Override
            public void onActivityResumed(Activity activity) {
                //Log.d(LOG_TAG, "Tracking Activity Resumed: " + activity.getLocalClassName());
            }

            @Override
            public void onActivityPaused(Activity activity) {
                //Log.d(LOG_TAG, "Tracking Activity Paused: " + activity.getLocalClassName());
            }

            @Override
            public void onActivityStopped(Activity activity) {
                //Log.d(LOG_TAG, "Tracking Activity Stopped: " + activity.getLocalClassName());
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                //Log.d(LOG_TAG, "Tracking Activity SaveInstanceState: " + activity.getLocalClassName());
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                //Log.d(LOG_TAG, "Tracking Activity Destroyed: " + activity.getLocalClassName());
            }
        });
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }

    public boolean isApiAuthenticated() {
        return apiAuthenticated;
    }

    public void setApiAuthenticated(boolean apiAuthenticated) {
        this.apiAuthenticated = apiAuthenticated;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public boolean isFacebookLogin() {
        if (getAccessToken() == null) {
            return false;
        }
        return !getAccessToken().isExpired();
    }

    public List<Offer> getOffers() {
        return offers;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }

    public Offer getSelectedOffer() {
        return selectedOffer;
    }

    public void setSelectedOffer(Offer selectedOffer) {
        this.selectedOffer = selectedOffer;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public Order getSelectedOrder() {
        return selectedOrder;
    }

    public void setSelectedOrder(Order selectedOrder) {
        this.selectedOrder = selectedOrder;
    }

    public List<OfferBought> getOfferBoughts() {
        return offerBoughts;
    }

    public void setOfferBoughts(List<OfferBought> offerBoughts) {
        this.offerBoughts = offerBoughts;
    }

    public OfferBought getSelectedOfferBought() {
        return selectedOfferBought;
    }

    public void setSelectedOfferBought(OfferBought selectedOfferBought) {
        this.selectedOfferBought = selectedOfferBought;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUserEmailByFacebook() {
        return userEmailByFacebook;
    }

    public void setUserEmailByFacebook(String userEmailByFacebook) {
        this.userEmailByFacebook = userEmailByFacebook;
    }

    public Reseller getReseller() {
        return reseller;
    }

    public void setReseller(Reseller reseller) {
        this.reseller = reseller;
    }

    public AccessToken getAccessToken() {
        return AccessToken.getCurrentAccessToken();
    }

    public void setApiAuth(String token) {
        if(token == null || token.equals("")) {
            setApiAuthenticated(false);
            setApiToken(null);
        } else {
            setApiAuthenticated(true);
            setApiToken(token);
        }
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public int pxToDp(int px) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public void showErrorDialog(final String title, final String message) {
        AlertDialog aDialog = new AlertDialog.Builder(getCurrentActivity()).setMessage(message).setTitle(title)
                .setNeutralButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int which) {
                        //if (!title.equalsIgnoreCase("About") && !title.equalsIgnoreCase("Directory Error") && !title.equalsIgnoreCase("View")) {
                        //    getCurrentActivity().finish();
                        //}
                    }
                }).create();
        aDialog.show();
    }

    public void showConfirmDialog(final String title, final String message) {
        showConfirmDialog(title, message, false);
    }

    public void showConfirmDialog(final String title, final String message, final boolean finishActivity) {
        AlertDialog aDialog = new AlertDialog.Builder(getCurrentActivity()).setMessage(message).setTitle(title)
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int which) {
                        if (finishActivity) {
                            getCurrentActivity().finish();
                        }
                    }
                }).create();

        aDialog.show();
    }
}
