package com.sergeybochkov.bookshelf.fx.fxutil;

import java.io.IOException;

public interface ResultCallback {

    interface Callback {
        void call(Object object) throws IOException;
    }

    Target callback(ResultCallback.Callback volumeCallback);
}
