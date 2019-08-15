package com.mak.app.mvpdemo.common.activity;

import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mak.app.mvpdemo.R;
import com.mak.app.mvpdemo.common.CommonConstants;
import com.mak.app.mvpdemo.common.adapter.CommonAdaptor;
import com.mak.app.mvpdemo.common.models.CommonModel;
import com.mak.app.mvpdemo.common.utils.CollectionUtils;
import com.mak.app.mvpdemo.customviews.EmptyRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * This class
 * <p>
 * Copyright (c) 2018 <ClientName>. All rights reserved.
 * Created by mak on 17/10/18.
 */
public abstract class BaseSearchActivity extends BaseActivity implements CommonAdaptor.OnItemClickListener {

    private EmptyRecyclerView mRecyclerView;
    private TextView mEmptyView;
    private CommonAdaptor mCommonAdaptor;
    private List<CommonModel> mCommonModels = new ArrayList<>();

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.tv_total)
    TextView mTvTotal;

    @BindView(R.id.rv_common)
    View mLayoutItemRV;

    @BindView(R.id.et_search)
    EditText mEtSearch;

    public abstract void setInjectDependency();

    public abstract String setActionBarTitle();

    public abstract ActionMenuClickListener setActionMenuClickListener();

    public ActionMenuClickListener mActionMenuClickListener;
    private boolean menuItemAddVisible = true;

    @Override
    protected boolean isFullScreen() {
        return false;
    }

    @Override
    public void injectDependency() {
        setInjectDependency();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_base_search;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(mToolbar);
        setAppActionBar();
        setUpTitle();

        mActionMenuClickListener = setActionMenuClickListener();

        mRecyclerView = mLayoutItemRV.findViewById(R.id.recycle_view);
        mEmptyView = mLayoutItemRV.findViewById(R.id.empty_view);

        initRecyclerView();

        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCommonAdaptor.filter(String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        setRecyclerAdapter(mRecyclerView);
    }

    private void setAppActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Drawable drawable = mToolbar.getNavigationIcon();
        if (drawable != null) {
            drawable.setColorFilter(ContextCompat.getColor(this, R.color.colorActionbarIcons), PorterDuff.Mode.SRC_ATOP);
        }
    }

    /**
     * setting here title
     */
    private void setUpTitle() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(null);
            mTvTitle.setText(setActionBarTitle());
        }
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        mRecyclerView.setEmptyView(mEmptyView);
    }

    private void setRecyclerAdapter(RecyclerView recyclerView) {
        mCommonAdaptor = new CommonAdaptor(this, mCommonModels, this);
        recyclerView.setAdapter(mCommonAdaptor);
    }

    protected void addItems(List<CommonModel> list) {
        if (CollectionUtils.isNullOrEmpty(list)) {
            mEmptyView.setVisibility(View.VISIBLE);
            mEmptyView.setText(getString(R.string.empty_list_message));
        } else {
            mEmptyView.setVisibility(View.GONE);
        }
        mCommonModels.clear();
        mCommonModels.addAll(list);
        mCommonAdaptor.addAll(mCommonModels);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

//        getMenuInflater().inflate(R.menu.menu_add, menu);
//        MenuItem menuItemAdd = menu.findItem(R.id.action_add);
//        menuItemAdd.setVisible(menuItemAddVisible);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
//            case R.id.action_add:
//                if (mActionMenuClickListener != null) {
//                    mActionMenuClickListener.onActionMenuClickListener(item.getItemId());
//                }
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void setTotal(int resID, int count) {
        Resources res = getResources();
        String text = res.getQuantityString(resID, count, count);

        Spannable wordToSpan = new SpannableString(text);

        wordToSpan.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.syncCounterTextColor)), 0, text.indexOf(CommonConstants.SEPARATOR_SPACE), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        mTvTotal.setText(wordToSpan);
    }

    protected void setMenuItemAddVisibility(boolean isVisible) {
        this.menuItemAddVisible = isVisible;
        invalidateOptionsMenu();
    }

    public interface ActionMenuClickListener {
        void onActionMenuClickListener(int itemId);
    }
}
