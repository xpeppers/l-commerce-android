package com.xpeppers.trentinolocal.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xpeppers.servicelib.bean.Offer;
import com.xpeppers.trentinolocal.R;
import com.xpeppers.trentinolocal.details.OfferDetailActivity;

import java.text.DecimalFormat;
import java.util.List;

/**
 * @author Emilio Frusciante - FEj (efrusciante AT wish-op DOT com)
 * @since 07/04/15
 */
public class OfferAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Offer> offers;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_HEADER = 2;

    public OfferAdapter(Context context, List<Offer> offers) {
        this.context = context;
        this.offers = offers;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        if(viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(context).inflate(R.layout.card_header, parent, false);
            return new RecyclerHeaderViewHolder(view);

        } else if(viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.card_offer, parent, false);
            return new OfferViewHolder(view);
        }

        throw new RuntimeException("There is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if(!isPositionHeader(position)) {
            OfferViewHolder offerViewHolder = (OfferViewHolder) viewHolder;
            int i = position -1;
            offerViewHolder.offerId = offers.get(i).getId();
            offerViewHolder.tvTitle.setText(offers.get(i).getTitle());

            //String location = offers.get(i).getAddress().getStreet() + ", " + offers.get(i).getAddress().getCity() + " - " + offers.get(i).getAddress().getZip_code();
            //offerViewHolder.tvLocation.setText(location);

            DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
            String price = decimalFormat.format(offers.get(i).getOriginal_price());
            offerViewHolder.tvPrice.setText("€ " + price);

            // show The Image
            //new DownloadImage(offerViewHolder.ivOffer).execute(offers.get(i).getImage());
            Picasso.with(context)
                    .load(offers.get(i).getImage_url())
                    .placeholder(R.drawable.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                placeholder)
                    .into(offerViewHolder.ivOffer);
        }
    }

    public int getBasicItemCount() {
        return offers == null ? 0 : offers.size();
    }

    @Override
    public int getItemCount() {
        return getBasicItemCount() + 1; // header
    }

    //added a method that returns viewType for a given position
    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    //added a method to check if given position is a header
    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    public void updateData(List<Offer> offers) {
        this.offers = offers;
    }

    public static class OfferViewHolder extends RecyclerView.ViewHolder {
        int offerId = 0;
        ImageView ivOffer;
        TextView tvTitle;
        TextView tvPrice;
        TextView tvLocation;

        OfferViewHolder(View itemView) {
            super(itemView);
            ivOffer = (ImageView) itemView.findViewById(R.id.ivOffer);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            tvLocation = (TextView) itemView.findViewById(R.id.tvLocation);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent detail = new Intent(v.getContext(), OfferDetailActivity.class);
                    detail.putExtra(OfferDetailActivity.EXTRA_OFFER_ID, offerId);
                    v.getContext().startActivity(detail);
                }
            });
        }
    }
}
