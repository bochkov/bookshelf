package com.sb.bookshelf.fx;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.net.URL;
import java.util.ResourceBundle;

public final class CtAbout implements Initializable {

    private static final Stage STAGE = new Stage();
    private static final StageFactory<CtAbout> STAGE_FACTORY = new StageFactory<>("/ui/about.fxml", STAGE);

    public static CtAbout instance(Window parent) {
        return STAGE_FACTORY.newStage(parent, Modality.APPLICATION_MODAL);
    }

    public Stage toStage() {
        STAGE.setResizable(false);
        return STAGE;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        STAGE.setTitle("О программе");
    }

    @FXML
    void close() {
        STAGE.close();
    }
}
