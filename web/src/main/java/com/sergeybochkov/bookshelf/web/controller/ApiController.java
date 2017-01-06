package com.sergeybochkov.bookshelf.web.controller;

import com.sergeybochkov.bookshelf.web.model.Volume;
import com.sergeybochkov.bookshelf.web.model.VolumeWrapper;
import com.sergeybochkov.bookshelf.web.model.SearchQuery;
import com.sergeybochkov.bookshelf.web.service.VolumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ApiController {

    private final VolumeService volumeService;

    @Autowired
    public ApiController(VolumeService volumeService) {
        this.volumeService = volumeService;
    }

    @RequestMapping(value = "/api/list/", method = RequestMethod.GET)
    public List<Volume> findAll() {
        return volumeService.findAll();
    }

    @RequestMapping(value = "/api/save/", method = RequestMethod.POST)
    public Volume addBook(@RequestBody Volume volume) {
        return volumeService.save(volume);
    }

    @RequestMapping(value = "/api/delete/", method = RequestMethod.POST)
    public void delete(@RequestBody VolumeWrapper books) {
        volumeService.delete(books.getVolumes());
    }

    @RequestMapping(value = "/api/search/", method = RequestMethod.POST)
    public List<Volume> search(@RequestBody SearchQuery request) {
        if (request.getRequest() == null)
            return null;

        if (request.getRequest().startsWith("{")) {
            List<Volume> allVolumes = volumeService.findAll();
            allVolumes.retainAll(match(request.getRequest()));
            return allVolumes;
        }
        else
            return volumeService.findOr(request.getRequest());
    }

    private List<Volume> match(String value) {
        String v = value.replaceAll("\\{|\\}", "");
        List<Volume> volumes = new ArrayList<>();
        for (String token : v.split(";|,")) {
            String[] f = token.split("=");
            if (f.length != 2)
                continue;

            if (volumes.isEmpty())
                volumes.addAll(volumeService.findByField(f[0], f[1]));
            else
                volumes.retainAll(volumeService.findByField(f[0], f[1]));
        }
        return volumes;
    }
}
