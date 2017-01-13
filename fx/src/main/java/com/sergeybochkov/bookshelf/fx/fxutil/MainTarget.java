package com.sergeybochkov.bookshelf.fx.fxutil;

import java.util.Map;

public interface MainTarget extends Target {

    void withViews(Map<String, View> views);

}
