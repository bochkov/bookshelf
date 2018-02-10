package com.sb.bookshelf.fx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public final class StageFactory<T> {

    private final FXMLLoader fxml;
    private final Stage stage;

    public StageFactory(String resource, Stage stage) {
        this.fxml = new FXMLLoader(StageFactory.class.getResource(resource));
        this.stage = stage;
    }

    public T newStage(Window parent) {
        return newStage(parent, Modality.NONE);
    }

    public T newStage(Window parent, Modality modality) {
        if (stage.getScene() == null) {
            try {
                stage.setScene(new Scene(fxml.load()));
                stage.initOwner(parent);
                stage.initModality(modality);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return fxml.getController();
    }
}
