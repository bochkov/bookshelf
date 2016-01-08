package com.sergeybochkov.bookshelf.fx.config;

import com.sergeybochkov.bookshelf.fx.controller.DetailController;
import com.sergeybochkov.bookshelf.fx.controller.MainController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class ControllersConfig {

    @Bean(name = "mainView")
    @Qualifier(value = "mainView")
    public View getMainView() throws IOException {
        return loadView("ui/main.fxml");
    }

    @Bean(name = "detailView")
    @Qualifier(value = "detailView")
    public View getDetailView() throws IOException {
        return loadView("ui/detail.fxml");
    }

    @Bean
    public MainController mainController() throws IOException {
        return (MainController) getMainView().getController();
    }

    @Bean
    public DetailController detailController() throws IOException {
        return (DetailController) getDetailView().getController();
    }

    protected View loadView(String url) throws IOException {
        InputStream fxmlStream = null;
        try {
            fxmlStream = getClass().getClassLoader().getResourceAsStream(url);
            FXMLLoader loader = new FXMLLoader();
            loader.load(fxmlStream);
            return new View(loader.getRoot(), loader.getController());
        }
        finally {
            if (fxmlStream != null)
                fxmlStream.close();
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
