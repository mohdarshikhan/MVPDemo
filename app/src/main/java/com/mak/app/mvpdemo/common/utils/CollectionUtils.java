package com.mak.app.mvpdemo.common.utils;

import java.util.Collection;

/**
 * This class contains Collection related methods
 * <p>
 * Copyright (c) 2019 <ClientName>. All rights reserved.
 * Created by mak on 25/1/19.
 */
public class CollectionUtils {

    private CollectionUtils() {
    }

    /**
     * Checking a collection is empty or null
     *
     * @param list Collection
     * @return true if collection empty or null else false
     */
    public static boolean isNullOrEmpty(Collection<?> list) {
        return list == null || list.size() == 0;
    }
}
