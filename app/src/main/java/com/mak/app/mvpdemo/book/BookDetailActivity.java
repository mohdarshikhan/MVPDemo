package com.mak.app.mvpdemo.book;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.mak.app.mvpdemo.R;
import com.mak.app.mvpdemo.apicalls.commonmodels.ImageLinks;
import com.mak.app.mvpdemo.common.BaseApplication;
import com.mak.app.mvpdemo.common.CommonConstants;
import com.mak.app.mvpdemo.common.activity.BaseActivity;
import com.mak.app.mvpdemo.common.models.CommonModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;

public class BookDetailActivity extends BaseActivity {

    private String bookJson;
    private CommonModel mBook;

    @BindView(R.id.book_image)
    ImageView mBookImage;

    @BindView(R.id.book_name)
    TextView mBookName;

    @BindView(R.id.book_desc)
    TextView mBookDesc;

    @Inject
    public Gson mGson;

    @Override
    protected boolean isFullScreen() {
        return false;
    }

    @Override
    public void injectDependency() {
        BaseApplication.getAppComponents().inject(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_book_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        bookJson = intent.getStringExtra("bookJson");

        mBook = mGson.fromJson(bookJson, CommonModel.class);

        setViews(mBook);
    }

    private void setViews(CommonModel book) {
        if (book != null) {
            ImageLinks imageLinks = book.getImageLinks();
            String imageUrl = imageLinks.getThumbnail();
            String name = book.getTitle();
            String desc = book.getDescription();

            if (!TextUtils.isEmpty(name)) {
                mBookName.setText(name);
            }

            if (!TextUtils.isEmpty(desc)) {
                mBookDesc.setText(desc);
            }

            if (!TextUtils.isEmpty(imageUrl)) {

                if (imageUrl.startsWith(CommonConstants.EXTRA_IMAGE_STARTS_HTTP)) {

                    Picasso.with(BookDetailActivity.this)
                            .load(imageUrl)
                            .centerCrop()
                            .fit()
                            .placeholder(R.drawable.placeholder_image)
                            .error(R.drawable.placeholder_image)
                            .into(mBookImage, new Callback() {
                                @Override
                                public void onSuccess() {
//                                    if (holder.mProgressBar != null) {
//                                        holder.mProgressBar.setVisibility(View.GONE);
//                                    }
                                }

                                @Override
                                public void onError() {
//                                    if (holder.mProgressBar != null) {
//                                        holder.mProgressBar.setVisibility(View.GONE);
//                                    }
                                }
                            });
                } else {
                    mBookImage.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.placeholder_image));
                }

            } else {
                mBookImage.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.placeholder_image));
            }

        }
    }
}
