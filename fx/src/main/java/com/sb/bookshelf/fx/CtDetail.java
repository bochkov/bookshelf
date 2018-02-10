package com.sb.bookshelf.fx;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public final class CtDetail implements Initializable {

    private static final Stage STAGE = new Stage();
    private static final StageFactory<CtDetail> STAGE_FACTORY = new StageFactory<>("/ui/detail.fxml", STAGE);

    @FXML
    TextField nameField, authorField, publisherField, isbnField;
    @FXML
    TextField yearField, pagesField;
    @FXML
    TextArea annotationArea, booksArea;
    @FXML
    Button okButton;

    Volume volume;
    VolCallback callback;

    public static CtDetail instance(Window parent, Modality modality) {
        return STAGE_FACTORY.newStage(parent, modality);
    }

    public CtDetail withVolume(Volume volume, VolCallback callback) {
        STAGE.setTitle(volume.getId() == null ? "Добавление записи" : "Редактирование записи");
        okButton.setText(volume.getId() == null ? "Добавить" : "Сохранить");
        this.callback = callback;
        this.volume = volume;
        updateFields();
        return this;
    }

    public Stage toStage() {
        return STAGE;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    void saveBook() {
        volume.setAuthor(authorField.getText());
        volume.setName(nameField.getText());
        volume.setPublisher(publisherField.getText());
        volume.setYear(yearField.getText());
        volume.setIsbn(isbnField.getText());
        volume.setPages(
                pagesField.getText().equals("") ? null : Integer.parseInt(pagesField.getText()));
        volume.setAnnotation(annotationArea.getText());
        volume.setBooks(Arrays.asList(booksArea.getText().split("\n")));
        callback.call(volume);
        STAGE.close();
    }

    @FXML
    void close() {
        STAGE.close();
    }

    public void updateFields() {
        authorField.setText(volume.getAuthor());
        nameField.setText(volume.getName());
        publisherField.setText(volume.getPublisher());
        yearField.setText(volume.getYear());
        isbnField.setText(volume.getIsbn());
        pagesField.setText(
                volume.getPages() == null ? "" : String.valueOf(volume.getPages()));
        booksArea.setText(
                volume.getBooks() == null ? "" : String.join("\n", volume.getBooks()));
        annotationArea.setText(volume.getAnnotation());
        authorField.requestFocus();
    }
}
