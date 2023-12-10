package sb.bookshelf.web.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import sb.bookshelf.web.dao.VolumeDao;
import sb.bookshelf.web.model.Volume;

@Slf4j
@Service
@RequiredArgsConstructor
public final class VolumeServiceImpl implements VolumeService {

    private final VolumeDao volumeDao;
    private final MongoTemplate mongoTemplate;

    @Override
    public List<Volume> latest(int count) {
        var sort = Sort.by(Sort.Direction.DESC, "_id");
        return volumeDao.findAll(
                PageRequest.of(0, count, sort)
        ).getContent();
    }

    private Set<Volume> match(String value) {
        LOG.debug("value = {}", value);
        Set<Volume> volumes = new HashSet<>();
        for (String token : value.split("[;,]")) {
            String[] f = token.split("=");
            if (f.length == 2) {
                LOG.debug("find by field={}, value={}", f[0], f[1]);
                for (Volume vol : findByField(f[0], f[1])) {
                    boolean added = volumes.add(vol);
                    LOG.debug("{}: vol={}", added ? "added" : "already exist", vol);
                }
            }
        }
        return volumes;
    }

    @Override
    public List<Volume> find(String query) {
        LOG.debug("{}", query);
        return query.contains("=") ?
                new ArrayList<>(match(query)) :
                volumeDao.find(query);
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
    public Volume save(Volume v) {
        return volumeDao.save(v);
    }

    @Override
    public List<String> delete(List<String> ids) {
        List<String> deletedIds = new ArrayList<>();
        for (String id : ids) {
            volumeDao.findById(id).ifPresent(v -> {
                volumeDao.deleteById(v.getId());
                deletedIds.add(v.getId());
            });
        }
        return deletedIds;
    }

    @Override
    public Long count() {
        return volumeDao.count();
    }
}
