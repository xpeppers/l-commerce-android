package com.xpeppers.trentinolocal.main;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.xpeppers.servicelib.bean.Offer;
import com.xpeppers.servicelib.services.OffersService;
import com.xpeppers.servicelib.utils.CallBack;
import com.xpeppers.trentinolocal.BaseFragment;
import com.xpeppers.trentinolocal.R;
import com.xpeppers.trentinolocal.adapter.ItineraryAdapter;
import com.xpeppers.trentinolocal.adapter.OfferAdapter;
import com.xpeppers.trentinolocal.adapter.OrderAdapter;

import java.util.List;

/**
 * @author Emilio Frusciante - FEj (efrusciante AT wish-op DOT com)
 * @since 07/14/15
 */
public class MainFragment extends BaseFragment implements ObservableScrollViewCallbacks {
    public enum RemoteDataType {OFFER, ITINERARY, RESERVATION, PROFILE};

    public static final String ARG_PAGE_NUMBER = "page_number";

    private int mPageNumber;

    private SwipeRefreshLayout swipeRefreshLayout = null;
    private ObservableRecyclerView recyclerView = null;
    private OfferAdapter offerAdapter = null;
    private ItineraryAdapter itineraryAdapter = null;
    private OrderAdapter orderAdapter = null;

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
                rootView = (ViewGroup) inflater
                        .inflate(R.layout.fragment_offers, container, false);

                loadCardList(rootView, RemoteDataType.OFFER);
                break;
            case 2:
                rootView = (ViewGroup) inflater
                        .inflate(R.layout.fragment_reservations, container, false);

                loadCardList(rootView, RemoteDataType.RESERVATION);
                break;
            case 3:
            case 0:
            default:
                rootView = (ViewGroup) inflater
                        .inflate(R.layout.fragment_itinerary, container, false);
                loadCardList(rootView, RemoteDataType.ITINERARY);
        }
        return rootView;
    }


    @Override
    public void onDestroy() {
        // DevicePairingPageFragment.this.getActivity().unregisterReceiver(mReceiver);
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

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        if (getActivity() != null && getActivity() instanceof ObservableScrollViewCallbacks) {
            ((ObservableScrollViewCallbacks) getActivity()).onScrollChanged(scrollY, firstScroll, dragging);
        }
    }

    @Override
    public void onDownMotionEvent() {
        if (getActivity() != null && getActivity() instanceof ObservableScrollViewCallbacks) {
            ((ObservableScrollViewCallbacks) getActivity()).onDownMotionEvent();
        }
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        if (getActivity() != null && getActivity() instanceof ObservableScrollViewCallbacks) {
            ((ObservableScrollViewCallbacks) getActivity()).onUpOrCancelMotionEvent(scrollState);
        }
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
                    /*
                    if (args != null && args.containsKey(ARG_SCROLL_Y)) {
                        final int scrollY = args.getInt(ARG_SCROLL_Y, 0);
                        ScrollUtils.addOnGlobalLayoutListener(recyclerView, new Runnable() {
                            @Override
                            public void run() {
                                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                                layoutParams.setMargins(0, scrollY, 0, 0);
                                recyclerView.setLayoutParams(layoutParams);
                            }
                        });
                    }
                    */
            // TouchInterceptionViewGroup should be a parent view other than ViewPager.
            // This is a workaround for the issue #117:
            // https://github.com/ksoichiro/Android-ObservableScrollView/issues/117
            recyclerView.setTouchInterceptionViewGroup((ViewGroup) parentActivity.findViewById(R.id.root));
        }
        recyclerView.setScrollViewCallbacks(this);

        loadData(remoteDataType, false);
    }

    private void loadData(RemoteDataType remoteDataType, boolean force) {
        if(force || !checkSetDataType(remoteDataType)) {
            switch (remoteDataType) {
                case ITINERARY:
                    /*
                    ItinerariesService.getInstance(global.getApplicationContext()).getItineraries(new CallBack() {
                        @Override
                        public void onSuccess(Object result) {
                            asyncResponse(RemoteDataType.ITINERARY, result);
                        }

                        @Override
                        public void onFailure(Throwable caught) {
                            asyncDialog("Error", caught.getMessage());
                        }
                    });
                    */
                    break;
                case OFFER:
                    OffersService.getInstance(global.getApplicationContext()).getAll(new CallBack() {
                        @Override
                        public void onSuccess(Object result) {
                            asyncResponse(RemoteDataType.OFFER, result);
                        }

                        @Override
                        public void onFailure(Throwable caught) {
                            asyncDialog("Error", caught.getMessage());
                        }
                    });
                    break;
                case RESERVATION:
                    /*
                    OrdersService.getInstance(global.getApplicationContext()).getOrders(userId, apiKey, new CallBack() {
                        @Override
                        public void onSuccess(Object result) {
                            asyncResponse(RemoteDataType.RESERVATION, result);
                        }

                        @Override
                        public void onFailure(Throwable caught) {
                            asyncDialog("Error", caught.getMessage());
                        }
                    });
                    */
                    break;
            }
        } else {
            refreshCard(remoteDataType);
        }
    }

    private boolean checkSetDataType(RemoteDataType remoteDataType) {
        boolean state = false;

        if((remoteDataType == RemoteDataType.OFFER && global.getOffers().size() > 0) // ||
                //(remoteDataType == RemoteDataType.ITINERARY && global.getItineraries().size() > 0)||
                //(remoteDataType == RemoteDataType.RESERVATION && global.getReservations().size() > 0)
        )
            state = true;

        return state;
    }

    private void asyncResponse(RemoteDataType remoteDataType,  Object results) {
        switch (remoteDataType) {
            case OFFER:
                List<Offer> offers = (List<Offer>) results;
                //List<Offer> fakeOffers = new ArrayList<>();
                //fakeOffers.addAll(offers);
                //fakeOffers.addAll(offers);
                //fakeOffers.addAll(offers);
                global.setOffers(offers);
                break;
            case ITINERARY:
                //List<Itinerary> itineraries = (List<Itinerary>) results;
                //global.setItineraries(itineraries);
                break;
            case RESERVATION:
                //List<Order> reservations = (List<Order>) results;
                //global.setReservations(reservations);
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

                        if (offerAdapter != null) {
                            offerAdapter.updateData(global.getOffers());
                        } else {
                            offerAdapter = new OfferAdapter(getActivity().getBaseContext(), global.getOffers());
                            recyclerView.setAdapter(offerAdapter);
                        }
                        offerAdapter.notifyDataSetChanged();
                    }
                });
                break;
            case ITINERARY:
                /*
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);

                        if (itineraryAdapter != null) {
                            itineraryAdapter.updateData(global.getItineraries());
                        } else {
                            itineraryAdapter = new ItineraryAdapter(getActivity().getBaseContext(), global.getItineraries());
                            recyclerView.setAdapter(itineraryAdapter);
                        }
                        itineraryAdapter.notifyDataSetChanged();
                    }
                });
                */
                break;
            case RESERVATION:
                /*
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);

                        if (orderAdapter != null) {
                            orderAdapter.updateData(global.getReservations());
                        } else {
                            orderAdapter = new OrderAdapter(getActivity().getBaseContext(), global.getReservations());
                            recyclerView.setAdapter(orderAdapter);
                        }
                        orderAdapter.notifyDataSetChanged();
                    }
                });
                */
                break;
        }
    }
}
