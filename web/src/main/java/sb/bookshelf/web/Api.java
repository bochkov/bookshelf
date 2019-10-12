package sb.bookshelf.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
public final class Api {

    private final AccountSevice accountSevice;
    private final VolumeService volumeService;

    @Autowired
    public Api(AccountSevice accountSevice, VolumeService volumeService) {
        this.accountSevice = accountSevice;
        this.volumeService = volumeService;
    }

    @GetMapping(value = "/count")
    public Long totalBooks() {
        return volumeService.count();
    }

    @GetMapping(value = "/list")
    public List<Volume> findAll() {
        return volumeService.findAll();
    }

    @GetMapping(value = "/list/latest")
    public List<Volume> latest(@RequestParam(defaultValue = "10", name = "c") int count) {
        return volumeService.latest(count);
    }

    @PostMapping(value = "/save")
    public Volume addBook(@RequestBody Volume volume) {
        return volumeService.save(volume);
    }

    @PostMapping(value = "/register")
    public void registerTestUser(@RequestBody Account account) {
        accountSevice.register(account);
    }

    @PostMapping(value = "/delete")
    public void delete(@RequestBody Volume[] volumes) {
        volumeService.delete(Arrays.asList(volumes));
    }

    @PostMapping(value = "/search")
    public List<Volume> search(@RequestBody String query) {
        if (query.startsWith("{")) {
            List<Volume> allVolumes = volumeService.findAll();
            allVolumes.retainAll(match(query));
            return allVolumes;
        } else
            return volumeService.find(query);
    }

    private List<Volume> match(String value) {
        String v = value.replaceAll("[{}]", "");
        List<Volume> volumes = new ArrayList<>();
        for (String token : v.split("[;,]")) {
            String[] f = token.split("=");
            if (f.length == 2) {
                if (volumes.isEmpty())
                    volumes.addAll(volumeService.findByField(f[0], f[1]));
                else
                    volumes.retainAll(volumeService.findByField(f[0], f[1]));
            }
        }
        return volumes;
    }

}
