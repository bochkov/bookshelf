package sb.bookshelf.web.service;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import sb.bookshelf.common.model.Volume;
import sb.bookshelf.common.reqres.DelInfo;
import sb.bookshelf.web.dao.VolumeDao;

@Slf4j
@Service
@RequiredArgsConstructor
public final class VolumeServiceImpl implements VolumeService {

    private final VolumeDao volumeDao;
    private final MongoTemplate mongoTemplate;

    @Override
    public List<Volume> findAll() {
        return volumeDao.findAll();
    }

    @Override
    public List<Volume> latest(int count) {
        var sort = Sort.by(Sort.Direction.DESC, "_id");
        return volumeDao.findAll(
                PageRequest.of(0, count, sort)
        ).getContent();
    }

    @Override
    public List<Volume> find(String query) {
        LOG.debug("{}", query);
        if (query.startsWith("{")) {
            List<Volume> allVolumes = findAll();
            allVolumes.retainAll(match(query));
            return allVolumes;
        } else
            return volumeDao.find(
                    query
                            .replace("[", "")
                            .replace("]", "")
                            .replace("{", "")
            );
    }

    @Override
    public List<Volume> findByField(String field, String query) {
        return switch (field) {
            case "title" -> volumeDao.findByTitleContaining(query);
            case "author" -> volumeDao.findByAuthorContaining(query);
            case "year" -> volumeDao.findByYearContaining(query);
            case "annotation" -> volumeDao.findByAnnotationContaining(query);
            default -> new ArrayList<>();
        };
    }

    @Override
    public List<String> allAuthors() {
        return mongoTemplate.query(Volume.class)
                .distinct("author")
                .as(String.class)
                .all();
    }

    @Override
    public List<String> allPublishers() {
        return mongoTemplate.query(Volume.class)
                .distinct("publisher")
                .as(String.class)
                .all();
    }

    @Override
    public Volume get(String id) {
        return volumeDao.findById(id).orElse(new Volume());
    }

    @Override
    public Volume save(Volume volume) {
        return volumeDao.save(volume);
    }

    @Override
    public DelInfo delete(List<String> ids) {
        var info = new DelInfo();
        for (String id : ids) {
            volumeDao.findById(id).ifPresent(v -> {
                volumeDao.deleteById(v.getId());
                info.append(v.getId());
            });
        }
        return info;
    }

    @Override
    public Long count() {
        return volumeDao.count();
    }

    private List<Volume> match(String value) {
        LOG.debug("value = {}", value);
        String v = value.replaceAll("[{}]", "");
        List<Volume> volumes = new ArrayList<>();
        for (String token : v.split("[;,]")) {
            String[] f = token.split("=");
            if (f.length == 2) {
                if (volumes.isEmpty()) {
                    volumes.addAll(findByField(f[0], f[1]));
                    LOG.debug("added by field={}, value={}", f[0], f[1]);
                } else {
                    volumes.retainAll(findByField(f[0], f[1]));
                    LOG.debug("retained field={}, value={}", f[0], f[1]);
                }
            }
        }
        return volumes;
    }
}
