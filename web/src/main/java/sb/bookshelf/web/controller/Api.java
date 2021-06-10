package sb.bookshelf.web.controller;

import java.io.Serializable;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import sb.bookshelf.common.model.Volume;
import sb.bookshelf.common.reqres.DelInfo;
import sb.bookshelf.common.reqres.SearchQuery;
import sb.bookshelf.common.reqres.TotalBooks;
import sb.bookshelf.common.reqres.VolumeInfo;
import sb.bookshelf.web.service.VolumeService;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class Api {

    private final VolumeService volumeService;

    @GetMapping(value = "/count/")
    public Serializable totalBooks() {
        return new TotalBooks(volumeService.count());
    }

    @GetMapping(value = "/authors/")
    public List<String> authors() {
        return volumeService.allAuthors();
    }

    @GetMapping(value = "/publishers/")
    public List<String> publishers() {
        return volumeService.allPublishers();
    }

    @GetMapping(value = "/list/")
    public List<Volume> findAll() {
        return volumeService.findAll();
    }

    @GetMapping(value = "/list/latest")
    public List<Volume> latest(@RequestParam(defaultValue = "10", name = "c") int count) {
        return volumeService.latest(count);
    }

    @PostMapping(value = "/save/")
    public Volume addBook(@RequestBody VolumeInfo info) {
        return volumeService.save(info.toVolume());
    }

    @PostMapping(value = "/delete/")
    public Serializable delete(@RequestBody DelInfo ids) {
        return volumeService.delete(ids.getIds());
    }

    @PostMapping(value = "/search/")
    public List<Volume> search(@RequestBody SearchQuery query) {
        return volumeService.find(query.getQuery());
    }
}
