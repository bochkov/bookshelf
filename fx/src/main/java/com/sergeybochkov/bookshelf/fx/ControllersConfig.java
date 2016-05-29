package com.sergeybochkov.bookshelf.fx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.io.InputStream;

public class ControllersConfig {

    private View mainView;
    private View detailView;

    public ControllersConfig() throws IOException {
        mainView = loadView("ui/main.fxml");
        detailView = loadView("ui/detail.fxml");
    }

    public Parent getMainView() {
        return mainView.getView();
    }

    public MainController mainController() {
        return (MainController) mainView.getController();
    }

    public Parent getDetailView() {
        return detailView.getView();
    }

    public DetailController detailController() {
        return (DetailController) detailView.getController();
    }

    protected View loadView(String url) throws IOException {
        try (InputStream fxmlStream = getClass().getClassLoader().getResourceAsStream(url)) {
            FXMLLoader loader = new FXMLLoader();
            loader.load(fxmlStream);
            return new View(loader.getRoot(), loader.getController());
        }
    }

    public class View {
        private Parent view;
        private Object controller;

        public View(Parent view, Object controller) {
            this.view = view;
            this.controller = controller;
        }

        public Parent getView() {
            return view;
        }

        public void setView(Parent view) {
            this.view = view;
        }

        public Object getController() {
            return controller;
        }

        public void setController(Object controller) {
            this.controller = controller;
        }
    }
}
