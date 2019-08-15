package com.mak.app.mvpdemo.book;

import com.mak.app.mvpdemo.apicalls.responses.GoogleBooksVolumesResponseModel;
import com.mak.app.mvpdemo.mvp.MvpView;

/**
 * <p>
 * Copyright (c) 2018 <ClientName>. All rights reserved.
 * Created by mohammadarshikhan on 2019-08-14.
 */
public interface BooksMvpView extends MvpView {

    void getBookVolumesResponses(GoogleBooksVolumesResponseModel googleBooksVolumesResponseModel);
}
