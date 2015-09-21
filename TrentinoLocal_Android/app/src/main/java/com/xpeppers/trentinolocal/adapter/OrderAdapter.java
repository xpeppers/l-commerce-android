package com.xpeppers.trentinolocal.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xpeppers.servicelib.bean.Order;
import com.xpeppers.trentinolocal.R;
import com.xpeppers.trentinolocal.details.OrderDetailActivity;

import java.util.List;

/**
 * @author Emilio Frusciante - FEj (efrusciante AT wish-op DOT com)
 * @since 07/04/15
 */
public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Order> orders;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_HEADER = 2;

    public OrderAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.orders = orders;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        if(viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(context).inflate(R.layout.card_header, parent, false);
            return new RecyclerHeaderViewHolder(view);

        } else if(viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.card_order, parent, false);
            return new OrderViewHolder(view);
        }

        throw new RuntimeException("There is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if(!isPositionHeader(position)) {
            OrderViewHolder orderViewHolder = (OrderViewHolder) viewHolder;
            int i = position -1;
            orderViewHolder.orderId = orders.get(i).getId();
            //orderViewHolder.tvTitle.setText(orders.get(i).getTitle());
            orderViewHolder.tvStatus.setText(orders.get(i).getStatus());

            // show The Image
            //new DownloadImage(itineraryViewHolder.ivItinerary).execute(itineraries.get(i).getImage());
            /*Picasso.with(context)
                    .load(orders.get(i).getImage())
                    .placeholder(R.drawable.placeholder)
                    .into(orderViewHolder.ivOrder);
            */
        }
    }

    public int getBasicItemCount() {
        return orders == null ? 0 : orders.size();
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

    public void updateData(List<Order> orders) {
        this.orders = orders;
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        double orderId = 0;
        ImageView ivOrder;
        TextView tvTitle;
        TextView tvStatus;

        OrderViewHolder(View itemView) {
            super(itemView);
            ivOrder = (ImageView) itemView.findViewById(R.id.ivOrder);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvStatus = (TextView) itemView.findViewById(R.id.tvStatus);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent detail = new Intent(v.getContext(), OrderDetailActivity.class);
                    detail.putExtra(OrderDetailActivity.EXTRA_ORDER_ID, orderId);
                    v.getContext().startActivity(detail);
                }
            });
        }
    }
}
