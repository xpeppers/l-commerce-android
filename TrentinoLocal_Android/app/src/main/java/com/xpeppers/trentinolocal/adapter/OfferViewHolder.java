package com.xpeppers.trentinolocal.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xpeppers.trentinolocal.R;
import com.xpeppers.trentinolocal.details.OfferDetailActivity;

/**
 * @author Emilio Frusciante - FEj (efrusciante AT wish-op DOT com)
 * @since 2015/10/29
 */
public class OfferViewHolder extends RecyclerView.ViewHolder {
    private long offerId = 0;
    private ImageView ivOffer;
    private TextView tvTitle;
    private TextView tvPrice;

    OfferViewHolder(View itemView) {
        super(itemView);
        ivOffer = (ImageView) itemView.findViewById(R.id.ivOffer);
        tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detail = new Intent(v.getContext(), OfferDetailActivity.class);
                detail.putExtra(OfferDetailActivity.EXTRA_OFFER_ID, offerId);
                v.getContext().startActivity(detail);
            }
        });
    }

    public long getOfferId() {
        return offerId;
    }

    public void setOfferId(long offerId) {
        this.offerId = offerId;
    }

    public ImageView getIvOffer() {
        return ivOffer;
    }

    public void setIvOffer(ImageView ivOffer) {
        this.ivOffer = ivOffer;
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public void setTvTitle(TextView tvTitle) {
        this.tvTitle = tvTitle;
    }

    public TextView getTvPrice() {
        return tvPrice;
    }

    public void setTvPrice(TextView tvPrice) {
        this.tvPrice = tvPrice;
    }
}
