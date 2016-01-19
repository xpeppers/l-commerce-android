package com.xpeppers.trentinolocal.details;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.rey.material.widget.ImageButton;
import com.squareup.picasso.Picasso;
import com.xpeppers.servicelib.bean.OfferBought;
import com.xpeppers.servicelib.services.OffersBoughtService;
import com.xpeppers.servicelib.utils.CallBack;
import com.xpeppers.trentinolocal.BaseActivity;
import com.xpeppers.trentinolocal.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class OfferBoughtDetailActivity extends BaseActivity {
    public static final String EXTRA_OFFER_BOUGHT_ID = "extra_offer_bought_id";

    private long offerBoughtId;

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

    private LinearLayout llCall;
    private TextView tvUserFullname;
    //private TextView tvStatus;
    private TextView tvCoupon;
    private TextView tvPrice;
    private TextView tvDate;
    private TextView tvHowDoesItWork;
    private HorizontalScrollView hsViewMultiImage;
    private LinearLayout llGallery;
    private ImageView ivOfferSingleImage;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_offer_bought);

        showProgressDialog();

        Toolbar mToolbarView = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbarView);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvToolbarTitle = (TextView) findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("Dettaglio");

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

        llCall = (LinearLayout) findViewById(R.id.llCall);

        tvUserFullname = (TextView) findViewById(R.id.tvUserFullname);
        //tvStatus = (TextView) findViewById(R.id.tvStatus);
        tvCoupon = (TextView) findViewById(R.id.tvCoupon);
        tvPrice = (TextView) findViewById(R.id.tvPrice);
        tvDate = (TextView) findViewById(R.id.tvDate);
        tvHowDoesItWork = (TextView) findViewById(R.id.tvHowDoesItWork);

        hsViewMultiImage = (HorizontalScrollView) findViewById(R.id.hsViewMultiImage);
        llGallery = (LinearLayout) findViewById(R.id.llGallery);
        ivOfferSingleImage = (ImageView) findViewById(R.id.ivOfferSingleImage);

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapView)).getMap();

        Intent intent = getIntent();
        offerBoughtId = intent.getLongExtra(EXTRA_OFFER_BOUGHT_ID, 0);
        loadData(offerBoughtId);
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

    private void loadData(long offerBoughtId) {
        OffersBoughtService.getInstance(getApplicationContext()).get(global.getApiToken(), offerBoughtId, new CallBack() {
            @Override
            public void onSuccess(Object result) {
                hideProgressDialog();
                OfferBought offerBought = (OfferBought) result;
                asyncResponse(offerBought);
            }

            @Override
            public void onFailure(Throwable caught) {
                hideProgressDialog();
                asyncDialog("Error", caught.getMessage());
            }
        });
    }

    private void asyncResponse(final OfferBought offerBought) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvToolbarTitle.setText(offerBought.getTitle());
                tvTitle.setText(Html.fromHtml(offerBought.getTitle()));
                tvDescription.setText(Html.fromHtml(offerBought.getDescription()));
                tvMerchant.setText(Html.fromHtml(offerBought.getMerchant()));
                tvAddress.setText(offerBought.getAddress().toString());
                tvAddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LatLng position = new LatLng(offerBought.getAddress().getLatitude(), offerBought.getAddress().getLongitude());
                        openNavigator(offerBought.getAddress().toString(), position);
                    }
                });
                ibAddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LatLng position = new LatLng(offerBought.getAddress().getLatitude(), offerBought.getAddress().getLongitude());
                        openNavigator(offerBought.getAddress().toString(), position);
                    }
                });
                if (offerBought.getTelephone() != null && !offerBought.getTelephone().equals("")) {
                    tvTel.setText(getResources().getString(R.string.tel, offerBought.getTelephone()));
                    tvTel.setVisibility(View.VISIBLE);
                    tvTel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            callNumber(offerBought.getTelephone());
                        }
                    });
                    ibTel.setVisibility(View.VISIBLE);
                    ibTel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            callNumber(offerBought.getTelephone());
                        }
                    });
                    llCall.setVisibility(View.VISIBLE);
                    llCall.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            callNumber(offerBought.getTelephone());
                        }
                    });
                } else {
                    tvTel.setVisibility(View.GONE);
                    ibTel.setVisibility(View.GONE);
                    llCall.setVisibility(View.GONE);
                }
                if (offerBought.getEmail() != null && !offerBought.getEmail().equals("")) {
                    tvEmail.setText(getResources().getString(R.string.email, offerBought.getEmail()));
                    tvEmail.setVisibility(View.VISIBLE);
                    tvEmail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sendEmail(offerBought.getEmail());
                        }
                    });
                    ibEmail.setVisibility(View.VISIBLE);
                    ibEmail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sendEmail(offerBought.getEmail());
                        }
                    });
                } else {
                    tvEmail.setVisibility(View.GONE);
                    ibEmail.setVisibility(View.GONE);
                }
                if (offerBought.getWeb_site() != null && !offerBought.getWeb_site().equals("")) {
                    tvSiteWeb.setText(getResources().getString(R.string.siteweb, offerBought.getWeb_site()));
                    tvSiteWeb.setVisibility(View.VISIBLE);
                    tvSiteWeb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openWeb(offerBought.getWeb_site());
                        }
                    });
                    ibSiteWeb.setVisibility(View.VISIBLE);
                    ibSiteWeb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openWeb(offerBought.getWeb_site());
                        }
                    });
                } else {
                    tvSiteWeb.setVisibility(View.GONE);
                    ibSiteWeb.setVisibility(View.GONE);
                }
                tvUserFullname.setText(offerBought.getUser_fullname());
                //tvStatus.setText("Status " + offerBought.getStatus());
                tvCoupon.setText(offerBought.getCoupon().getCode());

                String formattedReservationPrice = new DecimalFormat("##,##0.00€").format(offerBought.getReservation_price());
                String formattedPrice = new DecimalFormat("##,##0.00€").format(offerBought.getPrice());
                String price = getResources().getString(R.string.price_detail, formattedReservationPrice, formattedPrice);
                tvPrice.setText(price);

                String dateFormat = new SimpleDateFormat("dd/MM/yyyy").format(offerBought.getPurchase_date());
                tvDate.setText(dateFormat);

                tvHowDoesItWork.setText(getResources().getString(R.string.how_does_it_work_content_2, offerBought.getTelephone()));

                if(offerBought.getImage_gallery().size() == 1) {
                    hsViewMultiImage.setVisibility(View.GONE);
                    ivOfferSingleImage.setVisibility(View.VISIBLE);

                    Picasso.with(getBaseContext())
                            .load(offerBought.getImage_gallery().get(0))
                            .placeholder(R.drawable.placeholder)
                            .fit()
                            .centerCrop()
                            .into(ivOfferSingleImage);
                } else {
                    hsViewMultiImage.setVisibility(View.VISIBLE);
                    ivOfferSingleImage.setVisibility(View.GONE);
                    for (int i = 0; i < offerBought.getImage_gallery().size(); i++) {
                        ImageView imageView = new ImageView(global.getCurrentActivity());
                        imageView.setId(i);
                        imageView.setPadding(2, 2, 2, 2);

                        Picasso.with(getBaseContext())
                                .load(offerBought.getImage_gallery().get(i))
                                .placeholder(R.drawable.placeholder)
                                .fit()
                                .centerCrop()
                                .into(imageView);
                        //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        llGallery.addView(imageView);
                    }
                }

                if (map != null) {
                    configureMap(map, offerBought.getAddress().getLatitude(), offerBought.getAddress().getLongitude());
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
}
