package com.mak.app.mvpdemo.common.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.mak.app.mvpdemo.R;
import com.mak.app.mvpdemo.apicalls.commonmodels.ImageLinks;
import com.mak.app.mvpdemo.common.CommonConstants;
import com.mak.app.mvpdemo.common.models.CommonModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This class is common adaptor. which used in {@link com.mak.app.mvpdemo.common.activity.BaseSearchActivity}
 * <p>
 * Copyright (c) 2018 <ClientName>. All rights reserved.
 * Created by mak on 27/9/18.
 */
public class CommonAdaptor extends RecyclerView.Adapter<CommonAdaptor.ViewHolder> {

    private List<CommonModel> mItems;
    private List<CommonModel> mListOrigin;
    private OnItemClickListener mOnItemClickListener;
    private Context mContext;

    public CommonAdaptor(Context context, List<CommonModel> items, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.mItems = new ArrayList<>();
        this.mListOrigin = new ArrayList<>();

        this.mItems.addAll(items);
        this.mListOrigin.addAll(items);
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_common_list_fragment, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final CommonModel model = mItems.get(position);

        String name = model.getTitle();
        String subTitle = model.getSubTitle();

        ImageLinks imageLinks = model.getImageLinks();
        String imageUrl = null;
        if (imageLinks != null) {
            imageUrl = imageLinks.getSmallThumbnail();

        }

        holder.mHeading.setText(name);
        holder.mSubHeading.setText(subTitle);

        holder.mProgressBar.setVisibility(View.GONE);
        Picasso.with(mContext).cancelRequest(holder.mImage);

        if (imageUrl == null) {
            holder.mImageRoot.setVisibility(View.GONE);
        } else {
            holder.mImageRoot.setVisibility(View.VISIBLE);

            if (!TextUtils.isEmpty(imageUrl)) {

                if (imageUrl.startsWith(CommonConstants.EXTRA_IMAGE_STARTS_HTTP)) {

                    Picasso.with(mContext)
                            .load(imageUrl)
                            .centerCrop()
                            .fit()
                            .placeholder(R.drawable.placeholder_image)
                            .error(R.drawable.placeholder_image)
                            .into(holder.mImage, new Callback() {
                                @Override
                                public void onSuccess() {
                                    if (holder.mProgressBar != null) {
                                        holder.mProgressBar.setVisibility(View.GONE);
                                    }
                                }

                                @Override
                                public void onError() {
                                    if (holder.mProgressBar != null) {
                                        holder.mProgressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                } else {
                    holder.mImage.setImageDrawable(ContextCompat.getDrawable(mContext.getApplicationContext(), R.drawable.placeholder_image));
                }

            } else {
                holder.mImage.setImageDrawable(ContextCompat.getDrawable(mContext.getApplicationContext(), R.drawable.placeholder_image));
            }
        }

        holder.itemView.setOnClickListener(v -> mOnItemClickListener.onItemClick(holder.getAdapterPosition(), model));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    /**
     * Filter Adapter based on user search request.
     *
     * @param charText search value
     */
    public void filter(String charText) {

        mItems.clear();
        if (TextUtils.isEmpty(charText)) {
            mItems.addAll(mListOrigin);
        } else {
            for (CommonModel contact : mListOrigin) {
                charText = charText.toLowerCase();
                if (contact.getTitle().toLowerCase().contains(charText)) {
                    mItems.add(contact);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void addAll(List<CommonModel> commonModels) {

        this.mItems.clear();
        this.mListOrigin.clear();

        this.mItems.addAll(commonModels);
        this.mListOrigin.addAll(commonModels);
        notifyDataSetChanged();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_root)
        FrameLayout mImageRoot;

        @BindView(R.id.image)
        ImageView mImage;

        @BindView(R.id.heading)
        TextView mHeading;

        @BindView(R.id.sub_heading)
        TextView mSubHeading;

        @BindView(R.id.progress_bar)
        ProgressBar mProgressBar;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }

    public interface OnItemClickListener {
        void onItemClick(int position, CommonModel model);
    }
}