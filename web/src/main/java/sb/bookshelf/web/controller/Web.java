package sb.bookshelf.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sb.bookshelf.common.model.VolumeInfo;
import sb.bookshelf.web.model.Volume;
import sb.bookshelf.web.service.VolumeService;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class Web {

    private final VolumeService volumes;

    @GetMapping("/")
    public String index(Model model) {
        List<VolumeInfo> vols = volumes.latest(20)
                .stream()
                .map(Volume::toVolumeInfo)
                .toList();
        model.addAttribute("title", "Latest titles");
        model.addAttribute("volumes", vols);
        return "index";
    }

    @PostMapping("/search")
    public String search(@RequestParam String query, Model model) {
        List<VolumeInfo> vols = volumes.find(query)
                .stream()
                .map(Volume::toVolumeInfo)
                .toList();
        model.addAttribute("title", String.format("Search for '%s'", query));
        model.addAttribute("query", query);
        model.addAttribute("volumes", vols);
        return "index";
    }

    @GetMapping("/v/{id}")
    public String details(@PathVariable String id, Model model) {
        model.addAttribute("volume", volumes.get(id).toVolumeInfo());
        return "details";
    }

}
