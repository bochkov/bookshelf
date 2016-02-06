package com.sergeybochkov.bookshelf.fx.service;

import com.sergeybochkov.bookshelf.fx.dao.BookDao;
import com.sergeybochkov.bookshelf.fx.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookDao bookDao;

    @Override
    public List<Book> findAll() {
        return bookDao.findAll();
    }

    @Override
    public void delete(Book book) {
        bookDao.delete(book);
    }

    @Override
    public void deleteAll(List<Book> books) {
        bookDao.delete(books);
    }

    @Override
    public Book save(Book book) {
        return bookDao.save(book);
    }

    @Override
    public List<Book> findOr(String query) {
        return bookDao.findOr(query);
    }

    @Override
    public List<Book> findByField(String field, String query) {
        switch (field) {
            case "name":
                return bookDao.findByNameContaining(query);
            case "author":
                return bookDao.findByAuthorContaining(query);
            case "year":
                return bookDao.findByYearContaining(query);
            case "annotation":
                return bookDao.findByAnnotationContaining(query);
            default:
                return new ArrayList<>();
        }
    }
}
