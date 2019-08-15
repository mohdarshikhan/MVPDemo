package com.mak.app.mvpdemo.common.utils;

/**
 * This class contains status codes
 * <p>
 * Copyright (c) 2018 <ClientName>. All rights reserved.
 * Created by mak on 20/9/18.
 */
public enum StatusCodes {

    OK(200), INTERNAL_SERVER_ERROR(500), SERVER_UNAVAILABLE(503);

    public int code;

    private StatusCodes(int code) {
        this.code = code;
    }
}

