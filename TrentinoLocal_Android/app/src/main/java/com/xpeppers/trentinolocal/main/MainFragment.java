package com.xpeppers.trentinolocal.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.mikepenz.materialdrawer.view.BezelImageView;
import com.rey.material.widget.Button;
import com.squareup.picasso.Picasso;
import com.xpeppers.servicelib.bean.Offer;
import com.xpeppers.servicelib.bean.OfferBought;
import com.xpeppers.servicelib.bean.User;
import com.xpeppers.servicelib.services.OffersBoughtService;
import com.xpeppers.servicelib.services.OffersService;
import com.xpeppers.servicelib.services.UsersService;
import com.xpeppers.servicelib.utils.CallBack;
import com.xpeppers.trentinolocal.BaseFragment;
import com.xpeppers.trentinolocal.R;
import com.xpeppers.trentinolocal.adapter.OfferAdapter;
import com.xpeppers.trentinolocal.adapter.OfferBoughtAdapter;
import com.xpeppers.trentinolocal.details.StaticPageActivity;

import java.util.List;

/**
 * @author Emilio Frusciante - FEj (efrusciante AT wish-op DOT com)
 * @since 07/14/15
 */
public class MainFragment extends BaseFragment {
    public enum RemoteDataType {OFFER, OFFERBOUGHT, PROFILE};

    public static final String ARG_PAGE_NUMBER = "page_number";

    private int mPageNumber;

    private SwipeRefreshLayout swipeRefreshLayout = null;
    private ObservableRecyclerView recyclerView = null;
    private OfferAdapter offerAdapter = null;
    private OfferBoughtAdapter offerBoughtAdapter = null;

    public static MainFragment create(int pageNumber) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_NUMBER, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public MainFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retain this fragment across configuration changes.
        setRetainInstance(true);

        mPageNumber = getArguments().getInt(ARG_PAGE_NUMBER, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final ViewGroup rootView;
        switch (mPageNumber) {
            case 1:
                if(global.isApiAuthenticated() && global.isFacebookLogin()) {
                    rootView = (ViewGroup) inflater
                            .inflate(R.layout.fragment_offer_boughts, container, false);
                    loadCardList(rootView, RemoteDataType.OFFERBOUGHT);
                } else {
                    rootView = (ViewGroup) inflater
                            .inflate(R.layout.fragment_empty, container, false);
                }
                break;
            case 2:
            case 3:
                if(mPageNumber == 2 && global.getReseller() != null && global.getReseller().getCustom_url() != null && !global.getReseller().getCustom_url().equals("")) {
                    rootView = (ViewGroup) inflater
                            .inflate(R.layout.fragment_webview, container, false);
                    WebView webView = (WebView) rootView.findViewById(R.id.webView);
                    final ProgressBar pbWebviewSpinner = (ProgressBar) rootView.findViewById(R.id.pbWebviewSpinner);
                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.loadUrl(global.getReseller().getCustom_url());
                    webView.setWebChromeClient(new WebChromeClient() {
                        public void onProgressChanged(WebView view, int progress) {
                            if (progress < 100 && pbWebviewSpinner.getVisibility() == ProgressBar.GONE) {
                                pbWebviewSpinner.setVisibility(ProgressBar.VISIBLE);
                            }
                            pbWebviewSpinner.setProgress(progress);
                            if (progress == 100) {
                                pbWebviewSpinner.setVisibility(ProgressBar.GONE);
                            }
                        }
                    });
                } else {
                    if (global.isApiAuthenticated() && global.isFacebookLogin()) {
                        rootView = (ViewGroup) inflater
                                .inflate(R.layout.fragment_profile, container, false);
                        loadProfile(rootView);
                        /*
                        TextView tvHow = (TextView) rootView.findViewById(R.id.tvHow);
                        if(global.getReseller() != null && global.getReseller().getHow_it_works() != null && !global.getReseller().getHow_it_works().equals("")) {
                            tvHow.setVisibility(View.VISIBLE);

                            tvHow.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent staticPage = new Intent(v.getContext(), StaticPageActivity.class);
                                    staticPage.putExtra(StaticPageActivity.EXTRA_TITLE, rootView.getResources().getString(R.string.how));
                                    staticPage.putExtra(StaticPageActivity.EXTRA_BODY, global.getReseller().getHow_it_works());
                                    v.getContext().startActivity(staticPage);
                                }
                            });
                        } else {
                            tvHow.setVisibility(View.GONE);
                        }

                        TextView tvSupport = (TextView) rootView.findViewById(R.id.tvSupport);
                        if(global.getReseller() != null && global.getReseller().getSupport() != null && !global.getReseller().getSupport().equals("")) {
                            tvSupport.setVisibility(View.VISIBLE);

                            tvSupport.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent staticPage = new Intent(v.getContext(), StaticPageActivity.class);
                                    staticPage.putExtra(StaticPageActivity.EXTRA_TITLE, rootView.getResources().getString(R.string.support));
                                    staticPage.putExtra(StaticPageActivity.EXTRA_BODY, global.getReseller().getSupport());
                                    v.getContext().startActivity(staticPage);
                                }
                            });
                        } else {
                            tvSupport.setVisibility(View.GONE);
                        }

                        */

                        Button bHow = (Button) rootView.findViewById(R.id.bHow);
                        if (global.getReseller() != null && global.getReseller().getHow_it_works() != null && !global.getReseller().getHow_it_works().equals("")) {
                            bHow.setVisibility(View.VISIBLE);

                            bHow.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent staticPage = new Intent(v.getContext(), StaticPageActivity.class);
                                    staticPage.putExtra(StaticPageActivity.EXTRA_TITLE, rootView.getResources().getString(R.string.how));
                                    staticPage.putExtra(StaticPageActivity.EXTRA_BODY, global.getReseller().getHow_it_works());
                                    v.getContext().startActivity(staticPage);
                                }
                            });
                        } else {
                            bHow.setVisibility(View.GONE);
                        }

                        Button bSupport = (Button) rootView.findViewById(R.id.bSupport);
                        if (global.getReseller() != null && global.getReseller().getSupport() != null && !global.getReseller().getSupport().equals("")) {
                            bSupport.setVisibility(View.VISIBLE);

                            bSupport.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent staticPage = new Intent(v.getContext(), StaticPageActivity.class);
                                    staticPage.putExtra(StaticPageActivity.EXTRA_TITLE, rootView.getResources().getString(R.string.support));
                                    staticPage.putExtra(StaticPageActivity.EXTRA_BODY, global.getReseller().getSupport());
                                    v.getContext().startActivity(staticPage);
                                }
                            });
                        } else {
                            bSupport.setVisibility(View.GONE);
                        }

                        Button bLogout = (Button) rootView.findViewById(R.id.bLogout);
                        bLogout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                LoginManager.getInstance().logOut();
                                ((MainActivity) getActivity()).selectCurrentPage(0);
                            }
                        });

                    } else {
                        rootView = (ViewGroup) inflater
                                .inflate(R.layout.fragment_empty, container, false);
                    }
                }
                break;
            case 0:
            default:
                rootView = (ViewGroup) inflater
                        .inflate(R.layout.fragment_offers, container, false);
                loadCardList(rootView, RemoteDataType.OFFER);
        }
        return rootView;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public int getPageNumber() {
        return mPageNumber;
    }

    private void loadCardList(ViewGroup rootView, final RemoteDataType remoteDataType) {
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData(remoteDataType, true);
            }
        });

        Activity parentActivity = getActivity();
        recyclerView = (ObservableRecyclerView) rootView.findViewById(R.id.cardList);
        recyclerView.setLayoutManager(new LinearLayoutManager(parentActivity));
        recyclerView.setHasFixedSize(true);
        if (parentActivity instanceof ObservableScrollViewCallbacks) {
            recyclerView.setTouchInterceptionViewGroup((ViewGroup) parentActivity.findViewById(R.id.root));
        }

        loadData(remoteDataType, false);
    }

    private void loadProfile(ViewGroup rootView) {
        if(checkSetDataType(RemoteDataType.PROFILE))
            populateProfile(rootView);
        else
            loadData(RemoteDataType.PROFILE, false, rootView);
    }

    private void loadData(RemoteDataType remoteDataType, boolean force) {
        loadData(remoteDataType, force, null);
    }

    private void loadData(RemoteDataType remoteDataType, boolean force, final ViewGroup view) {
        if(force || !checkSetDataType(remoteDataType)) {
            switch (remoteDataType) {
                case OFFER:
                    OffersService.getInstance(global.getApplicationContext()).getAll(new CallBack() {
                        @Override
                        public void onSuccess(Object results) {
                            asyncResponse(RemoteDataType.OFFER, results);
                        }

                        @Override
                        public void onFailure(Throwable caught) {
                            asyncDialog("Error", caught.getMessage());
                        }
                    });
                    break;
                case OFFERBOUGHT:
                    OffersBoughtService.getInstance(global.getApplicationContext()).getAll(global.getApiToken(), new CallBack() {
                        @Override
                        public void onSuccess(Object results) {
                            asyncResponse(RemoteDataType.OFFERBOUGHT, results);
                        }

                        @Override
                        public void onFailure(Throwable caught) {
                            asyncDialog("Error", caught.getMessage());
                        }
                    });
                    break;
                case PROFILE:
                    UsersService.getInstance(global.getApplicationContext()).getProfile(global.getApiToken(), new CallBack() {
                        @Override
                        public void onSuccess(Object results) {
                            asyncResponse(RemoteDataType.PROFILE, results, view);
                        }

                        @Override
                        public void onFailure(Throwable caught) {
                            asyncDialog("Error", caught.getMessage());
                        }
                    });
                    break;
            }
        } else {
            refreshCard(remoteDataType);
        }
    }

    private boolean checkSetDataType(RemoteDataType remoteDataType) {
        boolean state = false;

        if((remoteDataType == RemoteDataType.OFFER && global.getOffers() != null && global.getOffers().size() > 0)
                || (remoteDataType == RemoteDataType.OFFERBOUGHT && global.getOfferBoughts() != null && global.getOfferBoughts().size() > 0)
                || (remoteDataType == RemoteDataType.PROFILE && global.getUser() != null)
        )
            state = true;

        return state;
    }

    private void asyncResponse(RemoteDataType remoteDataType, Object results) {
        asyncResponse(remoteDataType, results, null);
    }

    private void asyncResponse(RemoteDataType remoteDataType, Object results, ViewGroup view) {
        switch (remoteDataType) {
            case OFFER:
                List<Offer> offers = (List<Offer>) results;
                global.setOffers(offers);
                break;
            case OFFERBOUGHT:
                List<OfferBought> offerBoughts = (List<OfferBought>) results;
                global.setOfferBoughts(offerBoughts);
                break;
            case PROFILE:
                User user = (User) results;
                global.setUser(user);
                populateProfile(view);
                break;
        }

        refreshCard(remoteDataType);
    }

    private void refreshCard(RemoteDataType remoteDataType) {
        switch (remoteDataType) {
            case OFFER:
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);

                        if (offerAdapter == null) {
                            offerAdapter = new OfferAdapter(global.getApplicationContext());
                            recyclerView.setAdapter(offerAdapter);
                        }
                        offerAdapter.notifyDataSetChanged();
                    }
                });
                break;
            case OFFERBOUGHT:
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);

                        if (offerBoughtAdapter == null) {
                            offerBoughtAdapter = new OfferBoughtAdapter(global.getApplicationContext());
                            recyclerView.setAdapter(offerBoughtAdapter);
                        }
                        offerBoughtAdapter.notifyDataSetChanged();
                    }
                });
                break;
        }
    }

    private void populateProfile(final ViewGroup view) {
        if(view != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    BezelImageView bivProfileImage = (BezelImageView) view.findViewById(R.id.bivProfileImage);
                    Picasso.with(getActivity().getApplicationContext())
                            .load(global.getUser().getPicture_url())
                            .placeholder(R.drawable.placeholder)
                            .into(bivProfileImage);
                    TextView tvProfileName = (TextView) view.findViewById(R.id.tvProfileName);
                    tvProfileName.setText(global.getUser().getFull_name());
                    TextView tvProfileEmail = (TextView) view.findViewById(R.id.tvProfileEmail);
                    tvProfileEmail.setText(global.getUserEmailByFacebook());
                }
            });
        }
    }
}
