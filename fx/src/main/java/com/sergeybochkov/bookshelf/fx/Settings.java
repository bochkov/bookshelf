package com.sergeybochkov.bookshelf.fx;

import com.sergeybochkov.bookshelf.fx.fxutil.CallbackTarget;
import com.sergeybochkov.bookshelf.fx.fxutil.Target;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public final class Settings implements Target, CallbackTarget {

    private final Stage stage;
    private final AppProperties properties;

    @FXML
    private TextField hostField, portField;
    private CallbackTarget.Callback callback;

    public Settings(Stage stage, AppProperties properties) {
        this.stage = stage;
        this.properties = properties;
    }

    @Override
    public void init() {
        this.hostField.setText(properties.getProperty(AppProperties.HOST));
        this.portField.setText(properties.getProperty(AppProperties.PORT));
    }

    @Override
    public Target callback(Callback callback) {
        this.callback = callback;
        return this;
    }

    @FXML
    public void save() {
        properties.setProperty(AppProperties.HOST, hostField.getText());
        properties.setProperty(AppProperties.PORT, portField.getText());
        callback.call();
        stage.close();
    }

    @FXML
    public void close() {
        stage.close();
    }
}
