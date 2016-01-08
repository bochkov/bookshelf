package com.sergeybochkov.bookshelf.web.service;

import com.sergeybochkov.bookshelf.web.model.Book;

import java.util.List;

public interface BookService {

    List<Book> find(String query);

    List<Book> findAll();
}
