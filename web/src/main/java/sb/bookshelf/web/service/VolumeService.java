package sb.bookshelf.web.service;

import sb.bookshelf.web.model.Volume;

import java.util.List;

public interface VolumeService {

    List<Volume> latest(int count);

    List<Volume> find(String query);

    List<Volume> findByField(String field, String query);

    List<String> allAuthors();

    List<String> allPublishers();

    Volume get(String id);

    Volume save(Volume volume);

    List<String> delete(List<String> ids);

    Long count();
}
