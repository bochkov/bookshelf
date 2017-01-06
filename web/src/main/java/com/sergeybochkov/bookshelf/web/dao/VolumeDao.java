package com.sergeybochkov.bookshelf.web.dao;

import com.sergeybochkov.bookshelf.web.model.Volume;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface VolumeDao extends MongoRepository<Volume, String> {

    @Query("{ '$or': [" +
            "{ name :      { $regex: ?0, $options: 'i' } }, " +
            "{ author:     { $regex: ?0, $options: 'i' } }, " +
            "{ year:       { $regex: ?0, $options: 'i' } }, " +
            "{ annotation: { $regex: ?0, $options: 'i' } }, " +
            "{ books:      { $regex: ?0, $options: 'i' } } " +
            "] }")
    List<Volume> findOr(String query);

    List<Volume> findByNameContaining(String name);

    List<Volume> findByAuthorContaining(String author);

    List<Volume> findByAnnotationContaining(String annotation);

    List<Volume> findByYearContaining(String year);
}
