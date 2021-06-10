package sb.bookshelf.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sb.bookshelf.web.service.AccountService;
import sb.bookshelf.web.service.VolumeService;

@Slf4j
@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class Web {

    private final AccountService accounts;
    private final VolumeService volumes;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("title", "Latest titles");
        model.addAttribute("volumes", volumes.latest(10));
        return "index";
    }

    @PostMapping("/search")
    public String search(@RequestParam String query, Model model) {
        model.addAttribute("title", String.format("Search for '%s'", query));
        model.addAttribute("query", query);
        model.addAttribute("volumes", volumes.find(query));
        return "index";
    }

    @GetMapping("/v/{id}")
    public String details(@PathVariable String id, Model model) {
        model.addAttribute("volume", volumes.get(id));
        return "details";
    }

    @GetMapping("/register")
    public String register() {
        return accounts.count() == 0 ?
                "register" :
                "redirect:/";
    }

    @PostMapping("/register")
    public String confirmRegister(@RequestParam String username, @RequestParam String password) {
        var acc = accounts.add(username, password);
        return acc == null ?
                "redirect:/register" :
                "redirect:/";
    }
}
