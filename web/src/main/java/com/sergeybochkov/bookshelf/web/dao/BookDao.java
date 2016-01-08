package com.sergeybochkov.bookshelf.web.dao;

import com.sergeybochkov.bookshelf.web.model.Book;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BookDao extends MongoRepository<Book, String> {

    List<Book> findByNameContainingIgnoreCase(String name);

    List<Book> findByAuthorContainingIgnoreCase(String author);
}
