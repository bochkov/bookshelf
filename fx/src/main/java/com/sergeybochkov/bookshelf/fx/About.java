package com.sergeybochkov.bookshelf.fx;

import com.sergeybochkov.bookshelf.fx.fxutil.Target;
import javafx.stage.Stage;

public final class About implements Target {

    private final Stage stage;

    public About(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void show() {
        stage.show();
    }

    @Override
    public void close() {
        stage.close();
    }
}
