package com.mak.app.mvpdemo.dagger;

import com.mak.app.mvpdemo.book.BookDetailActivity;
import com.mak.app.mvpdemo.book.BooksActivity;
import com.mak.app.mvpdemo.common.BaseApplication;

import javax.inject.Singleton;

import dagger.Component;

/**
 * This class is to declare Components for dagger
 * <p>
 * Copyright (c) 2018 <ClientName>. All rights reserved.
 * Created by mak on 08/06/19.
 */
@Component(modules = AppModules.class)
@Singleton
public interface AppComponents {
    void inject(BaseApplication baseApplication);

    void inject(BooksActivity booksActivity);

    void inject(BookDetailActivity bookDetailActivity);

}
