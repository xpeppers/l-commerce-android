package com.xpeppers.trentinolocal.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.xpeppers.trentinolocal.R;
import com.xpeppers.trentinolocal.details.OfferDetailActivity;
import com.xpeppers.trentinolocal.main.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by linnal on 3/29/16.
 */
public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        Log.i(TAG, "From: " + from);

        if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else {
            // normal downstream message.
        }

        try {
            JSONObject js = new JSONObject(message);
            if(js.has("generic") && js.has("title")){
                sendNotification(pendingIntentForMainActivity(), js.getString("title"));
            }else if(js.has("id") && js.has("title")){
                sendNotification(pendingIntentForDetail(js.getInt("id")), js.getString("title"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private PendingIntent pendingIntentForDetail(int offerId){
        Intent intent = new Intent(this, OfferDetailActivity.class);
        intent.putExtra(OfferDetailActivity.EXTRA_OFFER_ID, offerId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        return pendingIntent;
    }


    private PendingIntent pendingIntentForMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        return pendingIntent;
    }

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(PendingIntent pendingIntent, String message) {


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.tdv_large_icon)
                .setContentTitle("GCM Message")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
