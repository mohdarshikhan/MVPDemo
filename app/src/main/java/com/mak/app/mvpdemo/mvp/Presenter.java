package com.mak.app.mvpdemo.mvp;

/**
 * Every presenter in the app must either implement this interface or extend BasePresenter
 * indicating the MvpView type that wants to be attached with.
 * <p>
 * Copyright (c) 2018 <ClientName>. All rights reserved.
 * Created by mak on 20/9/18.
 */
public interface Presenter<V extends MvpView> {

    void attachView(V mvpView);

    void detachView();
}
