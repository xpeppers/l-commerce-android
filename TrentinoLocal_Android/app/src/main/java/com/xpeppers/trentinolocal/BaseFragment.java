package com.xpeppers.trentinolocal;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;

/**
 * @author Emilio Frusciante - FEj (efrusciante AT wish-op DOT com)
 * @since 07/04/15
 */
public class BaseFragment extends Fragment {
    protected Global global;
    protected CallbackManager callbackManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        global = (Global) getActivity().getApplicationContext();

        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        global.setCurrentActivity(this.getActivity());
    }

    @Override
    public void onPause() {
        clearReferences();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        clearReferences();
        super.onDestroy();
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
        Activity activity = getActivity();
        if (activity == null) {
            return 0;
        }
        TypedValue typedValue = new TypedValue();
        int[] textSizeAttr = new int[]{R.attr.actionBarSize};
        int indexOfAttrTextSize = 0;
        TypedArray a = activity.obtainStyledAttributes(typedValue.data, textSizeAttr);
        int actionBarSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
        a.recycle();
        return actionBarSize;
    }

    protected int getScreenHeight() {
        Activity activity = getActivity();
        if (activity == null) {
            return 0;
        }
        return activity.findViewById(android.R.id.content).getHeight();
    }

    protected void asyncError(final String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                global.showErrorDialog("Error", message);
            }
        });
    }

    protected void asyncDialog(final String title, final String message) {
        asyncDialog(title, message, false);
    }

    protected void asyncDialog(final String title, final String message, final boolean finishActivity) {
        if(getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    global.showErrorDialog(title, message);
                }
            });
        }
    }
}
