package com.sergeybochkov.bookshelf.web.model;

public class SearchQuery {

    String request;

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    @Override
    public String toString() {
        return "SearchQuery {" +
                "request='" + request + '\'' +
                '}';
    }
}
