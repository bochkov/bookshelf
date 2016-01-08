package com.sergeybochkov.bookshelf.web.service;

import com.sergeybochkov.bookshelf.web.dao.BookDao;
import com.sergeybochkov.bookshelf.web.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookDao bookDao;

    @Override
    public List<Book> find(String query) {
        List<Book> books = new ArrayList<>();

        bookDao.findByAuthorContainingIgnoreCase(query)
                .stream()
                .filter(book -> !books.contains(book))
                .forEach(books::add);

        bookDao.findByNameContainingIgnoreCase(query)
                .stream()
                .filter(book -> !books.contains(book))
                .forEach(books::add);

        return books;
    }

    @Override
    public List<Book> findAll() {
        return bookDao.findAll();
    }
}
