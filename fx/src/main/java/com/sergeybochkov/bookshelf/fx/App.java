package com.sergeybochkov.bookshelf.fx;

import com.sergeybochkov.bookshelf.fx.fxutil.View;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public final class App extends Application {

    private final AppProperties appProperties;
    private final View mainView;
    private final Map<String, View> views = new HashMap<>();

    public App() throws Exception {
        appProperties = new AppProperties();
        mainView = new View("/ui/main.fxml", Main.class);
        views.put("detail", new View("/ui/detail.fxml", Detail.class, mainView));
        views.put("settings", new View("/ui/settings.fxml", Settings.class, mainView));
        views.put("about", new View("/ui/about.fxml", About.class, mainView));
    }

    @Override
    public void init() throws Exception {
        appProperties.load();
    }

    @Override
    public void stop() throws Exception {
        appProperties.save();
    }

    @Override
    public void start(Stage stage) throws Exception {
        mainView
                .target(Main.class)
                .withViews(views)
                .withProperties(appProperties)
                .show();
    }

    public static void main(String... args) throws Exception {
        launch(args);
    }
}
