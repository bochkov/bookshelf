package com.sergeybochkov.bookshelf.fx;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SettingsController {

    private ApplicationProperties applicationProperties;
    private Stage stage;
    private Callback callback;

    @FXML
    private TextField hostField;
    @FXML
    private TextField portField;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setProperties(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
        this.hostField.setText(applicationProperties.getHost());
        this.portField.setText(String.valueOf(applicationProperties.getPort()));
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @FXML
    public void close() {
        if (stage != null)
            stage.close();
    }

    @FXML
    public void save() {
        applicationProperties.setHost(hostField.getText());
        applicationProperties.setPort(Integer.parseInt(portField.getText()));
        if (callback != null)
            callback.call();
        close();
    }
}
