package sb.bookshelf.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public final class VolumeServiceImpl implements VolumeService {

    private final VolumeDao volumeDao;

    @Autowired
    public VolumeServiceImpl(VolumeDao volumeDao) {
        this.volumeDao = volumeDao;
    }

    @Override
    public List<Volume> findAll() {
        return volumeDao.findAll();
    }

    @Override
    public List<Volume> find(String query) {
        return volumeDao.find(
                query
                        .replace("[", "")
                        .replace("]", "")
                        .replace("{", "")
        );
    }

    @Override
    public List<Volume> findByField(String field, String query) {
        switch (field) {
            case "name":
                return volumeDao.findByNameContaining(query);
            case "author":
                return volumeDao.findByAuthorContaining(query);
            case "year":
                return volumeDao.findByYearContaining(query);
            case "annotation":
                return volumeDao.findByAnnotationContaining(query);
            default:
                return new ArrayList<>();
        }
    }

    @Override
    public Volume save(Volume volume) {
        return volumeDao.save(volume);
    }

    @Override
    public void delete(List<Volume> volumes) {
        volumeDao.delete(volumes);
    }
}
