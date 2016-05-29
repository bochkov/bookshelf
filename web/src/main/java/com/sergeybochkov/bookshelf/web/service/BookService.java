package com.sergeybochkov.bookshelf.web.service;

import com.sergeybochkov.bookshelf.web.model.Book;

import java.util.List;

public interface BookService {

    List<Book> findAll();

    List<Book> findOr(String query);

    List<Book> findByField(String field, String query);

    Book save(Book book);

    List<Book> delete(List<Book> books);
}
