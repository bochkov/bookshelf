package com.sergeybochkov.bookshelf.fx;

import com.sergeybochkov.bookshelf.fx.fxutil.Target;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public final class About implements Target {

    private final Stage stage;
    private final AppProperties properties;

    public About(Stage stage, AppProperties properties) {
        this.stage = stage;
        this.properties = properties;
    }

    @Override
    public void init() {
    }

    @FXML
    private void close() {
        stage.close();
    }
}
