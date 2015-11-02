package com.xpeppers.trentinolocal.details;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
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
    private TextView tvTel;
    private TextView tvEmail;
    private TextView tvSiteWeb;
    private TextView tvUserFullname;
    private TextView tvStatus;
    private TextView tvCoupon;
    private TextView tvPrice;
    private TextView tvDate;
    private LinearLayout llGallery;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_offer_bought);

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
        tvTel = (TextView) findViewById(R.id.tvTel);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvSiteWeb = (TextView) findViewById(R.id.tvSiteWeb);
        tvUserFullname = (TextView) findViewById(R.id.tvUserFullname);
        tvStatus = (TextView) findViewById(R.id.tvStatus);
        tvCoupon = (TextView) findViewById(R.id.tvCoupon);
        tvPrice = (TextView) findViewById(R.id.tvPrice);
        tvDate = (TextView) findViewById(R.id.tvDate);

        llGallery = (LinearLayout) findViewById(R.id.llGallery);

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
                OfferBought offerBought = (OfferBought) result;
                asyncResponse(offerBought);
            }

            @Override
            public void onFailure(Throwable caught) {
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
                tvMerchant.setText(offerBought.getMerchant());
                tvAddress.setText(offerBought.getAddress().toString());
                if(offerBought.getTelephone() != null && !offerBought.getTelephone().equals("")) {
                    tvTel.setText("Tel " + offerBought.getTelephone());
                    tvTel.setVisibility(View.VISIBLE);
                } else {
                    tvTel.setVisibility(View.GONE);
                }
                if(offerBought.getEmail() != null && !offerBought.getEmail().equals("")) {
                    tvEmail.setText("Email " + offerBought.getEmail());
                    tvEmail.setVisibility(View.VISIBLE);
                } else {
                    tvEmail.setVisibility(View.GONE);
                }
                if(offerBought.getWeb_site() != null && !offerBought.getWeb_site().equals("")) {
                    tvSiteWeb.setText("SiteWeb " + offerBought.getWeb_site());
                    tvSiteWeb.setVisibility(View.VISIBLE);
                } else {
                    tvSiteWeb.setVisibility(View.GONE);
                }
                tvUserFullname.setText("Nome " + offerBought.getUser_fullname());
                tvStatus.setText("Status " + offerBought.getStatus());
                tvCoupon.setText("Coupon " + offerBought.getCoupon().getCode());

                String formattedPrice = new DecimalFormat("##,##0.00€").format(offerBought.getPrice());
                tvPrice.setText("Costo " + formattedPrice);

                String dateFormat = new SimpleDateFormat("dd/MM/yyyy").format(offerBought.getPurchase_date());
                tvDate.setText("Acquistato il " + dateFormat);

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

                if (map != null) {
                    LatLng position = new LatLng(offerBought.getAddress().getLatitude(), offerBought.getAddress().getLongitude());
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
                    map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
                }
            }
        });
    }
}
