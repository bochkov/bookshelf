package com.sergeybochkov.bookshelf.fx;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Application extends javafx.application.Application {

    private static ControllersConfig controllers;

    public static ControllersConfig getControllers() {
        return controllers;
    }

    @Override
    public void init() throws Exception {
        controllers = new ControllersConfig();
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Bookshelf");
        stage.setScene(new Scene(controllers.getMainView()));
        stage.setResizable(true);
        stage.centerOnScreen();
        stage.getIcons().add(new Image("ui/logo.png"));
        stage.show();

        controllers.mainController().start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
