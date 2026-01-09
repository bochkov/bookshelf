package sb.bookshelf.web.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import sb.bookshelf.web.model.Volume;

import java.util.List;

public interface VolumeDao extends MongoRepository<Volume, String> {

    @Query("{ '$or': [" +
            "{ title :     { $regex: ?0, $options: 'i' } }, " +
            "{ author:     { $regex: ?0, $options: 'i' } }, " +
            "{ year:       { $regex: ?0, $options: 'i' } }, " +
            "{ annotation: { $regex: ?0, $options: 'i' } }, " +
            "{ books:      { $regex: ?0, $options: 'i' } } " +
            "] }")
    List<Volume> find(String query);

    List<Volume> findByTitleContainingIgnoreCase(String name);

    List<Volume> findByAuthorContainingIgnoreCase(String author);

    List<Volume> findByAnnotationContainingIgnoreCase(String annotation);

    List<Volume> findByYearContainingIgnoreCase(String year);
}
