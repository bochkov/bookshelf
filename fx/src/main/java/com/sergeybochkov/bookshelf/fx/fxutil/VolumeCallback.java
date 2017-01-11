package com.sergeybochkov.bookshelf.fx.fxutil;

import com.sergeybochkov.bookshelf.fx.model.Volume;

import java.io.IOException;

public interface VolumeCallback {

    interface Callback {
        void call(Volume volume) throws IOException;
    }

    Target callback(VolumeCallback.Callback volumeCallback);
}
