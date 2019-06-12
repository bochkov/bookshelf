package com.sb.bookshelf.fx;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public final class CtMain implements Initializable {

    private static final Stage STAGE = new Stage();
    private static final StageFactory<CtMain> STAGE_FACTORY = new StageFactory<>("/ui/main.fxml", STAGE);

    private final ObservableList<Volume> volumes = FXCollections.observableArrayList();

    @FXML
    Button addBookButton, previewBookButton, deleteBookButton;
    @FXML
    Button searchButton;
    @FXML
    MenuItem viewAllMenu, addBookMenu, previewBookMenu, deleteBookMenu;
    @FXML
    TableView<Volume> volumeTable;
    @FXML
    TextField searchField;
    @FXML
    Label countLabel;
    AppProps appProps;

    public static CtMain instance(Window parent) {
        return STAGE_FACTORY.newStage(parent);
    }

    public CtMain properties(AppProps appProps) {
        this.appProps = appProps;
        return this;
    }

    public Stage toStage() {
        return STAGE;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        STAGE.setTitle("BookShelf");
        STAGE.getIcons()
                .add(new Image(getClass().getResourceAsStream("/ui/logo.png")));

        volumeTable.getSelectionModel()
                .setSelectionMode(SelectionMode.MULTIPLE);
        volumeTable.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY)
                    && event.getClickCount() == 2)
                previewBook();
        });
        ObservableBooleanValue isSelected = volumeTable
                .getSelectionModel()
                .selectedIndexProperty()
                .isEqualTo(-1);
        previewBookButton.disableProperty().bind(isSelected);
        deleteBookButton.disableProperty().bind(isSelected);
        previewBookMenu.disableProperty().bind(isSelected);
        deleteBookMenu.disableProperty().bind(isSelected);
        volumeTable.itemsProperty().bind(new SimpleListProperty<>(volumes));
        countLabel.setText(String.format("Томов: %d", volumes.size()));
        volumes.addListener((ListChangeListener<Volume>) c ->
                countLabel.setText(
                        String.format("Томов: %d", volumes.size())));
        searchButton.disableProperty().bind(searchField.textProperty().isEmpty());

        STAGE.setOnShown(ev -> onShown());
    }

    private void onShown() {
        try {
            volumes.clear();
            countLabel.setText(
                    String.format(
                            "Томов: %d",
                            new Volumes(
                                    appProps.hostProperty().get(),
                                    appProps.portProperty().get(),
                                    appProps.userProperty().get(),
                                    appProps.passProperty().get()
                            ).count()
                    )
            );
        } catch (IOException ex) {
            new Alert(Alert.AlertType.ERROR, ex.getMessage())
                    .showAndWait();
        }
    }

    @FXML
    void viewAll() {
        try {
            volumes.addAll(
                    new Volumes(
                            appProps.hostProperty().get(),
                            appProps.portProperty().get(),
                            appProps.userProperty().get(),
                            appProps.passProperty().get()
                    ).findAll()
            );
        } catch (IOException ex) {
            new Alert(Alert.AlertType.ERROR, ex.getMessage())
                    .showAndWait();
        }
    }

    @FXML
    void showSettings() {
        CtSettings.instance(STAGE.getOwner())
                .properties(appProps)
                .toStage(this::clearSearch)
                .showAndWait();
    }

    @FXML
    void close() {
        STAGE.close();
    }

    @FXML
    void addBook() {
        CtDetail.instance(STAGE.getOwner(), Modality.APPLICATION_MODAL)
                .withVolume(
                        new Volume(),
                        this::saveAndShow)
                .toStage()
                .showAndWait();
    }

    private void saveAndShow(Volume volume) {
        try {
            // save
            volume.setId(
                    new Volumes(
                            appProps.hostProperty().get(),
                            appProps.portProperty().get(),
                            appProps.userProperty().get(),
                            appProps.passProperty().get()
                    ).save(volume).getId()
            );
            // add to table
            if (volumes.contains(volume))
                volumes.set(volumes.indexOf(volume), volume);
            else
                volumes.add(volume);
            volumeTable.scrollTo(volume);
        } catch (IOException ex) {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR, ex.getMessage())
                    .showAndWait();
        }
    }

    @FXML
    void previewBook() {
        List<Volume> selectedRows = volumeTable.getSelectionModel().getSelectedItems();
        if (!selectedRows.isEmpty()) {
            CtPreview.instance(STAGE.getOwner(), Modality.APPLICATION_MODAL)
                    .withVolume(
                            selectedRows.get(0),
                            v -> CtDetail.instance(STAGE.getOwner(), Modality.APPLICATION_MODAL)
                                    .withVolume(v, this::saveAndShow)
                                    .toStage()
                                    .showAndWait())
                    .toStage()
                    .showAndWait();
        }
    }

    private boolean confirmDelete(List<Volume> volumes) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение");
        alert.setHeaderText(null);
        alert.setContentText(volumes.size() > 0 ?
                String.format("Удалить книгу \"%s\"?", volumes.get(0).title()) :
                String.format("Удалить %s книг?", volumes.size()));
        Optional<ButtonType> op = alert.showAndWait();
        return op.isPresent() && op.get() == ButtonType.OK;
    }

    @FXML
    void deleteBook() {
        List<Volume> selectedVolumes = volumeTable.getSelectionModel().getSelectedItems();
        if (!selectedVolumes.isEmpty()) {
            if (confirmDelete(selectedVolumes)) {
                try {
                    new Volumes(
                            appProps.hostProperty().get(),
                            appProps.portProperty().get(),
                            appProps.userProperty().get(),
                            appProps.passProperty().get()
                    ).delete(selectedVolumes);
                    volumes.removeAll(selectedVolumes);
                } catch (IOException ex) {
                    new Alert(Alert.AlertType.ERROR, ex.getMessage())
                            .showAndWait();
                }
            }
        }
    }

    @FXML
    void showAbout() {
        CtAbout.instance(STAGE.getOwner())
                .toStage()
                .showAndWait();
    }

    @FXML
    void search() {
        try {
            volumes.setAll(
                    new Volumes(
                            appProps.hostProperty().get(),
                            appProps.portProperty().get(),
                            appProps.userProperty().get(),
                            appProps.passProperty().get()
                    ).find(searchField.getText()));
        } catch (IOException ex) {
            new Alert(Alert.AlertType.ERROR, ex.getMessage())
                    .showAndWait();
        }
    }

    @FXML
    void clearSearch() {
        searchField.setText("");
        onShown();
    }

    @FXML
    void showSearchHelp() {
        String message = "Существует возможность указать в каком поле искать." +
                "для этого необходимо поисковый запрос заключить в фигурные скобки " +
                "и указать поле, например {author=Булгаков,name=Мастер}.\n" +
                "Поиск поддерживает поля Год издания (year), Автор (author), " +
                "Название(name) и Текст Аннотации(annotation)";
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.setHeaderText("Возможности поиска");
        alert.showAndWait();
    }
}
