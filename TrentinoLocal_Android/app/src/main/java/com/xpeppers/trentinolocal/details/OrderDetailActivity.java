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
import com.xpeppers.servicelib.bean.Order;
import com.xpeppers.servicelib.services.OrdersService;
import com.xpeppers.servicelib.utils.CallBack;
import com.xpeppers.trentinolocal.BaseActivity;
import com.xpeppers.trentinolocal.R;

public class OrderDetailActivity extends BaseActivity {
    public static final String EXTRA_ORDER_ID = "extra_order_id";

    private ImageView ivBgOffer;
    private TextView tvConfirm;
    private TextView tvDescription;
    private TextView tvCode;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order);

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
        int orderId = intent.getIntExtra(EXTRA_ORDER_ID, 0);
        loadData(orderId);

        // Map
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapView)).getMap();
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

    private void loadData(int orderId) {
        long userId = 123;
        String apiKey = "abc123";
        OrdersService.getInstance(getApplicationContext()).get(orderId, new CallBack() {
            @Override
            public void onSuccess(Object result) {
                Order order = (Order) result;
                asyncResponse(order);
            }

            @Override
            public void onFailure(Throwable caught) {
                asyncDialog("Error", caught.getMessage());
            }
        });
    }

    private void asyncResponse(final Order order) {
        global.setSelectedOrder(order);
        refreshActivity();
    }

    private void refreshActivity() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Order order = global.getSelectedOrder();

                String htmlDetail = "ecco i dettagli della tua offerta:<br/><br/>";
                htmlDetail += "Come utilizzare il coupon<br/>Telefonare per effettuare la prenotazione.<br/>Comunicare il codice coupon:<b>"+
                        order.getCoupon().getCode()
                        +"</b><br/><br/>Eventuali integrazione e servizi aggiuntivi devono essere pagati all'arrivo.";
                tvDescription.setText(Html.fromHtml(htmlDetail));
                tvCode.setText(tvCode.getText() + " " + order.getCoupon().getCode());

                /*
                Picasso.with(getBaseContext())
                        .load(order.getOffers())
                        .placeholder(R.drawable.placeholder)
                        .into(ivBgOffer);
                */

                if (map != null) {
                    /*
                    LatLng position = new LatLng(order.getLatitude(), order.getLongitude());
                    Marker hamburg = map.addMarker(new MarkerOptions().position(position));
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
                    map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
                    */
                }
            }
        });
    }
}
