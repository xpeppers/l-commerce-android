package com.xpeppers.trentinolocal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.xpeppers.trentinolocal.Global;
import com.xpeppers.trentinolocal.R;

/**
 * @author Emilio Frusciante - FEj (efrusciante AT wish-op DOT com)
 * @since 07/04/15
 */
public class OfferBoughtAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private Global global;

    public OfferBoughtAdapter(Context context) {
        this.context = context;
        this.global = (Global) context.getApplicationContext();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.card_offer_bought, parent, false);
        return new OfferBoughtViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        OfferBoughtViewHolder offerBoughtViewHolder = (OfferBoughtViewHolder) viewHolder;
        int i = position;

        offerBoughtViewHolder.setOfferBoughtId(global.getOfferBoughts().get(i).getId());
        offerBoughtViewHolder.getTvTitle().setText(global.getOfferBoughts().get(i).getTitle());
        offerBoughtViewHolder.getTvStatus().setText(global.getOfferBoughts().get(i).getStatus());

        // show The Image
        Picasso.with(context)
                .load(global.getOfferBoughts().get(i).getImage_url())
                .placeholder(R.drawable.placeholder)
                .fit()
                .centerCrop()
                .into(offerBoughtViewHolder.getIvOfferBought());
    }

    @Override
    public int getItemCount() {
        int size = global.getOfferBoughts() == null ? 0 : global.getOfferBoughts().size();
        if(size != 0 && global.getOfferBoughts().get(global.getOfferBoughts().size()-1) == null)
            size -= 1;

        return size;
    }
}
