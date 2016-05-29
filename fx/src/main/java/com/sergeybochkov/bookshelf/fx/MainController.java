package com.sergeybochkov.bookshelf.fx;

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
import javafx.scene.input.MouseButton;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

public class MainController {

    private ControllersConfig controllers;

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

    private ObservableList<Book> data = FXCollections.observableArrayList();
    private Stage detailStage;
    private Client client;

    @FXML
    @SuppressWarnings("unused")
    public void initialize() {
        menuBar.setUseSystemMenuBar(true);

        configureTable();
        bind();

        countLabel.setText("Томов: " + data.size());
        data.addListener((ListChangeListener<Book>) c -> countLabel.setText("Томов: " + data.size()));

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            data.setAll(client.find(newValue));
            /*
            if (newValue.startsWith("{")) {
                List<Book> allBooks = client.findAll();
                allBooks.retainAll(match(newValue));
                data.setAll(allBooks);
            }
            else
                data.setAll(client.findOr(newValue));
                */
        });
    }

    private void configureTable() {
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
    }

    private void bind() {
        ObservableBooleanValue isSelected = bookTable.getSelectionModel().selectedIndexProperty().isEqualTo(-1);
        editBookButton.disableProperty().bind(isSelected);
        deleteBookButton.disableProperty().bind(isSelected);
        editBookMenuItem.disableProperty().bind(isSelected);
        deleteBookMenuItem.disableProperty().bind(isSelected);

        bookTable.itemsProperty().bind(new SimpleListProperty<>(data));
    }

    public void start() {
        controllers = Application.getControllers();
        client = new Client("127.0.0.1", 8080);
        try {
            data.setAll(client.findAll());
        }
        catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No connection with database. Program will be closed");
            alert.showAndWait();
            exit();
        }
    }

    /*
    private List<Book> match(String value) {
        String v = value.replaceAll("\\{|\\}", "");
        List<Book> books = new ArrayList<>();
        for (String token : v.split(";|,")) {
            String[] f = token.split("=");
            if (f.length != 2)
                continue;

            if (books.isEmpty())
                books.addAll(client.findByField(f[0], f[1]));
            else
                books.retainAll(client.findByField(f[0], f[1]));
        }
        return books;
    }
    */

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
        initDetailStage(DetailController.MODE_EDIT);
        controllers.detailController().setBook(selectedBook);
        controllers.detailController().setCallback(book -> addToTable(client.save(book)));
        detailStage.show();
    }

    @FXML
    public void deleteBook() {
        List<Book> selectedBooks = bookTable.getSelectionModel().getSelectedItems();
        if (selectedBooks.isEmpty())
            return;

        if (confirmDelete(selectedBooks)) {
            List<Book> deleted = client.delete(selectedBooks);
            data.removeAll(deleted);
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
        initDetailStage(DetailController.MODE_ADD);
        controllers.detailController().setBook(null);
        controllers.detailController().setCallback(book -> addToTable(client.save(book)));
        detailStage.show();
    }

    public void addToTable(Book book) {
        if (data.contains(book))
            data.set(data.indexOf(book), book);
        else
            data.add(book);

        bookTable.scrollTo(book);
    }

    private Stage initDetailStage(int mode) {
        if (detailStage == null) {
            detailStage = new Stage();
            detailStage.initModality(Modality.APPLICATION_MODAL);
            detailStage.setScene(new Scene(controllers.getDetailView()));
            controllers.detailController().setStage(detailStage);
        }

        controllers.detailController().setMode(mode);
        return detailStage;
    }
}
