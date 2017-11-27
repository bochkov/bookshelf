package com.sb.bookshelf.fx;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public final class CtSettings implements Initializable {

    private static final Stage STAGE = new Stage();
    private static final StageFactory<CtSettings> STAGE_FACTORY = new StageFactory<>("/ui/settings.fxml", STAGE);

    @FXML
    TextField hostField, portField, userField;
    @FXML
    PasswordField passField;
    AppProps appProps;

    public static CtSettings instance(Window parent) throws IOException {
        return STAGE_FACTORY.newStage(parent, Modality.APPLICATION_MODAL);
    }

    public CtSettings properties(AppProps appProps) {
        this.appProps = appProps;
        hostField.setText(appProps.hostProperty().get());
        portField.setText(appProps.portProperty().asString().get());
        userField.setText(appProps.userProperty().get());
        passField.setText(appProps.passProperty().get());
        return this;
    }

    public Stage toStage() throws IOException {
        return STAGE;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        STAGE.setTitle("Настройки");
    }

    @FXML
    void save() {
        appProps.hostProperty().setValue(hostField.getText());
        appProps.portProperty().setValue(Integer.valueOf(portField.getText()));
        appProps.userProperty().setValue(userField.getText());
        appProps.passProperty().setValue(passField.getText());
        STAGE.close();
    }

    @FXML
    void close() {
        STAGE.close();
    }
}
