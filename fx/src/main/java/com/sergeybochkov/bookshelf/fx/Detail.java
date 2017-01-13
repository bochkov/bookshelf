package com.sergeybochkov.bookshelf.fx;

import com.sergeybochkov.bookshelf.fx.fxutil.Target;
import com.sergeybochkov.bookshelf.fx.fxutil.ResultCallback;
import com.sergeybochkov.bookshelf.fx.model.Volume;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

public final class Detail implements Target, ResultCallback {

    private final Stage stage;
    private final Properties properties;

    @FXML
    private TextField authorField, nameField, publisherField, yearField, isbnField, pagesField;
    @FXML
    private TextArea annotationArea, booksArea;
    @FXML
    private Button okButton;

    private Volume volume;
    private ResultCallback.Callback callback;

    public Detail(Stage stage, Properties properties) {
        this.stage = stage;
        this.properties = properties;
    }

    @Override
    public void init() {
    }

    @Override
    public Target callback(ResultCallback.Callback callback) {
        this.callback = callback;
        return this;
    }

    public Detail withVolume(Volume volume) {
        okButton.setText(volume.getId() == null ? "Добавить" : "Сохранить");
        stage.setTitle(volume.getId() == null ? "Добавление записи" : "Редактирование записи");
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
        callback.call(volume);
        stage.close();
    }

    @FXML
    public void close() {
        stage.close();
    }
}
