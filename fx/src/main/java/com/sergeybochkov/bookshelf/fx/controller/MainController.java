package com.sergeybochkov.bookshelf.fx.controller;

import com.sergeybochkov.bookshelf.fx.config.ControllersConfig;
import com.sergeybochkov.bookshelf.fx.model.Book;
import com.sergeybochkov.bookshelf.fx.service.BookService;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.ArrayList;
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
    private Label countLabel;
    @FXML
    private TextField searchField;
    @FXML
    private MenuBar menuBar;
    @FXML
    private MenuItem editBookMenuItem, deleteBookMenuItem;
    @FXML
    private Button editBookButton, deleteBookButton;

    private ObservableList<Book> data = FXCollections.emptyObservableList();
    private Stage detailStage;

    @FXML
    @SuppressWarnings("unused")
    public void initialize() {
        menuBar.setUseSystemMenuBar(true);

        bookTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        bookTable.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2)
                editBook();
        });
        bookTable.setRowFactory(tr -> new TableRow<Book>() {
            private Tooltip tt = new Tooltip();

            private void hackTooltipStartTiming(Tooltip tooltip) {
                try {
                    Field fieldBehavior = tooltip.getClass().getDeclaredField("BEHAVIOR");
                    fieldBehavior.setAccessible(true);
                    Object objBehavior = fieldBehavior.get(tooltip);

                    Field fieldTimer = objBehavior.getClass().getDeclaredField("hideTimer");
                    fieldTimer.setAccessible(true);
                    Timeline objTimer = (Timeline) fieldTimer.get(objBehavior);

                    objTimer.getKeyFrames().clear();
                    objTimer.getKeyFrames().add(new KeyFrame(new Duration(200000)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void updateItem(Book book, boolean empty) {
                super.updateItem(book, empty);
                setTooltip(null);
                if (book == null || book.getAnnotation() == null || book.getAnnotation().equals("")) {
                    if (getStyleClass().contains("annotated"))
                        getStyleClass().remove("annotated");
                    if (!getStyleClass().contains("not-annotated"))
                        getStyleClass().add("not-annotated");
                } else {
                    tt.setText(book.getAnnotation());
                    tt.setWrapText(true);
                    hackTooltipStartTiming(tt);
                    tt.setPrefWidth(400D);
                    setTooltip(tt);
                    if (getStyleClass().contains("not-annotated"))
                        getStyleClass().remove("not-annotated");
                    if (!getStyleClass().contains("annotated"))
                        getStyleClass().add("annotated");
                }
            }
        });

        ObservableBooleanValue isSelected = bookTable.getSelectionModel().selectedIndexProperty().isEqualTo(-1);
        editBookButton.disableProperty().bind(isSelected);
        deleteBookButton.disableProperty().bind(isSelected);
        editBookMenuItem.disableProperty().bind(isSelected);
        deleteBookMenuItem.disableProperty().bind(isSelected);
    }

    @PostConstruct
    public void init() {
        TableColumn<Book, String> nameColumn = new TableColumn<>("Название");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Book, String> authorColumn = new TableColumn<>("Автор");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

        TableColumn<Book, String> yearColumn = new TableColumn<>("Год издания");
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));

        bookTable.getColumns().add(0, authorColumn);
        bookTable.getColumns().add(1, nameColumn);
        bookTable.getColumns().add(2, yearColumn);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            data.removeAll();
            if (newValue.startsWith("{")) {
                List<Book> allBooks = bookService.findAll();
                allBooks.retainAll(match(newValue));
                data.setAll(allBooks);
            }
            else
                data.setAll(bookService.findOr(newValue));
        });
    }

    public void fillData() {
        try {
            data = FXCollections.observableArrayList(bookService.findAll());
        }
        catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No connection with database. Program will be closed");
            alert.showAndWait();
            exit();
        }

        data.addListener((ListChangeListener<Book>) c -> countLabel.setText("Томов: " + data.size()));
        countLabel.setText("Томов: " + data.size());
        bookTable.itemsProperty().bind(new SimpleListProperty<>(data));
    }

    private List<Book> match(String value) {
        String v = value.replaceAll("\\{|\\}", "");
        List<Book> books = new ArrayList<>();
        for (String token : v.split(";|,")) {
            String[] f = token.split("=");
            if (f.length != 2)
                continue;

            if (books.isEmpty())
                books.addAll(bookService.findByField(f[0], f[1]));
            else
                books.retainAll(bookService.findByField(f[0], f[1]));
        }
        return books;
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
        List<Book> selectedBooks = bookTable.getSelectionModel().getSelectedItems();
        if (selectedBooks.isEmpty())
            return;

        if (confirmDelete(selectedBooks)) {
            bookService.deleteAll(selectedBooks);
            data.removeAll(selectedBooks);
        }
    }

    private boolean confirmDelete(List<Book> books) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение");
        alert.setHeaderText(null);

        if (books.size() == 1) {
            Book book = books.get(0);
            alert.setContentText("Удалить книгу \"" + book.getTitle() + "\" ?");
        } else
            alert.setContentText("Удалить " + books.size() + " книг?");

        Optional<ButtonType> op = alert.showAndWait();
        return op.isPresent() && op.get() == ButtonType.OK;
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

        bookTable.scrollTo(book);
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
