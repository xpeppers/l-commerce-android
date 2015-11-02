package com.xpeppers.trentinolocal.details;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.rey.material.widget.Button;
import com.xpeppers.trentinolocal.BaseActivity;
import com.xpeppers.trentinolocal.R;
import com.xpeppers.trentinolocal.main.MainActivity;

public class ConfirmOrderDetailActivity extends BaseActivity {
    public static final String EXTRA_TITLE = "extra_title";
    public static final String EXTRA_PAYMENT_ID = "extra_payment_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_confirm_order);

        Intent intent = getIntent();
        String title = intent.getStringExtra(EXTRA_TITLE);
        String paymentId = intent.getStringExtra(EXTRA_PAYMENT_ID);


        Toolbar mToolbarView = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbarView);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView tvToolbarTitle = (TextView) findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText(title);

        TextView tvCode = (TextView) findViewById(R.id.tvCode);
        tvCode.setText(paymentId);

        Button bContinue = (Button) findViewById(R.id.bContinue);
        bContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfirmOrderDetailActivity.this, MainActivity.class);
                intent.putExtra(MainActivity.EXTRA_START_PAGE, 0);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
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

    @Override
    public void onBackPressed() {
        openMyPrenotation();
    }

    private void openMyPrenotation() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.EXTRA_START_PAGE, 1);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
