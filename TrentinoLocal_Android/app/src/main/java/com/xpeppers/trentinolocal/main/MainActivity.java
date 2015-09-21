package com.xpeppers.trentinolocal.main;

import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.ksoichiro.android.observablescrollview.CacheFragmentStatePagerAdapter;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.rey.material.widget.ImageButton;
import com.xpeppers.trentinolocal.BaseActivity;
import com.xpeppers.trentinolocal.R;

/**
 * @author Emilio Frusciante - FEj (efrusciante AT wish-op DOT com)
 * @since 07/04/15
 */
public class MainActivity extends BaseActivity implements ObservableScrollViewCallbacks, SearchView.OnQueryTextListener {
    public static final String EXTRA_START_PAGE = "extra_start_page";

    private int NUMBER_PAGES = 4;
    private View mHeaderView = null;
    private Toolbar mToolbarView = null;
    private int mBaseTranslationY = 0;
    private ViewPager vpContent;
    private ContentPagerAdapter contentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHeaderView = findViewById(R.id.header);
        mToolbarView = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbarView);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        vpContent = (ViewPager) findViewById(R.id.vpContent);
        contentPagerAdapter = new ContentPagerAdapter(getSupportFragmentManager());
        vpContent.setAdapter(contentPagerAdapter);
        vpContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectButtonMenu(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        configureButtonMenu();


        Intent intent = getIntent();
        int startPage = intent.getIntExtra(EXTRA_START_PAGE, 1);
        vpContent.setCurrentItem(startPage);
        selectButtonMenu(startPage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
            if (searchView != null) {
                searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
                searchView.setIconifiedByDefault(false);
                searchView.setOnQueryTextListener(this);
            }
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                onSearchRequested();
                return true;
            case R.id.action_map:
                MapFragment mapFragment = new MapFragment();
                String tag = mapFragment.toString();
                Bundle args = new Bundle();
                mapFragment.setArguments(args);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.vpContent, mapFragment)
                        .commit();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextChange(String query) {
        Toast.makeText(MainActivity.this, query, Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Toast.makeText(MainActivity.this, query, Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        if (dragging) {
            int toolbarHeight = mToolbarView.getHeight();
            float currentHeaderTranslationY = ViewHelper.getTranslationY(mHeaderView);
            if (firstScroll) {
                if (-toolbarHeight < currentHeaderTranslationY) {
                    mBaseTranslationY = scrollY;
                }
            }
            float headerTranslationY = ScrollUtils.getFloat(-(scrollY - mBaseTranslationY), -toolbarHeight, 0);
            ViewPropertyAnimator.animate(mHeaderView).cancel();
            ViewHelper.setTranslationY(mHeaderView, headerTranslationY);
        }
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        mBaseTranslationY = 0;

        int toolbarHeight = mToolbarView.getHeight();

        Fragment fragment = getCurrentFragment();
        if (fragment == null) {
            return;
        }
        View view = fragment.getView();
        if (view == null) {
            return;
        }

        final ObservableRecyclerView scrollView = (ObservableRecyclerView) view.findViewById(R.id.cardList);
        if (scrollView == null) {
            return;
        }

        int scrollY = scrollView.getCurrentScrollY();
        if (scrollState == ScrollState.DOWN) {
            showToolbar();
        } else if (scrollState == ScrollState.UP) {
            if (toolbarHeight <= scrollY) {
                hideToolbar();
            } else {
                showToolbar();
            }
        } else {
            if(!toolbarIsShown() && !toolbarIsHidden()) {
                showToolbar();
            }
        }
    }

    private boolean toolbarIsShown() {
        return ViewHelper.getTranslationY(mHeaderView) == 0;
    }
    private boolean toolbarIsHidden() {
        return ViewHelper.getTranslationY(mHeaderView) == -mToolbarView.getHeight();
    }

    private void showToolbar() {
        float headerTranslationY = ViewHelper.getTranslationY(mHeaderView);
        if (headerTranslationY != 0) {
            ViewPropertyAnimator.animate(mHeaderView).cancel();
            ViewPropertyAnimator.animate(mHeaderView).translationY(0).setDuration(200).start();
        }
        //if(flCardList != null) ViewPropertyAnimator.animate(flCardList).translationY(mToolbarView.getHeight()).setDuration(200).start();
    }

    private void hideToolbar() {
        float headerTranslationY = ViewHelper.getTranslationY(mHeaderView);
        int toolbarHeight = mToolbarView.getHeight();
        if (headerTranslationY != -toolbarHeight) {
            ViewPropertyAnimator.animate(mHeaderView).cancel();
            ViewPropertyAnimator.animate(mHeaderView).translationY(-toolbarHeight).setDuration(200).start();
        }
        //if(flCardList != null) ViewPropertyAnimator.animate(flCardList).translationY(0).setDuration(200).start();
    }

    protected void configureButtonMenu() {
        ImageButton ibMap = (ImageButton) findViewById(R.id.ibMap);
        if(ibMap != null) {
            ibMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vpContent.setCurrentItem(0);
                }
            });
        }
        ImageButton ibBuilding = (ImageButton) findViewById(R.id.ibBuilding);
        if(ibBuilding != null) {
            ibBuilding.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vpContent.setCurrentItem(1);
                }
            });
        }

        ImageButton ibTicket = (ImageButton) findViewById(R.id.ibTicket);
        if(ibTicket != null) {
            ibTicket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vpContent.setCurrentItem(2);
                }
            });
        }

        ImageButton ibUser = (ImageButton) findViewById(R.id.ibUser);
        if(ibUser != null) {
            ibUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vpContent.setCurrentItem(3);
                }
            });
        }
    }

    public void selectButtonMenu(int number) {
        LinearLayout llButtonMenu = (LinearLayout) findViewById(R.id.llButtonMenu);
        int childCount = llButtonMenu.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = llButtonMenu.getChildAt(i);
            if (view instanceof ImageButton) {
                if(i != number) {
                    view.setAlpha(1f);
                    view.setClickable(true);
                } else {
                    view.setAlpha(0.5f);
                    view.setClickable(false);
                }
            }
        }
    }

    private Fragment getCurrentFragment() {
        return contentPagerAdapter.getItemAt(vpContent.getCurrentItem());
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
    }
}
