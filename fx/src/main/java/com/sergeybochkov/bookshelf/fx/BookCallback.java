package com.sergeybochkov.bookshelf.fx;

import java.io.IOException;

public interface BookCallback {

    void call(Book book) throws IOException;
}
