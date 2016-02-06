package com.sergeybochkov.bookshelf.fx.dao;

import com.sergeybochkov.bookshelf.fx.model.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface BookDao extends MongoRepository<Book, String> {

    @Query("{'$or': [{name : { $regex: ?0, $options: 'i' }}, " +
            "{author: { $regex: ?0, $options: 'i' }}, " +
            "{year: { $regex: ?0, $options: 'i' }}, " +
            "{annotation: { $regex: ?0, $options: 'i' }}]}")
    List<Book> findOr(String query);

    List<Book> findByNameContaining(String name);

    List<Book> findByAuthorContaining(String author);

    List<Book> findByAnnotationContaining(String annotation);

    List<Book> findByYearContaining(String year);
}
