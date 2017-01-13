package com.sergeybochkov.bookshelf.fx;

import com.sergeybochkov.bookshelf.fx.fxutil.View;
import javafx.application.Application;
import javafx.stage.Stage;

public final class App extends Application {

    private final AppProperties appProperties;
    private final View mainView;

    public App() throws Exception {
        appProperties = new AppProperties();
        appProperties.load();
        mainView = new View("/ui/main.fxml", appProperties)
                .children(
                        new View("/ui/detail.fxml", appProperties),
                        new View("/ui/settings.fxml", appProperties),
                        new View("/ui/about.fxml", appProperties));
    }

    public static void main(String... args) throws Exception {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        appProperties.save();
    }

    @Override
    public void start(Stage stage) throws Exception {
        mainView.stage().show();
        mainView.target(Main.class).start();
    }
}
