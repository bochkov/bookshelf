package com.sergeybochkov.bookshelf.fx.service;

import com.sergeybochkov.bookshelf.fx.model.Book;

import java.util.List;

public interface BookService {

    List<Book> findAll();

    void delete(Book book);

    void deleteAll(List<Book> books);

    Book save(Book book);

    List<Book> findOr(String query);

    List<Book> findByField(String field, String query);
}
