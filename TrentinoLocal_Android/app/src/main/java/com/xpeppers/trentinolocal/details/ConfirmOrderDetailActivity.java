package com.xpeppers.trentinolocal.details;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.xpeppers.servicelib.bean.Offer;
import com.xpeppers.trentinolocal.BaseActivity;
import com.xpeppers.trentinolocal.R;
import com.xpeppers.trentinolocal.main.MainActivity;

public class ConfirmOrderDetailActivity extends BaseActivity {
    public static final String EXTRA_PAYMENT_ID = "extra_payment_id";

    private ImageView ivBgOffer;
    private TextView tvConfirm;
    private TextView tvDescription;
    private TextView tvCode;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_confirm_order);

        Toolbar mToolbarView = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbarView);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ScrollView svMain = (ScrollView) findViewById(R.id.svMain);
        ImageView transparentImageView = (ImageView) findViewById(R.id.transparent_image);
        transparentImageView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        svMain.requestDisallowInterceptTouchEvent(true);
                        // Disable touch on transparent view
                        return false;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        svMain.requestDisallowInterceptTouchEvent(false);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        svMain.requestDisallowInterceptTouchEvent(true);
                        return false;

                    default:
                        return true;
                }
            }
        });

        ivBgOffer = (ImageView) findViewById(R.id.ivBgOffer);
        tvConfirm = (TextView) findViewById(R.id.tvConfirm);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        tvCode = (TextView) findViewById(R.id.tvCode);

        Intent intent = getIntent();
        String paymentId = intent.getStringExtra(EXTRA_PAYMENT_ID);

        // Map
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapView)).getMap();

        refreshActivity();
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
        intent.putExtra(MainActivity.EXTRA_START_PAGE, 2);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void refreshActivity() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Offer offer = global.getSelectedOffer();

                //tvConfirm.setText(offer.getTitle());
                //tvShortDescription.setText(Html.fromHtml(offer.getShort_description()));
                //tvDescription.setText(Html.fromHtml(offer.getDescription()));
                String htmlDetail = "ecco i dettagli della tua offerta:<br/><br/>";
                htmlDetail += offer.getTitle();
                htmlDetail += "<br/><br/>";
                htmlDetail += "Tel. 0465 800071";
                htmlDetail += "<br/><br/>";
                htmlDetail += "Come utilizzare il coupon<br/>Telefonare per effettuare la prenotazione.<br/>Comunicare il codice coupon:<b>543231</b><br/><br/>Eventuali integrazione e servizi aggiuntivi devono essere pagati all'arrivo.";
                tvDescription.setText(Html.fromHtml(htmlDetail));
                tvCode.setText(tvCode.getText() + " 543231");

                //new DownloadImage(ivBgOffer).execute(offer.getImage());
                /*
                Picasso.with(getBaseContext())
                        .load(offer.getImage())
                        .placeholder(R.drawable.placeholder)
                        .into(ivBgOffer);

                if (map != null) {
                    LatLng position = new LatLng(offer.getLatitude(), offer.getLongitude());
                    Marker hamburg = map.addMarker(new MarkerOptions().position(position));
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
                    map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
                }
                */
            }
        });
    }
}
