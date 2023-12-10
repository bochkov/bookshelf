package sb.bookshelf.web.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import sb.bookshelf.web.model.Volume;

public interface VolumeDao extends MongoRepository<Volume, String> {

    @Query("{ '$or': [" +
            "{ title :     { $regex: ?0, $options: 'i' } }, " +
            "{ author:     { $regex: ?0, $options: 'i' } }, " +
            "{ year:       { $regex: ?0, $options: 'i' } }, " +
            "{ annotation: { $regex: ?0, $options: 'i' } }, " +
            "{ books:      { $regex: ?0, $options: 'i' } } " +
            "] }")
    List<Volume> find(String query);

    List<Volume> findByTitleContaining(String name);

    List<Volume> findByAuthorContaining(String author);

    List<Volume> findByAnnotationContaining(String annotation);

    List<Volume> findByYearContaining(String year);
}
