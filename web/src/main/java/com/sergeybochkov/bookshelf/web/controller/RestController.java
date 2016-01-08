package com.sergeybochkov.bookshelf.web.controller;

import com.sergeybochkov.bookshelf.web.model.Book;
import com.sergeybochkov.bookshelf.web.model.SearchQuery;
import com.sergeybochkov.bookshelf.web.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    @Autowired
    private BookService bookService;

    @RequestMapping(value = "/search/", method = RequestMethod.POST)
    public List<Book> search(@RequestBody SearchQuery request) {
        if (request.getRequest() == null)
            return null;
        return bookService.find(request.getRequest());
    }
}
