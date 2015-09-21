package com.xpeppers.trentinolocal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xpeppers.servicelib.bean.Itinerary;
import com.xpeppers.trentinolocal.R;

import java.util.List;

/**
 * @author Emilio Frusciante - FEj (efrusciante AT wish-op DOT com)
 * @since 07/04/15
 */
public class ItineraryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Itinerary> itineraries;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_HEADER = 2;

    public ItineraryAdapter(Context context, List<Itinerary> itineraries) {
        this.context = context;
        this.itineraries = itineraries;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        if(viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(context).inflate(R.layout.card_header, parent, false);
            return new RecyclerHeaderViewHolder(view);

        } else if(viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.card_itinerary, parent, false);
            return new ItineraryViewHolder(view);
        }

        throw new RuntimeException("There is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if(!isPositionHeader(position)) {
            ItineraryViewHolder itineraryViewHolder = (ItineraryViewHolder) viewHolder;
            int i = position -1;
            itineraryViewHolder.itineraryId = itineraries.get(i).getId();
            itineraryViewHolder.tvTitle.setText(itineraries.get(i).getTitle());

            // show The Image
            //new DownloadImage(itineraryViewHolder.ivItinerary).execute(itineraries.get(i).getImage());
            Picasso.with(context)
                    .load(itineraries.get(i).getImage())
                    .placeholder(R.drawable.placeholder)
                    .into(itineraryViewHolder.ivItinerary);
        }
    }

    public int getBasicItemCount() {
        return itineraries == null ? 0 : itineraries.size();
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

    public void updateData(List<Itinerary> itineraries) {
        this.itineraries = itineraries;
    }

    public static class ItineraryViewHolder extends RecyclerView.ViewHolder {
        int itineraryId = 0;
        ImageView ivItinerary;
        TextView tvTitle;

        ItineraryViewHolder(View itemView) {
            super(itemView);
            ivItinerary = (ImageView) itemView.findViewById(R.id.ivItinerary);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Intent detail = new Intent(v.getContext(), ItineraryDetailActivity.class);
                    //detail.putExtra(ItineraryDetailActivity.EXTRA_ITINERARY_ID, itineraryId);
                    //v.getContext().startActivity(detail);
                }
            });
        }
    }
}
