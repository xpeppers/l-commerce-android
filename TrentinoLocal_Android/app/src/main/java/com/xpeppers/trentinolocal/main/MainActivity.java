package com.xpeppers.trentinolocal.main;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.appevents.AppEventsLogger;
import com.github.ksoichiro.android.observablescrollview.CacheFragmentStatePagerAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.rey.material.widget.Button;
import com.xpeppers.servicelib.bean.Auth;
import com.xpeppers.servicelib.bean.Reseller;
import com.xpeppers.servicelib.services.ResellerService;
import com.xpeppers.servicelib.services.UsersService;
import com.xpeppers.servicelib.utils.CallBack;
import com.xpeppers.trentinolocal.BaseActivity;
import com.xpeppers.trentinolocal.Global;
import com.xpeppers.trentinolocal.gcm.QuickstartPreferences;
import com.xpeppers.trentinolocal.R;
import com.xpeppers.trentinolocal.gcm.RegistrationIntentService;
import com.xpeppers.trentinolocal.login.LoginActivity;

/**
 * @author Emilio Frusciante - FEj (efrusciante AT wish-op DOT com)
 * @since 07/04/15
 */
public class MainActivity extends BaseActivity {
    public static String EXTRA_START_PAGE = "start_page";
    private int NUMBER_PAGES = 3;
    private int START_PAGE_NUMBER = 0;

    private TextView tvToolbarTitle = null;

    private ViewPager vpContent;
    private ContentPagerAdapter contentPagerAdapter;

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mToolbarView = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbarView);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        tvToolbarTitle = (TextView) findViewById(R.id.tvToolbarTitle);

        vpContent = (ViewPager) findViewById(R.id.vpContent);

        contentPagerAdapter = new ContentPagerAdapter(getSupportFragmentManager());
        contentPagerAdapter.setPageNumber(NUMBER_PAGES);

        vpContent.setAdapter(contentPagerAdapter);

        vpContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if((position == 1
                        || (position == 2
                            && (global.getReseller() == null || global.getReseller().getCustom_url() == null
                            || global.getReseller().getCustom_url().equals(""))) || position == 3)
                        && !(global.isApiAuthenticated() && global.isFacebookLogin())) {
                    secureButtonMenu(position);
                    vpContent.setCurrentItem(0);
                    //Log.d(Global.LOG_TAG, "OnPageSelected position: " + 0);
                } else {
                    //Log.d(Global.LOG_TAG, "OnPageSelected position: " + position);
                    selectButtonMenu(position);
                    selectHeaderTitle(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        configureButtonMenu();

        // Init First Page
        Intent intent = getIntent();
        int start_page = intent.getIntExtra(EXTRA_START_PAGE, START_PAGE_NUMBER);
        vpContent.setCurrentItem(start_page);
        selectButtonMenu(start_page);
        selectHeaderTitle(start_page);

        if(global.isFacebookLogin() && !global.isApiAuthenticated()) {
            apiLogin();
            getEmailFromFacebook();
        }

        if(global.getReseller() == null) {
            ResellerService.getInstance(global.getApplicationContext()).getReseller(new CallBack() {
                @Override
                public void onSuccess(Object result) {
                    Reseller reseller = (Reseller) result;
                    global.setReseller(reseller);

                    activateEventButton();
                }

                @Override
                public void onFailure(Throwable caught) {

                }
            });
        }


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                Log.d("RegIntentService", "sentToken= " + sentToken);
            }
        };

        // Registering BroadcastReceiver
        registerReceiver();

        if (checkPlayServices()) {
            Log.d("RegIntentService", "checkPlayServices= true");

            // Start IntentService to register this application with GCM.
            Intent _intent = new Intent(this, RegistrationIntentService.class);
            startService(_intent);
        }
    }

    private void registerReceiver(){
        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
        registerReceiver();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
        super.onPause();
    }

    protected void configureButtonMenu() {
        Button bOffer = (Button) findViewById(R.id.bOffer);
        if(bOffer != null) {
            bOffer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vpContent.setCurrentItem(0);
                }
            });
        }

        Button bOrder = (Button) findViewById(R.id.bOrder);
        if(bOrder != null) {
            bOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    secureButtonMenu(1);
                }
            });
        }

        Button bProfile = (Button) findViewById(R.id.bProfile);
        if(bProfile != null) {
            bProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pageNumber = 2;
                    if(global.getReseller() != null && global.getReseller().getCustom_url() != null && !global.getReseller().getCustom_url().equals("")) {
                        pageNumber = 3;
                    }
                    secureButtonMenu(pageNumber);
                }
            });
        }

        activateEventButton();
    }

    protected void activateEventButton() {
        final Button bEvent = (Button) findViewById(R.id.bEvent);
        if(bEvent != null) {
            if(global.getReseller() != null && global.getReseller().getButton_text() != null && !global.getReseller().getButton_text().equals(""))
                bEvent.setText(global.getReseller().getButton_text());

            if(global.getReseller() != null && global.getReseller().getCustom_url() != null && !global.getReseller().getCustom_url().equals("")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bEvent.setVisibility(View.VISIBLE);

                        bEvent.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Log.d(Global.LOG_TAG, "EventButton clicked");
                                secureButtonMenu(2);
                                //openWeb(global.getReseller().getCustom_url());
                            }
                        });

                    }
                });

                //Log.d(Global.LOG_TAG, "EventButton configure");

                NUMBER_PAGES = 4;
                contentPagerAdapter.setPageNumber(NUMBER_PAGES);
                contentPagerAdapter.notifyDataSetChanged();

            } else {
                bEvent.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void apiLogin() {
        String token = global.getAccessToken().getToken();

        UsersService.getInstance(getApplicationContext()).login(Global.PROVIDER_FB, token, new CallBack() {
            @Override
            public void onSuccess(Object result) {
                Auth auth = (Auth) result;
                global.setApiAuth(auth.getToken());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        contentPagerAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onFailure(Throwable caught) {

            }
        });
    }

    private void secureButtonMenu(int pageNumber) {
        //Log.d(Global.LOG_TAG, "SecureButton pageNumber " + pageNumber);
        if((pageNumber == 0
                || (pageNumber == 2
                    && global.getReseller() != null && global.getReseller().getCustom_url() != null
                    && !global.getReseller().getCustom_url().equals("")))
                || (global.isApiAuthenticated() && global.isFacebookLogin())) {
            //Log.d(Global.LOG_TAG, "SecureButton CurrentItem pageNumber " + pageNumber);
            vpContent.setCurrentItem(pageNumber);
        } else {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivityForResult(intent, Global.REQUEST_CODE_FACEBOOK_LOGIN);
        }
    }

    public void selectCurrentPage(int number) {
        vpContent.setCurrentItem(number);
    }

    public void selectButtonMenu(int number) {
        LinearLayout llButtonMenu = (LinearLayout) findViewById(R.id.llButtonMenu);
        int childCount = llButtonMenu.getChildCount();

        /*
        if(number == 2 && global.getReseller() != null && global.getReseller().getCustom_url() != null && !global.getReseller().getCustom_url().equals("")) {
            number = 3;
        }
        */

        for (int i = 0; i < childCount; i++) {
            View view = llButtonMenu.getChildAt(i);
            if (view instanceof Button) {
                if(i == number) {
                    view.setAlpha(1f);
                    view.setClickable(false);
                } else {
                    view.setAlpha(0.5f);
                    view.setClickable(true);
                }
            }
        }
    }

    public void selectHeaderTitle(int pageNumber) {
        String title;
        switch (pageNumber) {
            case 1:
                title = getResources().getString(R.string.orders);
                break;
            case 2:
            case 3:
                if(pageNumber == 2 && global.getReseller() != null && global.getReseller().getCustom_url() != null && !global.getReseller().getCustom_url().equals("")) {
                    title = global.getReseller().getButton_text();
                } else {
                    title = getResources().getString(R.string.profile);
                }
                break;
            case 0:
            default:
                title = getResources().getString(R.string.offers);
                break;
        }

        if(tvToolbarTitle != null)
            tvToolbarTitle.setText(title);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Global.REQUEST_CODE_FACEBOOK_LOGIN) {
            if (resultCode == Activity.RESULT_OK) {
                if(contentPagerAdapter != null)
                    contentPagerAdapter.notifyDataSetChanged();
            } else {
                Log.i(Global.LOG_TAG, "FACEBOOK: Login error");
            }
        }
    }


    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i("checkPlayServices", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }



    private class ContentPagerAdapter extends CacheFragmentStatePagerAdapter {
        private int pageNumber = NUMBER_PAGES;

        public ContentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment createItem(int position) {
            return MainFragment.create(position);
        }

        @Override
        public int getCount() {
            return pageNumber;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        public void setPageNumber(int pageNumber) {
            this.pageNumber = pageNumber;
        }
    }
}
