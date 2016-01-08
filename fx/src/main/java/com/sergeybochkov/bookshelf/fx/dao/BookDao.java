package com.sergeybochkov.bookshelf.fx.dao;

import com.sergeybochkov.bookshelf.fx.model.Book;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookDao extends MongoRepository<Book, String> {
}
