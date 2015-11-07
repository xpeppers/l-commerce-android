package com.xpeppers.trentinolocal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.xpeppers.trentinolocal.Global;
import com.xpeppers.trentinolocal.R;

import java.text.DecimalFormat;

/**
 * @author Emilio Frusciante - FEj (efrusciante AT wish-op DOT com)
 * @since 07/04/15
 */
public class OfferAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private Global global;

    public OfferAdapter(Context context) {
        this.context = context;
        this.global = (Global) context.getApplicationContext();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.card_offer, parent, false);
        return new OfferViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        OfferViewHolder offerViewHolder = (OfferViewHolder) viewHolder;

        int i = position;
        offerViewHolder.setOfferId(global.getOffers().get(i).getId());
        offerViewHolder.getTvTitle().setText(global.getOffers().get(i).getTitle());
        String formattedPrice = new DecimalFormat("##,##0.00€").format(global.getOffers().get(i).getPrice());
        offerViewHolder.getTvPrice().setText(formattedPrice);

        // show The Image
        Picasso.with(context)
                .load(global.getOffers().get(i).getImage_url())
                .placeholder(R.drawable.placeholder)
                .fit()
                .centerCrop()
                .into(offerViewHolder.getIvOffer());
    }

    @Override
    public int getItemCount() {
        int size = global.getOffers() == null ? 0 : global.getOffers().size();
        if(size != 0 && global.getOffers().get(global.getOffers().size()-1) == null)
            size -= 1;

        return size;
    }
}
