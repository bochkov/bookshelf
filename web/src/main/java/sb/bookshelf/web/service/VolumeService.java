package sb.bookshelf.web.service;

import java.util.List;

import sb.bookshelf.common.model.Volume;
import sb.bookshelf.common.reqres.DelInfo;

public interface VolumeService {

    List<Volume> findAll();

    List<Volume> latest(int count);

    List<Volume> find(String query);

    List<Volume> findByField(String field, String query);

    List<String> allAuthors();

    List<String> allPublishers();

    Volume get(String id);

    Volume save(Volume volume);

    DelInfo delete(List<String> ids);

    Long count();
}
