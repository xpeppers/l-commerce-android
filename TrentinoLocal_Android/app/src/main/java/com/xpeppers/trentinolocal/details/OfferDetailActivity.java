package com.xpeppers.trentinolocal.details;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.rey.material.widget.Button;
import com.rey.material.widget.ImageButton;
import com.squareup.picasso.Picasso;
import com.xpeppers.servicelib.bean.Coupon;
import com.xpeppers.servicelib.bean.Offer;
import com.xpeppers.servicelib.bean.Order;
import com.xpeppers.servicelib.bean.Payment;
import com.xpeppers.servicelib.services.OffersService;
import com.xpeppers.servicelib.services.OrdersService;
import com.xpeppers.servicelib.utils.CallBack;
import com.xpeppers.trentinolocal.BaseActivity;
import com.xpeppers.trentinolocal.Global;
import com.xpeppers.trentinolocal.PayPalConf;
import com.xpeppers.trentinolocal.R;
import com.xpeppers.trentinolocal.login.LoginActivity;
import com.xpeppers.trentinolocal.utils.Tracker;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @author Emilio Frusciante - FEj (efrusciante AT wish-op DOT com)
 * @since 07/04/15
 */
public class OfferDetailActivity extends BaseActivity {
    public final static String EXTRA_OFFER_ID = "extra_offer_id";

    private long offerId;
    private long orderId;
    private Coupon orderCoupon;

    private TextView tvToolbarTitle;

    private TextView tvTitle;
    private TextView tvDescription;
    private TextView tvMerchant;
    private TextView tvAddress;
    private ImageButton ibAddress;
    private TextView tvTel;
    private ImageButton ibTel;
    private TextView tvEmail;
    private ImageButton ibEmail;
    private TextView tvSiteWeb;
    private ImageButton ibSiteWeb;
    private TextView tvPrice;
    private TextView tvOldPrice;
    private Button bPurchase;
    private HorizontalScrollView hsViewMultiImage;
    private LinearLayout llGallery;
    private ImageView ivOfferSingleImage;
    private GoogleMap map;

    ShareButton shareButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(this);

        setContentView(R.layout.activity_detail_offer);


        showProgressDialog();

        Toolbar mToolbarView = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbarView);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvToolbarTitle = (TextView) findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("Dettaglio");

        shareButton = (ShareButton)findViewById(R.id.share_btn);


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

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        tvMerchant = (TextView) findViewById(R.id.tvMerchant);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        ibAddress = (ImageButton) findViewById(R.id.ibAddress);
        ibAddress.setImageDrawable(
                new IconicsDrawable(this)
                        .icon(GoogleMaterial.Icon.gmd_near_me)
                        .color(Color.BLACK)
                        .sizeDp(24));

        tvTel = (TextView) findViewById(R.id.tvTel);
        ibTel = (ImageButton) findViewById(R.id.ibTel);
        ibTel.setImageDrawable(
                new IconicsDrawable(this)
                        .icon(GoogleMaterial.Icon.gmd_phone)
                        .color(Color.BLACK)
                        .sizeDp(24));

        tvEmail = (TextView) findViewById(R.id.tvEmail);
        ibEmail = (ImageButton) findViewById(R.id.ibEmail);
        ibEmail.setImageDrawable(
                new IconicsDrawable(this)
                        .icon(GoogleMaterial.Icon.gmd_mail)
                        .color(Color.BLACK)
                        .sizeDp(24));

        tvSiteWeb = (TextView) findViewById(R.id.tvSiteWeb);
        ibSiteWeb = (ImageButton) findViewById(R.id.ibSiteWeb);
        ibSiteWeb.setImageDrawable(
                new IconicsDrawable(this)
                        .icon(GoogleMaterial.Icon.gmd_web)
                        .color(Color.BLACK)
                        .sizeDp(24));

        tvPrice = (TextView) findViewById(R.id.tvPrice);
        tvOldPrice = (TextView) findViewById(R.id.tvOldPrice);
        bPurchase = (Button) findViewById(R.id.bPurchase);

        hsViewMultiImage = (HorizontalScrollView) findViewById(R.id.hsViewMultiImage);
        llGallery = (LinearLayout) findViewById(R.id.llGallery);
        ivOfferSingleImage = (ImageView) findViewById(R.id.ivOfferSingleImage);

        // Map
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapView)).getMap();

        bPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                onBuyPressed();
            }
        });


        Intent intent = getIntent();
        offerId = intent.getLongExtra(EXTRA_OFFER_ID, 0);
        loadData(offerId);
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

    private void loadData(long offerId) {
        Log.i("OFFER DETAIL ACTIVITY", offerId + " ");
        OffersService.getInstance(getApplicationContext()).get(offerId, new CallBack() {
            @Override
            public void onSuccess(Object result) {
                hideProgressDialog();
                Offer offer = (Offer) result;
                asyncResponse(offer);
            }

            @Override
            public void onFailure(Throwable caught) {
                // TODO: 4/15/16  handle better: dialog error should be changed and better to close current activity
                hideProgressDialog();
                asyncDialog("Error", caught.getMessage());
                Log.i("OFFER DETAIL ACTIVITY", caught.getLocalizedMessage());

            }
        });
    }

    private void asyncResponse(final Offer offer) {
        global.setSelectedOffer(offer);
        refreshActivity();
    }

    private void refreshActivity() {
        final Offer offer = global.getSelectedOffer();

        Tracker.sendScreenView("OFFERTE-" + offer.getTitle());

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvToolbarTitle.setText(offer.getTitle());
                tvTitle.setText(Html.fromHtml(offer.getTitle()));
                tvDescription.setText(Html.fromHtml(offer.getDescription()));
                tvMerchant.setText(Html.fromHtml(offer.getMerchant()));
                tvAddress.setText(offer.getAddress().toString());
                tvAddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LatLng position = new LatLng(offer.getAddress().getLatitude(), offer.getAddress().getLongitude());
                        openNavigator(offer.getAddress().toString(), position);
                    }
                });
                ibAddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LatLng position = new LatLng(offer.getAddress().getLatitude(), offer.getAddress().getLongitude());
                        openNavigator(offer.getAddress().toString(), position);
                    }
                });

                if (offer.getTelephone() != null && !offer.getTelephone().equals("")) {
                    tvTel.setText(getResources().getString(R.string.tel, offer.getTelephone()));
                    tvTel.setVisibility(View.VISIBLE);
                    tvTel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            callNumber(offer.getTelephone());
                        }
                    });
                    ibTel.setVisibility(View.VISIBLE);
                    ibTel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            callNumber(offer.getTelephone());
                        }
                    });
                } else {
                    tvTel.setVisibility(View.GONE);
                    ibTel.setVisibility(View.GONE);
                }
                if (offer.getEmail() != null && !offer.getEmail().equals("")) {
                    tvEmail.setText(getResources().getString(R.string.email, offer.getEmail()));
                    tvEmail.setVisibility(View.VISIBLE);
                    tvEmail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sendEmail(offer.getEmail());
                        }
                    });
                    ibEmail.setVisibility(View.VISIBLE);
                    ibEmail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sendEmail(offer.getEmail());
                        }
                    });
                } else {
                    tvEmail.setVisibility(View.GONE);
                    ibEmail.setVisibility(View.GONE);
                }
                if (offer.getWeb_site() != null && !offer.getWeb_site().equals("")) {
                    tvSiteWeb.setText(getResources().getString(R.string.siteweb, offer.getWeb_site()));
                    tvSiteWeb.setVisibility(View.VISIBLE);
                    tvSiteWeb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openWeb(offer.getWeb_site());
                        }
                    });
                    ibSiteWeb.setVisibility(View.VISIBLE);
                    ibSiteWeb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openWeb(offer.getWeb_site());
                        }
                    });
                } else {
                    tvSiteWeb.setVisibility(View.GONE);
                    ibSiteWeb.setVisibility(View.GONE);
                }

                String formattedPrice = new DecimalFormat("##,##0.00€").format(offer.getPrice());
                tvPrice.setText(formattedPrice);

                if (offer.getOriginal_price() > 0) {
                    String formattedOriginalPrice = new DecimalFormat("##,##0.00€").format(offer.getOriginal_price());
                    tvOldPrice.setText(Html.fromHtml("<strike>" + formattedOriginalPrice + "</strike>"));
                    tvOldPrice.setPaintFlags(tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    tvOldPrice.setVisibility(View.VISIBLE);
                } else {
                    tvOldPrice.setVisibility(View.GONE);
                }


                String formattedReservationPrice = new DecimalFormat("##,##0.00€").format(offer.getReservation_price());
                bPurchase.setText(getResources().getString(R.string.reservation, formattedReservationPrice));

                if (offer.getImage_gallery().size() == 1) {
                    hsViewMultiImage.setVisibility(View.GONE);
                    ivOfferSingleImage.setVisibility(View.VISIBLE);

                    Picasso.with(getBaseContext())
                            .load(offer.getImage_gallery().get(0))
                            .placeholder(R.drawable.placeholder)
                            .fit()
                            .centerCrop()
                            .into(ivOfferSingleImage);
                } else {
                    hsViewMultiImage.setVisibility(View.VISIBLE);
                    ivOfferSingleImage.setVisibility(View.GONE);

                    for (int i = 0; i < offer.getImage_gallery().size(); i++) {
                        ImageView imageView = new ImageView(global.getCurrentActivity());
                        imageView.setId(i);
                        imageView.setPadding(2, 2, 2, 2);

                        Picasso.with(getBaseContext())
                                .load(offer.getImage_gallery().get(i))
                                .placeholder(R.drawable.placeholder)
                                .fit()
                                .centerCrop()
                                .into(imageView);
                        llGallery.addView(imageView);
                    }
                }

                if (map != null) {
                    configureMap(map, offer.getAddress().getLatitude(), offer.getAddress().getLongitude());
                }

                if(offer.getUrl() != null){
                    ShareLinkContent content = new ShareLinkContent.Builder()
                            .setContentUrl(Uri.parse(offer.getUrl()))
                            .build();
                    shareButton.setShareContent(content);
                }
            }
        });
    }

    private void configureMap(GoogleMap map, double lat, double lon)
    {
        if (map != null) {
            LatLng position = new LatLng(lat, lon);
            map.addMarker(new MarkerOptions().position(position).icon(BitmapDescriptorFactory.fromResource(R.mipmap.pin)));
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(position);
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 15.0f));
            map.getUiSettings().setScrollGesturesEnabled(false);
        }
    }


    public void onBuyPressed() {
        if(global.isApiAuthenticated() && global.isFacebookLogin()) {
            showProgressDialog();

            OrdersService.getInstance(getApplicationContext()).create(global.getApiToken(), offerId, new CallBack() {
                @Override
                public void onSuccess(Object result) {
                    hideProgressDialog();

                    Order order = (Order) result;
                    orderId = order.getId();
                    orderCoupon = order.getCoupon();

                    PayPalConfiguration config = new PayPalConfiguration()
                            .environment(PayPalConf.MODE)
                            .clientId(PayPalConf.APP_ID);

                    PayPalPayment payment = new PayPalPayment(new BigDecimal(global.getSelectedOffer().getReservation_price()), "EUR",
                            global.getSelectedOffer().getTitle(), PayPalPayment.PAYMENT_INTENT_AUTHORIZE);

                    Intent intent = new Intent(OfferDetailActivity.this, PaymentActivity.class);

                    intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
                    intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
                    startActivityForResult(intent, Global.REQUEST_CODE_PAYPAL);
                }

                @Override
                public void onFailure(Throwable caught) {
                    hideProgressDialog();
                }
            });
        } else {
            // Login
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, Global.REQUEST_CODE_FACEBOOK_LOGIN);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        global.setCurrentActivity(this);

        if(requestCode == Global.REQUEST_CODE_PAYPAL) {
            if (resultCode == Activity.RESULT_OK) {
                final PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null &&
                        confirm.getProofOfPayment() != null && confirm.getProofOfPayment().getState().equals("approved")) {
                    showProgressDialog();

                    OrdersService.getInstance(getBaseContext()).confirmPayment(global.getApiToken(), orderId, confirm.getProofOfPayment().getPaymentId(), new CallBack() {
                        @Override
                        public void onSuccess(Object result) {
                            hideProgressDialog();
                            Payment payment = (Payment) result;
                            Intent intent = new Intent(OfferDetailActivity.this, ConfirmOrderDetailActivity.class);
                            intent.putExtra(ConfirmOrderDetailActivity.EXTRA_PAYMENT_ID, payment.getPaypal_payment_token());
                            if(orderCoupon != null)
                                intent.putExtra(ConfirmOrderDetailActivity.EXTRA_OFFER_CODE, orderCoupon.getCode());

                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(Throwable caught) {
                            hideProgressDialog();
                            Intent intent = new Intent(OfferDetailActivity.this, ErrorOrderActivity.class);
                            startActivity(intent);
                        }
                    });
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i(Global.LOG_TAG, "PAYPAL: The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(Global.LOG_TAG, "PAYPAL: An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }

        if(requestCode == Global.REQUEST_CODE_FACEBOOK_LOGIN) {
            if (resultCode == Activity.RESULT_OK) {
                onBuyPressed();
            } else {
                Log.i(Global.LOG_TAG, "FACEBOOK: Login error");
            }
        }
    }
}
