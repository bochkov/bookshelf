package sb.bookshelf.web.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import sb.bookshelf.common.messages.DeleteRequest;
import sb.bookshelf.common.messages.DeleteResponse;
import sb.bookshelf.common.messages.SearchQuery;
import sb.bookshelf.common.messages.TotalBooks;
import sb.bookshelf.common.model.Volume;
import sb.bookshelf.common.model.VolumeInfo;
import sb.bookshelf.web.service.VolumeService;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class Api {

    private final VolumeService volumeService;

    @GetMapping(value = "/count/")
    public TotalBooks totalBooks() {
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

    @GetMapping(value = "/latest/")
    public List<Volume> latest(@RequestParam(defaultValue = "10", name = "c") int count) {
        return volumeService.latest(count);
    }

    @PostMapping(value = "/save/")
    public Volume saveBook(@RequestBody VolumeInfo info) {
        return volumeService.save(info);
    }

    @PostMapping(value = "/delete/")
    public DeleteResponse delete(@RequestBody DeleteRequest req) {
        return new DeleteResponse(
                volumeService.delete(
                        req.getIds()
                )
        );
    }

    @PostMapping(value = "/search/")
    public List<Volume> search(@RequestBody SearchQuery query) {
        return volumeService.find(query.getQuery());
    }
}
