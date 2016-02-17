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
import com.xpeppers.trentinolocal.utils.Tracker;

public class ConfirmOrderDetailActivity extends BaseActivity {
    public static final String EXTRA_TITLE = "extra_title";
    public static final String EXTRA_PAYMENT_ID = "extra_payment_id";
    public static final String EXTRA_OFFER_CODE = "extra_offer_code";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_confirm_order);

        Tracker.sendScreenView("PAGAMENTO COMPLETATO");

        Intent intent = getIntent();
        String title = intent.getStringExtra(EXTRA_TITLE);
        String paymentId = intent.getStringExtra(EXTRA_PAYMENT_ID);
        String offerCode = intent.getStringExtra(EXTRA_OFFER_CODE);


        Toolbar mToolbarView = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbarView);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView tvToolbarTitle = (TextView) findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText(title);

        TextView tvCode = (TextView) findViewById(R.id.tvCode);
        tvCode.setText(paymentId);

        TextView tvOfferCode = (TextView) findViewById(R.id.tvOfferCode);
        tvOfferCode.setText(getResources().getString(R.string.offer_code, offerCode));
        if(offerCode != null)
            tvOfferCode.setVisibility(View.VISIBLE);
        else
            tvOfferCode.setVisibility(View.GONE);

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
