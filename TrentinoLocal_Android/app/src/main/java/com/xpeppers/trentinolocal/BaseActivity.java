package com.xpeppers.trentinolocal;

import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;

/**
 * @author Emilio Frusciante - FEj (efrusciante AT wish-op DOT com)
 * @since 07/04/15
 */
public class BaseActivity extends AppCompatActivity {
    protected Global global;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        global = (Global) getApplicationContext();
    }

    @Override
    protected void onResume() {
        super.onResume();
        global.setCurrentActivity(this);
    }

    @Override
    protected void onPause() {
        clearReferences();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        clearReferences();
        super.onDestroy();
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
}
