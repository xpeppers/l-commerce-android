package com.xpeppers.trentinolocal.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xpeppers.trentinolocal.R;
import com.xpeppers.trentinolocal.details.OfferBoughtDetailActivity;

/**
 * @author Emilio Frusciante - FEj (efrusciante AT wish-op DOT com)
 * @since 2015/10/29
 */
public class OfferBoughtViewHolder extends RecyclerView.ViewHolder {
    private long offerBoughtId = 0;
    private ImageView ivOfferBought;
    private TextView tvTitle;
    private TextView tvStatus;

    OfferBoughtViewHolder(View itemView) {
        super(itemView);
        ivOfferBought = (ImageView) itemView.findViewById(R.id.ivOfferBought);
        tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        tvStatus = (TextView) itemView.findViewById(R.id.tvStatus);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detail = new Intent(v.getContext(), OfferBoughtDetailActivity.class);
                detail.putExtra(OfferBoughtDetailActivity.EXTRA_OFFER_BOUGHT_ID, offerBoughtId);
                v.getContext().startActivity(detail);
            }
        });
    }

    public long getOfferBoughtId() {
        return offerBoughtId;
    }

    public void setOfferBoughtId(long offerBoughtId) {
        this.offerBoughtId = offerBoughtId;
    }

    public ImageView getIvOfferBought() {
        return ivOfferBought;
    }

    public void setIvOfferBought(ImageView ivOfferBought) {
        this.ivOfferBought = ivOfferBought;
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public void setTvTitle(TextView tvTitle) {
        this.tvTitle = tvTitle;
    }

    public TextView getTvStatus() {
        return tvStatus;
    }

    public void setTvStatus(TextView tvStatus) {
        this.tvStatus = tvStatus;
    }
}
