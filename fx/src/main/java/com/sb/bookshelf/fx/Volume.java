package com.sb.bookshelf.fx;

import lombok.Data;

import java.util.List;

@Data
public final class Volume {

    private String id;
    private String name;
    private String author;
    private String publisher;
    private String year;
    private String annotation;
    private String isbn;
    private Integer pages;
    private List<String> books;

    public String title() {
        return (author != null && !author.isEmpty()) ?
                String.format("%s. %s", author, name) :
                name;
    }
}