package com.sergeybochkov.bookshelf.fx;

import com.sergeybochkov.bookshelf.fx.fxutil.Target;
import com.sergeybochkov.bookshelf.fx.fxutil.VolumeCallback;
import com.sergeybochkov.bookshelf.fx.model.Volume;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;

public final class Detail implements Target, VolumeCallback {

    @FXML
    private TextField authorField, nameField, publisherField, yearField, isbnField, pagesField;
    @FXML
    private TextArea annotationArea, booksArea;
    @FXML
    private Button okButton;

    private final Stage stage;

    private VolumeCallback.Callback volumeCallback = (vol) -> {};
    private Volume volume;

    public Detail(Stage stage) {
        this.stage = stage;
    }

    public Detail withVolume(Volume volume) {
        this.volume = volume;
        updateFields();
        return this;
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
        volumeCallback.call(volume);
        stage.close();
    }

    @Override
    public void show() {
        okButton.setText(volume.getId() == null ? "Добавить" : "Сохранить");
        stage.setTitle(volume.getId() == null ? "Добавление записи" : "Редактирование записи");
        stage.show();
    }

    @FXML
    @Override
    public void close() {
        stage.close();
    }

    @Override
    public Target callback(VolumeCallback.Callback volumeCallback) {
        this.volumeCallback = volumeCallback;
        return this;
    }
}
