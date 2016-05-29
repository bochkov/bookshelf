package com.sergeybochkov.bookshelf.web.controller;

import com.sergeybochkov.bookshelf.web.model.Book;
import com.sergeybochkov.bookshelf.web.model.SearchQuery;
import com.sergeybochkov.bookshelf.web.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ApiController {

    @Autowired
    private BookService bookService;

    @RequestMapping(value = "/api/list/", method = RequestMethod.GET)
    public List<Book> findAll() {
        return bookService.findAll();
    }

    @RequestMapping(value = "/api/save/", method = RequestMethod.POST)
    public Book addBook(@RequestBody Book book) {
        return bookService.save(book);
    }

    @RequestMapping(value = "/api/search/", method = RequestMethod.POST)
    public List<Book> search(@RequestBody SearchQuery request) {
        if (request.getRequest() == null)
            return null;

        if (request.getRequest().startsWith("{")) {
            List<Book> allBooks = bookService.findAll();
            allBooks.retainAll(match(request.getRequest()));
            return allBooks;
        }
        else
            return bookService.findOr(request.getRequest());
    }

    private List<Book> match(String value) {
        String v = value.replaceAll("\\{|\\}", "");
        List<Book> books = new ArrayList<>();
        for (String token : v.split(";|,")) {
            String[] f = token.split("=");
            if (f.length != 2)
                continue;

            if (books.isEmpty())
                books.addAll(bookService.findByField(f[0], f[1]));
            else
                books.retainAll(bookService.findByField(f[0], f[1]));
        }
        return books;
    }
}
