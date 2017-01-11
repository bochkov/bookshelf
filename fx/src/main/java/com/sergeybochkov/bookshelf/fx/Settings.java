package com.sergeybochkov.bookshelf.fx;

import com.sergeybochkov.bookshelf.fx.fxutil.CallbackTarget;
import com.sergeybochkov.bookshelf.fx.fxutil.Target;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public final class Settings implements Target, CallbackTarget {

    private final Stage stage;
    private AppProperties appProperties;
    private CallbackTarget.Callback callback = () -> {};

    @FXML
    private TextField hostField, portField;

    public Settings(Stage stage) {
        this.stage = stage;
    }

    public Settings withProperties(AppProperties appProperties) {
        this.appProperties = appProperties;
        this.hostField.setText(appProperties.getHost());
        this.portField.setText(String.valueOf(appProperties.getPort()));
        return this;
    }

    @Override
    public void show() {
        stage.show();
    }

    @FXML
    public void close() {
        stage.close();
    }

    @FXML
    public void save() {
        appProperties.setHost(hostField.getText());
        appProperties.setPort(Integer.parseInt(portField.getText()));
        callback.call();
        stage.close();
    }

    @Override
    public Target callback(Callback callback) {
        this.callback = callback;
        return this;
    }
}
