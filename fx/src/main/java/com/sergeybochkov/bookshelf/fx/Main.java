package com.sergeybochkov.bookshelf.fx;

import com.sergeybochkov.bookshelf.fx.fxutil.MainTarget;
import com.sergeybochkov.bookshelf.fx.fxutil.View;
import com.sergeybochkov.bookshelf.fx.graphic.TableTooltip;
import com.sergeybochkov.bookshelf.fx.model.Volume;
import javafx.beans.binding.Bindings;
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

public final class Main implements MainTarget {

    private final Stage stage;
    private final AppProperties properties;
    private final Map<String, View> views = new HashMap<>();

    @FXML
    private TableView<Volume> volumeTable;
    @FXML
    private Label countLabel;
    @FXML
    private TextField searchField;
    @FXML
    private MenuItem addBookMenu, editBookMenu, deleteBookMenu;
    @FXML
    private Button addBookButton, editBookButton, deleteBookButton;

    private Client client;
    private ObservableList<Volume> volumes = FXCollections.observableArrayList();

    public Main(Stage stage, AppProperties properties) {
        this.stage = stage;
        this.properties = properties;
        this.stage.setTitle("BookShelf");
        this.stage.getIcons().add(new Image(getClass().getResourceAsStream("/ui/logo.png")));
    }

    @Override
    public void init() {
        volumeTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        volumeTable.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2)
                editBook();
        });
        volumeTable.setRowFactory(new TableTooltip());
        ObservableBooleanValue isSelected = volumeTable.getSelectionModel().selectedIndexProperty().isEqualTo(-1);
        editBookButton.disableProperty().bind(isSelected);
        deleteBookButton.disableProperty().bind(isSelected);
        editBookMenu.disableProperty().bind(isSelected);
        deleteBookMenu.disableProperty().bind(isSelected);
        volumeTable.itemsProperty().bind(new SimpleListProperty<>(volumes));
        countLabel.setText("Томов: " + volumes.size());
        volumes.addListener((ListChangeListener<Volume>) c -> countLabel.setText("Томов: " + volumes.size()));
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                volumes.setAll(client.find(newValue));
            } catch (IOException e) {
                //e.printStackTrace();
            }
        });
    }

    @Override
    public void withViews(Map<String, View> views) {
        this.views.putAll(views);
        this.views.get("detail").target(Detail.class).callback(obj -> addToTable(client.save((Volume) obj)));
        this.views.get("settings").target(Settings.class).callback(this::start);
    }

    public void start() {
        volumes.clear();
        client = new Client(
                properties.host(),
                properties.port());
        client.connectedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue)
                volumes.clear();
        });
        addBookMenu.disableProperty().bind(Bindings.not(client.connectedProperty()));
        addBookButton.disableProperty().bind(Bindings.not(client.connectedProperty()));
        try {
            volumes.setAll(client.findAll());
        } catch (Exception ex) {
            new Alert(Alert.AlertType.ERROR, "Нет соединения с сервером. Проверьте настройки")
                    .showAndWait();
        }
    }

    @FXML
    private void showSettings() {
        views.get("settings").stage().show();
    }

    @FXML
    private void showAbout() {
        views.get("about").stage().show();
    }

    @FXML
    public void addBook() {
        views.get("detail")
                .target(Detail.class)
                .withVolume(new Volume());
        views.get("detail")
                .stage()
                .show();
    }

    @FXML
    public void editBook() {
        List<Volume> selectedRows = volumeTable.getSelectionModel().getSelectedItems();
        if (!selectedRows.isEmpty()) {
            views.get("detail")
                    .target(Detail.class)
                    .withVolume(selectedRows.get(0));
            views.get("detail")
                    .stage()
                    .show();
        }
    }

    @FXML
    public void deleteBook() throws IOException {
        List<Volume> selectedVolumes = volumeTable.getSelectionModel().getSelectedItems();
        if (!selectedVolumes.isEmpty()) {
            if (confirmDelete(selectedVolumes)) {
                client.delete(selectedVolumes);
                volumes.removeAll(selectedVolumes);
            }
        }
    }

    private boolean confirmDelete(List<Volume> volumes) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение");
        alert.setHeaderText(null);
        alert.setContentText(volumes.size() > 0 ?
                String.format("Удалить книгу \"%s\" ?", volumes.get(0).title()) :
                String.format("Удалить %s книг?", volumes.size()));
        Optional<ButtonType> op = alert.showAndWait();
        return op.isPresent() && op.get() == ButtonType.OK;
    }

    public void addToTable(Volume volume) {
        if (volumes.contains(volume))
            volumes.set(volumes.indexOf(volume), volume);
        else
            volumes.add(volume);

        volumeTable.scrollTo(volume);
    }

    @FXML
    public void close() {
        stage.close();
    }
}
