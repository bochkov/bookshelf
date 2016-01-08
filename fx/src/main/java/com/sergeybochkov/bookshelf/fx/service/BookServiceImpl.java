package com.sergeybochkov.bookshelf.fx.service;

import com.sergeybochkov.bookshelf.fx.dao.BookDao;
import com.sergeybochkov.bookshelf.fx.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
