package com.mak.app.mvpdemo.common.models;

import com.mak.app.mvpdemo.apicalls.commonmodels.ImageLinks;

import java.util.List;

/**
 * This class is common model for list screen with search bar on top level
 * <p>
 * Copyright (c) 2018 <ClientName>. All rights reserved.
 * Created by mak on 26/9/18.
 */
public class CommonModel {

    private String id;
    private String title;
    private String subTitle;
    private List<String> authors;
    private String publisher;
    private String publishedDate;
    private String description;
    private String categories;
    private ImageLinks imageLinks;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public ImageLinks getImageLinks() {
        return imageLinks;
    }

    public void setImageLinks(ImageLinks imageLinks) {
        this.imageLinks = imageLinks;
    }
}
