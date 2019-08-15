package com.mak.app.mvpdemo.apicalls;

import com.mak.app.mvpdemo.apicalls.responses.GoogleBooksVolumesResponseModel;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * This class is to Define API endpoints
 * <p>
 * Copyright (c) 2018 <ClientName>. All rights reserved.
 * Created by mak on 20/9/18.
 */
public interface APIServices {

    @GET(APIConstants.APIS.BOOK_VOLUMES)
    Observable<GoogleBooksVolumesResponseModel> getBookVolumes(@Query(APIConstants.APIVariables.QUERY_KEY) String query);


}