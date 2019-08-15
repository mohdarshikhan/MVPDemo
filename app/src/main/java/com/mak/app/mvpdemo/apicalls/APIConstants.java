package com.mak.app.mvpdemo.apicalls;

/**
 * This is class for Constant API Variables and make the class non-extendable by adding final
 * <p>
 * Copyright (c) 2018 <ClientName>. All rights reserved.
 * Created by mak on 20/9/18.
 */
public final class APIConstants {

    public interface Globals {
        String MAIN_THREAD = "mMainThread";
        String NEW_THREAD = "mNewThread";
        String IMMEDIATE_THREAD = "immediate";
        String UNAUTHORIZED = "UNAUTHORIZED";
        String SSL = "SSL";
    }

    public interface APIS {
        String DOMAIN = "https://www.googleapis.com/";
        String API_VERSION = "books/v1/";

        // https://www.googleapis.com/books/v1/volumes?q=android
        String BOOK_VOLUMES = DOMAIN + API_VERSION + "volumes";

    }

    public interface APIVariables {
        String QUERY_WORD = "android";
        String QUERY_KEY = "q";
    }

}
