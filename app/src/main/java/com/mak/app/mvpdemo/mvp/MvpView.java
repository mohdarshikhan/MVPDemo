package com.mak.app.mvpdemo.mvp;

/**
 * Base interface that any class that wants to act as a View in the MVP (Model View Presenter)
 * pattern must implement. Generally this interface will be extended by a more specific interface
 * that then usually will be implemented by an Activity or Fragment.
 * <p>
 * Copyright (c) 2018 <ClientName>. All rights reserved.
 * Created by mak on 20/9/18.
 */
public interface MvpView {
    void showProgress();

    void hideProgress();

    void showError(Exception arg0);

    void showError(Throwable e);

    void showErrorPopup(String errorDescription);

    void showErrorSnackBar(String errorDescription);
}


