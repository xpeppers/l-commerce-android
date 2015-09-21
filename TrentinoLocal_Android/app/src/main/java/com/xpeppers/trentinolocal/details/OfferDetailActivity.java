package com.xpeppers.trentinolocal.details;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.rey.material.widget.Button;
import com.squareup.picasso.Picasso;
import com.xpeppers.servicelib.bean.Offer;
import com.xpeppers.servicelib.bean.Payment;
import com.xpeppers.servicelib.services.OffersService;
import com.xpeppers.servicelib.services.OrdersService;
import com.xpeppers.servicelib.utils.CallBack;
import com.xpeppers.trentinolocal.BaseActivity;
import com.xpeppers.trentinolocal.R;

import java.math.BigDecimal;

/**
 * @author Emilio Frusciante - FEj (efrusciante AT wish-op DOT com)
 * @since 07/04/15
 */
public class OfferDetailActivity extends BaseActivity {
    public final static String EXTRA_OFFER_ID = "extra_offer_id";

    private ImageView ivBgOffer;
    private TextView tvTitle;
    //private TextView tvShortDescription;
    private TextView tvDescription;
    private Button bPurchase;
    //private LinearLayout llGallery;

    //static final LatLng HAMBURG = new LatLng(53.558, 9.927);
    //static final LatLng KIEL = new LatLng(53.551, 9.993);
    private GoogleMap map;

    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment (ENVIRONMENT_NO_NETWORK).
            // When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId("AXcACHOqhpKmYnfd9bjv5BPNrmSg4lGLbLEDO-9pIfvM_0dbuLcnQbs9wpzGmeScc6tljPvv2u6ld683");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_offer);

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
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        //tvShortDescription = (TextView) findViewById(R.id.tvShortDescription);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        bPurchase = (Button) findViewById(R.id.bPurchase);
        //llGallery = (LinearLayout) findViewById(R.id.llGallery);

        Intent intent = getIntent();
        int offerId = intent.getIntExtra(EXTRA_OFFER_ID, 0);
        loadData(offerId);

        // Map
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapView)).getMap();

        bPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                onBuyPressed(v);
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

    private void loadData(int offerId) {
        OffersService.getInstance(getApplicationContext()).get(offerId, new CallBack() {
            @Override
            public void onSuccess(Object result) {
                Offer offer = (Offer) result;
                asyncResponse(offer);
            }

            @Override
            public void onFailure(Throwable caught) {
                asyncDialog("Error", caught.getMessage());
            }
        });
    }

    private void asyncResponse(final Offer offer) {
        global.setSelectedOffer(offer);
        refreshActivity();
    }

    private void refreshActivity() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Offer offer = global.getSelectedOffer();

                tvTitle.setText(offer.getTitle());
                //tvShortDescription.setText(Html.fromHtml(offer.getShort_description()));
                tvDescription.setText(Html.fromHtml(offer.getDescription()));
                bPurchase.setText("Acquista    € " + offer.getOriginal_price());
                //new DownloadImage(ivBgOffer).execute(offer.getImage());
                Picasso.with(getBaseContext())
                        .load(offer.getImage_url())
                        .placeholder(R.drawable.placeholder)
                        .into(ivBgOffer);

                /*
                for (int i = 0; i < offer.getGallery_images().size(); i++) {
                    ImageView imageView = new ImageView(global.getCurrentActivity());
                    imageView.setId(i);
                    imageView.setPadding(2, 2, 2, 2);

                    //new DownloadImage(imageView).execute(offer.getGallery_images().get(i));
                    Picasso.with(getBaseContext())
                            .load(offer.getGallery_images().get(i))
                            .placeholder(R.drawable.placeholder)
                            .into(imageView);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    llGallery.addView(imageView);
                }
                */

                if (map != null) {
                    LatLng position = new LatLng(offer.getAddress().getLatitude(), offer.getAddress().getLongitude());
                    //Marker pinPosition = map.addMarker(new MarkerOptions().position(position));
                    //Marker hamburg = map.addMarker(new MarkerOptions().position(position).title("Hamburg"));
                    /*
                    Marker kiel = map.addMarker(new MarkerOptions()
                            .position(KIEL)
                            .title("Kiel")
                            .snippet("Kiel is cool")
                            .icon(BitmapDescriptorFactory
                                    .fromResource(R.mipmap.ic_launcher)));
                    */

                    // Move the camera instantly to hamburg with a zoom of 15.
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));

                    // Zoom in, animating the camera.
                    map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);


                }
            }
        });
    }

    public void onBuyPressed(View pressed) {

        // PAYMENT_INTENT_SALE will cause the payment to complete immediately.
        // Change PAYMENT_INTENT_SALE to
        //   - PAYMENT_INTENT_AUTHORIZE to only authorize payment and capture funds later.
        //   - PAYMENT_INTENT_ORDER to create a payment for authorization and capture
        //     later via calls from your server.

        PayPalPayment payment = new PayPalPayment(new BigDecimal(global.getSelectedOffer().getPrice()), "EUR",
                global.getSelectedOffer().getTitle(), PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);

        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            final PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null &&
                    confirm.getProofOfPayment() != null && confirm.getProofOfPayment().getState().equals("approved")) {

                // TODO contact my server for verification
                // TODO: send 'confirm' to your server for verification.
                // see https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                // for more details.

                OrdersService.getInstance(getBaseContext()).pay(global.getSelectedOrder().getId(), confirm.getProofOfPayment().getPaymentId(), new CallBack() {
                    @Override
                    public void onSuccess(Object result) {
                        Payment payment = (Payment) result;
                        Intent intent = new Intent(OfferDetailActivity.this, ConfirmOrderDetailActivity.class);
                        intent.putExtra(ConfirmOrderDetailActivity.EXTRA_PAYMENT_ID, payment.getPaypal_payment_token());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Throwable caught) {

                    }
                });
            }
        }
        else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("paymentExample", "The user canceled.");
        }
        else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
        }
    }
}
