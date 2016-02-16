package com.xpeppers.trentinolocal.utils;

import com.google.android.gms.analytics.HitBuilders;
import com.xpeppers.trentinolocal.Global;

public class Tracker {
    public static void sendScreenView(String screenName) {
        Global.tracker.setScreenName(screenName);
        Global.tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
