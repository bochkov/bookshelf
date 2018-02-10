package com.sb.bookshelf.fx;

import javafx.application.Application;
import javafx.stage.Stage;

public final class App extends Application {

    private final AppProps props = new AppProps();

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void init() throws Exception {
        props.load();
        super.init();
    }

    @Override
    public void start(Stage primaryStage) {
        CtMain.instance(primaryStage)
                .properties(props)
                .toStage()
                .show();
    }

    @Override
    public void stop() throws Exception {
        props.save();
        super.stop();
    }
}
