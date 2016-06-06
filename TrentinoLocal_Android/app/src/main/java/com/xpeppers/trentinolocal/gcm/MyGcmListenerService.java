package com.xpeppers.trentinolocal.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

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

        try {
            JSONObject js = new JSONObject(message);
            if(js.has("generic") && js.has("title")){
                sendNotification(pendingIntentForMainActivity(), js.getString("title"));
            }else if(js.has("id") && js.has("title")){
                sendNotification(pendingIntentForDetail(js.getLong("id")), js.getString("title"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private PendingIntent pendingIntentForDetail(long offerId){
        Intent intent = new Intent(this, OfferDetailActivity.class);
        intent.putExtra(OfferDetailActivity.EXTRA_OFFER_ID, offerId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        return pendingIntent;
    }


    private PendingIntent pendingIntentForMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.EXTRA_ACTION, "refresh");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        return pendingIntent;
    }

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(PendingIntent pendingIntent, String message) {

        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(useWhiteIcon ? R.drawable.tdv_large_icon_silhouete : R.drawable.tdv_large_icon)
                .setContentTitle(getResources().getString(R.string.notification_title))
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        if(useWhiteIcon) {
            notificationBuilder.setColor(getResources().getColor(R.color.trentino_background));
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int n = (int) System.currentTimeMillis();
        notificationManager.notify(n , notificationBuilder.build());
    }

}
