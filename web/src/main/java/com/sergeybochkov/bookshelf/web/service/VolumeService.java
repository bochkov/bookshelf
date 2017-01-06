package com.sergeybochkov.bookshelf.web.service;

import com.sergeybochkov.bookshelf.web.model.Volume;

import java.util.List;

public interface VolumeService {

    List<Volume> findAll();

    List<Volume> findOr(String query);

    List<Volume> findByField(String field, String query);

    Volume save(Volume volume);

    void delete(List<Volume> volumes);
}
