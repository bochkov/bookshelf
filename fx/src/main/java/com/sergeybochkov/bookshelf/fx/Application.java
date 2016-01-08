package com.sergeybochkov.bookshelf.fx;

import com.sergeybochkov.bookshelf.fx.config.AbstractJavaFxApplicationSupport;
import com.sergeybochkov.bookshelf.fx.config.ControllersConfig;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Lazy;

@Lazy
@SpringBootApplication
public class Application extends AbstractJavaFxApplicationSupport {

    @Value("${ui.title}")
    private String title;

    @Autowired
    private ControllersConfig.View mainView;

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle(title);
        stage.setScene(new Scene(mainView.getView()));
        stage.setResizable(true);
        stage.centerOnScreen();
        stage.getIcons().add(new Image("ui/logo.png"));
        stage.show();
    }

    public static void main(String[] args) {
        launchApp(Application.class, args);
    }
}
