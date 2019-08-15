package com.mak.app.mvpdemo.apicalls.commonmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * <p>
 * Copyright (c) 2018 <ClientName>. All rights reserved.
 * Created by mohammadarshikhan on 2019-08-14.
 */
public class SearchInfo {

    @SerializedName("textSnippet")
    @Expose
    private String textSnippet;

    public String getTextSnippet() {
        return textSnippet;
    }

    public void setTextSnippet(String textSnippet) {
        this.textSnippet = textSnippet;
    }
}
