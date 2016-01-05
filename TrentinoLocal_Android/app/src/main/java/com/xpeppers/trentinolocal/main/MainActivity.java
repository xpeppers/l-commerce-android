package com.xpeppers.trentinolocal.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.appevents.AppEventsLogger;
import com.github.ksoichiro.android.observablescrollview.CacheFragmentStatePagerAdapter;
import com.rey.material.widget.Button;
import com.xpeppers.servicelib.bean.Auth;
import com.xpeppers.servicelib.bean.Reseller;
import com.xpeppers.servicelib.services.ResellerService;
import com.xpeppers.servicelib.services.UsersService;
import com.xpeppers.servicelib.utils.CallBack;
import com.xpeppers.trentinolocal.BaseActivity;
import com.xpeppers.trentinolocal.Global;
import com.xpeppers.trentinolocal.R;
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

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View mHeaderView = findViewById(R.id.header);
        //mHeaderView.setElevation(4f);

        Toolbar mToolbarView = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbarView);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        tvToolbarTitle = (TextView) findViewById(R.id.tvToolbarTitle);

        vpContent = (ViewPager) findViewById(R.id.vpContent);

        contentPagerAdapter = new ContentPagerAdapter(getSupportFragmentManager());
        vpContent.setAdapter(contentPagerAdapter);

        vpContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if((position == 1 || position == 2) && !(global.isApiAuthenticated() && global.isFacebookLogin())) {
                    secureButtonMenu(position);
                    vpContent.setCurrentItem(0);
                } else {
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
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
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
                    secureButtonMenu(2);
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
                    }
                });

                bEvent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openWeb(global.getReseller().getCustom_url());
                    }
                });
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
        if(global.isApiAuthenticated() && global.isFacebookLogin()) {
            vpContent.setCurrentItem(pageNumber);
        } else {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivityForResult(intent, Global.REQUEST_CODE_FACEBOOK_LOGIN);
        }
    }

    public void selectButtonMenu(int number) {
        LinearLayout llButtonMenu = (LinearLayout) findViewById(R.id.llButtonMenu);
        int childCount = llButtonMenu.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = llButtonMenu.getChildAt(i);
            if (view instanceof Button) {
                if(i != number) {
                    view.setAlpha(0.5f);
                    view.setClickable(true);
                } else {
                    view.setAlpha(1f);
                    view.setClickable(false);
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
                title = getResources().getString(R.string.profile);
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

    private class ContentPagerAdapter extends CacheFragmentStatePagerAdapter {
        public ContentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment createItem(int position) {
            return MainFragment.create(position);
        }

        @Override
        public int getCount() {
            return NUMBER_PAGES;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }
}
