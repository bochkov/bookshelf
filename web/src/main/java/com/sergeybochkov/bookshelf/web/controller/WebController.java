package com.sergeybochkov.bookshelf.web.controller;

import com.sergeybochkov.bookshelf.web.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {

    @Autowired
    private BookService bookService;

    @RequestMapping("/")
    public String index() {
        return "index";
    }
}
