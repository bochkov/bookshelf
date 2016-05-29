package com.sergeybochkov.bookshelf.fx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
    private TextArea annotationArea;
    @FXML
    private Button okButton;

    private Stage thisStage;
    private BookCallback callback;
    private Book book;

    public void setStage(Stage stage) {
        thisStage = stage;
    }

    public void setMode(int mode) {
        okButton.setText(mode == MODE_ADD ? "Добавить" : "Сохранить");
        if (thisStage != null)
            thisStage.setTitle(mode == MODE_ADD ? "Добавление записи" : "Редактирование записи");
    }

    public void setBook(Book book) {
        this.book = book == null ? new Book() : book;
        updateFields();
    }

    public void setCallback(BookCallback callback) {
        this.callback = callback;
    }

    @FXML
    public void exit() {
        this.callback = null;
        if (thisStage != null)
            thisStage.close();
    }

    public void updateFields() {
        authorField.setText(book.getAuthor());
        nameField.setText(book.getName());
        publisherField.setText(book.getPublisher());
        yearField.setText(book.getYear());
        isbnField.setText(book.getIsbn());
        if (book.getPages() == null)
            pagesField.setText("");
        else
            pagesField.setText(String.valueOf(book.getPages()));
        annotationArea.setText(book.getAnnotation());

        authorField.requestFocus();
    }

    @FXML
    public void saveBook() {
        book.setAuthor(authorField.getText());
        book.setName(nameField.getText());
        book.setPublisher(publisherField.getText());
        book.setYear(yearField.getText());
        book.setIsbn(isbnField.getText());
        if (pagesField.getText().equals(""))
            book.setPages(null);
        else
            book.setPages(Integer.parseInt(pagesField.getText()));
        book.setAnnotation(annotationArea.getText());

        callback.call(book);
        exit();
    }
}
