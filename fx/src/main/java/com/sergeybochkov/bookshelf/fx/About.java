package com.sergeybochkov.bookshelf.fx;

import com.sergeybochkov.bookshelf.fx.fxutil.Target;
import javafx.fxml.FXML;
import javafx.stage.Stage;

import java.util.Properties;

public final class About implements Target {

    private final Stage stage;
    private final Properties properties;

    public About(Stage stage, Properties properties) {
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
