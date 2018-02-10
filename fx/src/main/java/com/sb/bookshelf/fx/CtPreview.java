package com.sb.bookshelf.fx;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.net.URL;
import java.util.ResourceBundle;

public final class CtPreview implements Initializable {

    private static final Stage STAGE = new Stage();
    private static final StageFactory<CtPreview> STAGE_FACTORY = new StageFactory<>("/ui/preview.fxml", STAGE);

    @FXML
    Label nameLabel, authorLabel, publisherLabel, isbnLabel;
    @FXML
    Label yearLabel, pagesLabel;
    @FXML
    TextArea annotationArea, booksArea;

    Volume volume;
    VolCallback callback;

    public static CtPreview instance(Window parent, Modality modality) {
        return STAGE_FACTORY.newStage(parent, modality);
    }

    public CtPreview withVolume(Volume volume, VolCallback callback) {
        STAGE.setTitle(volume.title());
        this.volume = volume;
        this.callback = callback;
        this.volume.fill(
                authorLabel,
                nameLabel,
                publisherLabel,
                yearLabel,
                isbnLabel,
                pagesLabel,
                booksArea,
                annotationArea
        );
        return this;
    }

    public Stage toStage() {
        return STAGE;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        annotationArea.setEditable(false);
        booksArea.setEditable(false);
    }

    @FXML
    void close() {
        STAGE.close();
    }

    @FXML
    public void editBook() {
        callback.call(volume);
        STAGE.close();
    }
}
