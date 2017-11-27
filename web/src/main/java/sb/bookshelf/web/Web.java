package sb.bookshelf.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
public final class Web {

    private final AccountSevice accounts;

    @Autowired
    public Web(AccountSevice accounts) {
        this.accounts = accounts;
    }

    @GetMapping("/")
    public ModelAndView index() {
        Map<String, Object> model = new HashMap<>();
        model.put("firstRun", accounts.isEmpty());
        return new ModelAndView("index", model);
    }
}
