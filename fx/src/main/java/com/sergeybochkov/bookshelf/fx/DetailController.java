package com.sergeybochkov.bookshelf.fx;

import com.sergeybochkov.bookshelf.fx.model.Volume;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;

public class DetailController {

    public static final int MODE_ADD = 0;
    public static final int MODE_EDIT = 1;

    @FXML
    private TextField authorField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField publisherField;
    @FXML
    private TextField yearField;
    @FXML
    private TextField isbnField;
    @FXML
    private TextField pagesField;
    @FXML
    private TextArea annotationArea, booksArea;
    @FXML
    private Button okButton;

    private Stage thisStage;
    private VolumeCallback callback;
    private Volume volume;

    public void setStage(Stage stage) {
        thisStage = stage;
    }

    public void setMode(int mode) {
        okButton.setText(mode == MODE_ADD ? "Добавить" : "Сохранить");
        if (thisStage != null)
            thisStage.setTitle(mode == MODE_ADD ? "Добавление записи" : "Редактирование записи");
    }

    public void setVolume(Volume volume) {
        this.volume = volume == null ? new Volume() : volume;
        updateFields();
    }

    public void setCallback(VolumeCallback callback) {
        this.callback = callback;
    }

    @FXML
    public void exit() {
        this.callback = null;
        if (thisStage != null)
            thisStage.close();
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

    @FXML
    public void saveBook() throws IOException {
        volume.setAuthor(authorField.getText());
        volume.setName(nameField.getText());
        volume.setPublisher(publisherField.getText());
        volume.setYear(yearField.getText());
        volume.setIsbn(isbnField.getText());
        volume.setPages(
                pagesField.getText().equals("") ? null : Integer.parseInt(pagesField.getText()));
        volume.setAnnotation(annotationArea.getText());
        volume.setBooks(Arrays.asList(booksArea.getText().split("\n")));

        try {
            callback.call(volume);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        exit();
    }
}
