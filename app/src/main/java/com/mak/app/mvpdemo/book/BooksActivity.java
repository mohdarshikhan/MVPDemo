package com.mak.app.mvpdemo.book;

import android.os.Bundle;
import android.widget.Toast;

import com.mak.app.mvpdemo.R;
import com.mak.app.mvpdemo.apicalls.APIConstants;
import com.mak.app.mvpdemo.apicalls.commonmodels.ImageLinks;
import com.mak.app.mvpdemo.apicalls.commonmodels.Item;
import com.mak.app.mvpdemo.apicalls.commonmodels.VolumeInfo;
import com.mak.app.mvpdemo.apicalls.responses.GoogleBooksVolumesResponseModel;
import com.mak.app.mvpdemo.common.BaseApplication;
import com.mak.app.mvpdemo.common.activity.BaseSearchActivity;
import com.mak.app.mvpdemo.common.models.CommonModel;
import com.mak.app.mvpdemo.common.utils.CollectionUtils;
import com.mak.app.mvpdemo.common.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * <p>
 * Copyright (c) 2018 <ClientName>. All rights reserved.
 * Created by mohammadarshikhan on 2019-08-14.
 */
public class BooksActivity extends BaseSearchActivity implements BooksMvpView,
        BaseSearchActivity.ActionMenuClickListener {

    private List<Item> mBooks = new ArrayList<>();

    @Inject
    BooksPresenter mFarmersPresenter;

    @Override
    public void setInjectDependency() {
        BaseApplication.getAppComponents().inject(this);
    }

    @Override
    public String setActionBarTitle() {
        return getString(R.string.title_books);
    }

    @Override
    public ActionMenuClickListener setActionMenuClickListener() {
        return this;
    }

    @Override
    public void showProgress() {
        showProgressDialog();
    }

    @Override
    public void hideProgress() {
        hideProgressDialog();
    }

    @Override
    public void showError(Exception arg0) {
        showSnackBar(arg0.getMessage());
    }

    @Override
    public void showError(Throwable e) {
        showSnackBar(e.getMessage());
    }

    @Override
    public void showErrorPopup(String errorDescription) {
        showAlert(errorDescription);
    }

    @Override
    public void showErrorSnackBar(String errorDescription) {
        showSnackBar(errorDescription);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFarmersPresenter.attachView(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.isNetworkAvailable(this)) {
            if (mFarmersPresenter != null) {
                mFarmersPresenter.getBooks(this, APIConstants.APIVariables.QUERY_WORD);
            }
        } else {
            showToast(getString(R.string.error_internet_not_working), Toast.LENGTH_SHORT);
        }

    }

    @Override
    public void onItemClick(int position, CommonModel model) {

    }

    @Override
    public void getBookVolumesResponses(GoogleBooksVolumesResponseModel googleBooksVolumesResponseModel) {

        if (googleBooksVolumesResponseModel != null) {

            List<Item> books = googleBooksVolumesResponseModel.getItems();

            if (!CollectionUtils.isNullOrEmpty(books)) {

                mBooks.clear();
                mBooks.addAll(books);
                if (!mBooks.isEmpty()) {
                    List<CommonModel> commonModels = new ArrayList<>();
                    for (Item book : mBooks) {
                        CommonModel commonModel = new CommonModel();
                        commonModel.setId(book.getId());
                        commonModel.setSubTitle(book.getEtag());
                        VolumeInfo volumeInfo = book.getVolumeInfo();
                        String title = volumeInfo.getTitle();
                        commonModel.setTitle(title);

                        ImageLinks imageLinks = volumeInfo.getImageLinks();
                        commonModel.setImageLinks(imageLinks);

                        commonModels.add(commonModel);
                    }

                    if (!CollectionUtils.isNullOrEmpty(commonModels)) {
                        addItems(commonModels);
                        setTotal(R.plurals.books_count, commonModels.size());
                    }

                }
            }
        }
    }

    @Override
    public void onActionMenuClickListener(int itemId) {

    }
}
