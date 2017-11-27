package sb.bookshelf.web;

import java.util.List;

public interface VolumeService {

    List<Volume> findAll();

    List<Volume> find(String query);

    List<Volume> findByField(String field, String query);

    Volume save(Volume volume);

    void delete(List<Volume> volumes);
}
