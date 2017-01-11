package com.sergeybochkov.bookshelf.fx.fxutil;

public interface CallbackTarget {

    interface Callback {
        void call();
    }

    Target callback(CallbackTarget.Callback callback);
}
