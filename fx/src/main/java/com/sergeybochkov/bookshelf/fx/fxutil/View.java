package com.sergeybochkov.bookshelf.fx.fxutil;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public final class View {

    private final Stage stage;
    private final Target target;

    public View(String location, Class<? extends Target> targetClass) throws Exception {
        this(location, targetClass, null);
    }

    public View(String location, Class<? extends Target> targetClass, View parent) throws Exception {
        stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(location));
        target = targetClass.getConstructor(Stage.class).newInstance(stage);
        if (parent != null) {
            stage.initOwner(parent.stage());
            stage.getIcons().addAll(parent.stage().getIcons());
            stage.setTitle(parent.stage().getTitle());
        }
        loader.setController(target);
        Parent view = loader.load();
        if (target instanceof InitTarget)
            ((InitTarget) target).init();
        stage.setScene(new Scene(view));
    }

    public <T> T target(Class<T> castClz) {
        return castClz.cast(target());
    }

    public Target target() {
        return target;
    }

    public Stage stage() {
        return stage;
    }
}
