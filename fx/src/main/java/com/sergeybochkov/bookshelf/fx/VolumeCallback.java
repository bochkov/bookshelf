package com.sergeybochkov.bookshelf.fx;

import com.sergeybochkov.bookshelf.fx.model.Volume;

import java.io.IOException;

public interface VolumeCallback {

    void call(Volume volume) throws IOException;
}
