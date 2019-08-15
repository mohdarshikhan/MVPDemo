package com.mak.app.mvpdemo.book;

import android.content.Context;

import com.mak.app.mvpdemo.apicalls.APIConstants;
import com.mak.app.mvpdemo.apicalls.APIServices;
import com.mak.app.mvpdemo.apicalls.responses.GoogleBooksVolumesResponseModel;
import com.mak.app.mvpdemo.common.utils.ExceptionUtility;
import com.mak.app.mvpdemo.mvp.BasePresenter;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;
import io.reactivex.observers.DisposableObserver;
import timber.log.Timber;

/**
 * <p>
 * Copyright (c) 2018 <ClientName>. All rights reserved.
 * Created by mohammadarshikhan on 2019-08-14.
 */
public class BooksPresenter extends BasePresenter<BooksMvpView> {

    @Inject
    @Named(APIConstants.Globals.MAIN_THREAD)
    public Scheduler mMainThread;

    @Inject
    @Named(APIConstants.Globals.NEW_THREAD)
    public Scheduler mNewThread;

    @Inject
    @Named(APIConstants.Globals.UNAUTHORIZED)
    public APIServices mApiServices;

    @Inject
    public BooksPresenter() {

    }

    @Override
    public void attachView(BooksMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    public void getBooks(final Context context, String query) {

        if (isViewAttached()) {
            getMvpView().showProgress();
        }

        mApiServices.getBookVolumes(query)
                .subscribeOn(mNewThread)
                .observeOn(mMainThread)
                .subscribe(new DisposableObserver<GoogleBooksVolumesResponseModel>() {
                    @Override
                    public void onComplete() {
                        if (isViewAttached()) {
                            getMvpView().hideProgress();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) {
                            getMvpView().hideProgress();
                        }
                        String errorMsg = ExceptionUtility.handleServerResponseException(context, e);
                        if (isViewAttached()) {
                            getMvpView().showErrorPopup(errorMsg);
                        }
                        Timber.d(e.getMessage());
                    }

                    @Override
                    public void onNext(GoogleBooksVolumesResponseModel googleBooksVolumesResponseModel) {
                        if (isViewAttached()) {
                            getMvpView().getBookVolumesResponses(googleBooksVolumesResponseModel);
                        }
                    }
                });
    }
}
