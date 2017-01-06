package com.sergeybochkov.bookshelf.fx;

import java.io.IOException;

public interface VolumeCallback {

    void call(Volume volume) throws IOException;
}
