package com.example.testebs.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.testebs.Model.Product;
import com.example.testebs.R;

import java.util.ArrayList;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.MyViewHolder> {
    private ArrayList<Product> products;
    private LayoutInflater layoutInflater;
    Context context;
    String currency = "$";

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void OnItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public ProductsAdapter(Context context, ArrayList<Product> products) {
        layoutInflater = LayoutInflater.from(context);
        this.products = products;
        this.context = context;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.card_product, parent, false);
        return new MyViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(final ProductsAdapter.MyViewHolder holder, int position) {
        if (!TextUtils.isEmpty(products.get(position).getTitle())) {
            String cropName = products.get(position).getTitle().substring(0, 1).toUpperCase() + products.get(position).getTitle().substring(1);
            holder.title.setText(cropName);
        }
        if (!TextUtils.isEmpty(products.get(position).getShort_description())) {
            if (products.get(position).getShort_description().length() > 40) {
                holder.shortDescription.setText(products.get(position).getShort_description().substring(0, 39) + "...");
            } else {
                holder.shortDescription.setText(products.get(position).getShort_description());
            }
        }
        if (products.get(position).getPrice() != null) {
            holder.price.setText(products.get(position).getPrice().toString() + currency);
        }
        if (products.get(position).getSale_precent() != null) {
            if (products.get(position).getSale_precent() == 0) {
                holder.salePercent.setVisibility(View.GONE);
                holder.price.setPaintFlags(holder.price.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                holder.price.setTextColor(Color.parseColor("#198ff2"));
                holder.price.setTextSize(18);
                holder.sale.setVisibility(View.GONE);
            } else {
                holder.salePercent.setVisibility(View.VISIBLE);
                holder.price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                holder.price.setTextColor(Color.parseColor("#c7c7c7"));
                holder.price.setTextSize(13);
                holder.sale.setVisibility(View.VISIBLE);
                holder.sale.setText("Sale " + products.get(position).getSale_precent() + "%");
                holder.sale.setTextSize(14);
            }
            int salePrice = (100 - products.get(position).getSale_precent()) * products.get(position).getPrice() / 100;
            holder.salePercent.setText(salePrice + currency);
        }


        if (products.get(position).getImage() != null && !products.get(position).getImage().equals("")) {

            Glide.with(context)
                    .load(products.get(position).getImage())
                    .apply(
                            new RequestOptions()
                                    .placeholder(R.drawable.image_not_found)
                                    .error(R.drawable.image_not_found)
                                    .centerCrop()
                    )
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            holder.image.setVisibility(View.VISIBLE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            holder.image.setVisibility(View.VISIBLE);
                            return false;
                        }
                    })
                    .transition(withCrossFade())
                    .into(holder.image);
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, shortDescription, price, salePercent, sale;
        ImageView image;

        MyViewHolder(View itemView, final ProductsAdapter.OnItemClickListener listener) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            shortDescription = itemView.findViewById(R.id.shortDescription);
            price = itemView.findViewById(R.id.price);
            image = itemView.findViewById(R.id.image);
            salePercent = itemView.findViewById(R.id.sale_percent);
            sale = itemView.findViewById(R.id.sale);
            itemView.setTag(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.OnItemClick(position);
                        }
                    }

                }
            });
        }
    }
}
