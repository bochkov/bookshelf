package com.sergeybochkov.bookshelf.fx;

import com.sergeybochkov.bookshelf.fx.fxutil.InitTarget;
import com.sergeybochkov.bookshelf.fx.fxutil.View;
import com.sergeybochkov.bookshelf.fx.graphic.TableTooltip;
import com.sergeybochkov.bookshelf.fx.model.Volume;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class Main implements InitTarget {

    private final Stage stage;
    private final Map<String, View> views = new HashMap<>();

    private AppProperties appProperties;
    private Client client;
    private ObservableList<Volume> data = FXCollections.observableArrayList();

    @FXML
    private TableView<Volume> bookTable;
    @FXML
    private Label countLabel;
    @FXML
    private TextField searchField;
    @FXML
    private MenuItem addBookMenu, editBookMenu, deleteBookMenu;
    @FXML
    private Button addBookButton, editBookButton, deleteBookButton;

    public Main(Stage stage) {
        this.stage = stage;
        this.stage.setTitle("BookShelf");
        this.stage.getIcons().add(new Image(getClass().getResourceAsStream("/ui/logo.png")));
    }

    public Main withProperties(AppProperties appProperties) {
        this.appProperties = appProperties;
        return this;
    }

    @Override
    public void init() {
        bookTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        bookTable.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2)
                editBook();
        });
        bookTable.setRowFactory(new TableTooltip());
        ObservableBooleanValue isSelected = bookTable.getSelectionModel().selectedIndexProperty().isEqualTo(-1);
        editBookButton.disableProperty().bind(isSelected);
        deleteBookButton.disableProperty().bind(isSelected);
        editBookMenu.disableProperty().bind(isSelected);
        deleteBookMenu.disableProperty().bind(isSelected);
        bookTable.itemsProperty().bind(new SimpleListProperty<>(data));
        countLabel.setText("Томов: " + data.size());
        data.addListener((ListChangeListener<Volume>) c -> countLabel.setText("Томов: " + data.size()));
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                data.setAll(client.find(newValue));
            } catch (IOException e) {
                //e.printStackTrace();
            }
        });
    }

    public void start() {
        client = new Client(appProperties.host(), appProperties.port());
        try {
            data.setAll(client.findAll());
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Нет соединения с сервером. Проверьте настройки");
            alert.showAndWait();
        }
    }

    @FXML
    private void showSettings() {
        views.get("settings")
                .target(Settings.class)
                .withProperties(appProperties)
                .callback(this::start)
                .show();
    }

    @FXML
    private void showAbout() {
        views.get("about")
                .target(About.class)
                .show();
    }

    @FXML
    public void addBook() {
        views.get("detail")
                .target(Detail.class)
                .withVolume(new Volume())
                .callback(volume -> addToTable(client.save(volume)))
                .show();
    }

    @FXML
    public void editBook() {
        List<Volume> selectedRows = bookTable.getSelectionModel().getSelectedItems();
        if (!selectedRows.isEmpty()) {
            views.get("detail")
                    .target(Detail.class)
                    .withVolume(selectedRows.get(0))
                    .callback(volume -> addToTable(client.save(volume)))
                    .show();
        }
    }

    @FXML
    public void deleteBook() throws IOException {
        List<Volume> selectedVolumes = bookTable.getSelectionModel().getSelectedItems();
        if (!selectedVolumes.isEmpty()) {
            if (confirmDelete(selectedVolumes)) {
                client.delete(selectedVolumes);
                data.removeAll(selectedVolumes);
            }
        }
    }

    private boolean confirmDelete(List<Volume> volumes) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение");
        alert.setHeaderText(null);

        if (volumes.size() == 1) {
            Volume volume = volumes.get(0);
            alert.setContentText("Удалить книгу \"" + volume.title() + "\" ?");
        } else
            alert.setContentText("Удалить " + volumes.size() + " книг?");

        Optional<ButtonType> op = alert.showAndWait();
        return op.isPresent() && op.get() == ButtonType.OK;
    }

    public void addToTable(Volume volume) {
        if (data.contains(volume))
            data.set(data.indexOf(volume), volume);
        else
            data.add(volume);

        bookTable.scrollTo(volume);
    }

    public Main withViews(Map<String, View> views) {
        this.views.putAll(views);
        return this;
    }

    @Override
    public void show() {
        stage.show();
        start();
    }

    @FXML
    @Override
    public void close() {
        stage.close();
    }
}
