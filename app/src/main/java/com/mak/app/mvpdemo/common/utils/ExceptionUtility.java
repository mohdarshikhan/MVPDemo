package com.mak.app.mvpdemo.common.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.mak.app.mvpdemo.R;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.HttpException;
import timber.log.Timber;

/**
 * This class is to declare Exception related functions
 * <p>
 * Copyright (c) 2018 <ClientName>. All rights reserved.
 * Created by mak on 20/9/18.
 */
public class ExceptionUtility {

    private static final String LOG_TAG = ExceptionUtility.class.getSimpleName();

    /**
     * Handling the exception, which encountered on API call
     *
     * @param context Context
     * @param e       Throwable
     * @return Error Message
     */
    public static String handleServerResponseException(Context context, Throwable e) {
        try {
            if (e instanceof HttpException) {
                HttpException error = (HttpException) e;
                String errorBody = null;
                errorBody = error.response().errorBody().string();
                int errorCode = error.response().code();
                if (errorCode == StatusCodes.SERVER_UNAVAILABLE.code) {
                    return context.getString(R.string.error_server_unavailable);
                } else if (errorCode == StatusCodes.INTERNAL_SERVER_ERROR.code) {
                    return context.getString(R.string.error_internal_server_error);
                } else {
                    return ExceptionUtility.handleServerResponseError(errorBody);
                }
            } else if (e instanceof ConnectException) {
                ConnectException exception = (ConnectException) e;
                return exception.getMessage();
            } else if (e instanceof SocketTimeoutException) {
                SocketTimeoutException exception = (SocketTimeoutException) e;
                return exception.getMessage();
            } else if (e instanceof UnknownHostException) {
                return context.getString(R.string.error_internet_not_working);
            } else if (e instanceof Exception) {
                Exception exception = (Exception) e;
                return exception.getMessage();
            }

        } catch (IOException e1) {
            Log.e(LOG_TAG, e1.getMessage());
        }

        return null;
    }

    public static String handleServerResponseError(String errorBody) {
        // Todo handle proper error
        if (errorBody != null) {
            if (!TextUtils.isEmpty(errorBody)) {
                Timber.d("errorBody: " + errorBody);
//                ErrorResponseModel errorResponseModel = new Gson().fromJson(errorBody, ErrorResponseModel.class);
//                if (errorResponseModel != null) {
//                    if (errorResponseModel.getErrorDescription() != null) {
//                        return errorResponseModel.getErrorDescription();
//                    }
//                }
                return errorBody;
            }
        }

        return null;
    }
}

