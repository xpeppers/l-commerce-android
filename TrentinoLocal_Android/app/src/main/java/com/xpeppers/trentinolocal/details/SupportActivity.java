package com.xpeppers.trentinolocal.details;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.xpeppers.trentinolocal.BaseActivity;
import com.xpeppers.trentinolocal.R;

public class SupportActivity extends BaseActivity {
    private TextView tvToolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        Toolbar mToolbarView = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbarView);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvToolbarTitle = (TextView) findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText(getResources().getString(R.string.support));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
