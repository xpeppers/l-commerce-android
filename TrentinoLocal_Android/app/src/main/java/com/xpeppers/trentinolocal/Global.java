package com.xpeppers.trentinolocal;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import com.xpeppers.servicelib.bean.Offer;
import com.xpeppers.servicelib.bean.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emilio Frusciante - FEj (efrusciante AT wish-op DOT com)
 * @since 07/04/15
 */
public class Global extends Application {
    private List<Offer> offers = new ArrayList<>();
    //private List<Itinerary> itineraries = new ArrayList<>();
    //private List<Order> reservations = new ArrayList<>();

    private Offer selectedOffer = null;
    //private Itinerary selectedItinerary = null;
    private Order selectedOrder = null;

    private Activity currentActivity;

    private static String LOG_TAG = "TRENTINO_LOCAL";

    @Override
    public void onCreate() {
        super.onCreate();

        registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            // TODO inserire nella callback giusta eventuali funzionalità da eseguire ogni volta sul ciclo di vita di una activity, es. GoogleAnalytics

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                Log.d(LOG_TAG, "Tracking Activity Created: " + activity.getLocalClassName());
            }

            @Override
            public void onActivityStarted(Activity activity) {
                Log.d(LOG_TAG, "Tracking Activity Started: " + activity.getLocalClassName());
            }

            @Override
            public void onActivityResumed(Activity activity) {
                Log.d(LOG_TAG, "Tracking Activity Resumed: " + activity.getLocalClassName());
            }

            @Override
            public void onActivityPaused(Activity activity) {
                Log.d(LOG_TAG, "Tracking Activity Paused: " + activity.getLocalClassName());
            }

            @Override
            public void onActivityStopped(Activity activity) {
                Log.d(LOG_TAG, "Tracking Activity Stopped: " + activity.getLocalClassName());
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                Log.d(LOG_TAG, "Tracking Activity SaveInstanceState: " + activity.getLocalClassName());
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                Log.d(LOG_TAG, "Tracking Activity Destroyed: " + activity.getLocalClassName());
            }
        });
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(Activity currentActivity) {
        this.currentActivity = currentActivity;
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

    public Order getSelectedOrder() {
        return selectedOrder;
    }

    public void setSelectedOrder(Order selectedOrder) {
        this.selectedOrder = selectedOrder;
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
