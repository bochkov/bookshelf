package com.sb.bookshelf.fx;

public interface Callback {

    void call();

    class EMPTY implements Callback {
        @Override
        public void call() {
        }
    }
}
