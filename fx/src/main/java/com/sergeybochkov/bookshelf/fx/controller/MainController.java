package com.sergeybochkov.bookshelf.fx.controller;

import com.sergeybochkov.bookshelf.fx.config.ControllersConfig;
import com.sergeybochkov.bookshelf.fx.model.Book;
import com.sergeybochkov.bookshelf.fx.service.BookService;
import javafx.beans.value.ObservableBooleanValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

public class MainController {

    @Autowired
    private BookService bookService;

    @Autowired
    private ControllersConfig.View detailView;

    @FXML
    private TableView<Book> bookTable;
    @FXML
    private TextField searchField;
    @FXML
    private Button editBookButton, deleteBookButton;

    private ObservableList<Book> data;
    private Stage detailStage;

    @FXML
    public void initialize() {
        bookTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        ObservableBooleanValue isSelected = bookTable.getSelectionModel().selectedIndexProperty().isEqualTo(-1);
        editBookButton.disableProperty().bind(isSelected);
        deleteBookButton.disableProperty().bind(isSelected);
    }

    @PostConstruct
    public void init() {
        List<Book> bookList = bookService.findAll();
        data = FXCollections.observableArrayList(bookList);

        TableColumn<Book, String> nameColumn = new TableColumn<>("Название");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Book, String> authorColumn = new TableColumn<>("Автор");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

        TableColumn<Book, String> yearColumn = new TableColumn<>("Год издания");
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));

        bookTable.getColumns().setAll(nameColumn, authorColumn, yearColumn);

        FilteredList<Book> filtered = new FilteredList<>(data, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filtered.setPredicate(book -> {
                if (newValue == null || newValue.isEmpty())
                    return true;
                if (book.getName() != null && book.getName().toLowerCase().contains(newValue.toLowerCase()) ||
                        book.getAuthor() != null && book.getAuthor().toLowerCase().contains(newValue.toLowerCase()) ||
                        book.getYear() != null && book.getYear().toLowerCase().contains(newValue.toLowerCase()))
                    return true;

                return false;
            });
        });

        bookTable.setItems(filtered);
    }

    @FXML
    public void exit() {
        System.exit(0);
    }

    @FXML
    public void editBook() {
        List<Book> selectedRows = bookTable.getSelectionModel().getSelectedItems();
        if (selectedRows.isEmpty())
            return;

        Book selectedBook = selectedRows.get(0);
        Stage stage = createStage(DetailController.MODE_EDIT);
        ((DetailController) detailView.getController()).setBook(selectedBook);
        stage.show();
    }

    @FXML
    public void deleteBook() {
        List<Book> selectedRows = bookTable.getSelectionModel().getSelectedItems();
        if (selectedRows.isEmpty())
            return;

        if (confirmDelete(selectedRows)) {
            bookService.deleteAll(selectedRows);
        }
    }

    private boolean confirmDelete(List<Book> books) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение");
        alert.setHeaderText(null);

        if (books.size() == 1) {
            Book book = books.get(0);
            String bookStr = book.getAuthor() + ". " + book.getName();
            alert.setContentText("Удалить книгу " + bookStr + "?");
        } else
            alert.setContentText("Удалить " + books.size() + " книг?");

        Optional<ButtonType> op = alert.showAndWait();
        return op.get() == ButtonType.OK;
    }

    @FXML
    public void addBook() {
        Stage stage = createStage(DetailController.MODE_ADD);
        ((DetailController) detailView.getController()).setBook(null);
        stage.show();
    }

    public void addToTable(Book book) {
        if (data.contains(book))
            data.set(data.indexOf(book), book);
        else
            data.add(book);
    }

    private Stage createStage(int mode) {
        if (detailStage != null) {
            DetailController controller = (DetailController) detailView.getController();
            controller.setMode(mode);
            return detailStage;
        }

        detailStage = new Stage();
        detailStage.initModality(Modality.APPLICATION_MODAL);
        detailStage.setScene(new Scene(detailView.getView()));
        DetailController controller = (DetailController) detailView.getController();
        controller.setStage(detailStage);
        controller.setMode(mode);
        return detailStage;
    }
}
