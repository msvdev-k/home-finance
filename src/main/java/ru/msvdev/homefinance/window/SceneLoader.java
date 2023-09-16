package ru.msvdev.homefinance.window;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;


@Component
public class SceneLoader {

    private final ApplicationContext ctx;

    @Autowired
    public SceneLoader(ApplicationContext ctx) {
        this.ctx = ctx;
    }

    public Scene load(String fxmlPath) throws IOException {
        URL resourceUrl = Objects.requireNonNull(SceneLoader.class.getResource(fxmlPath));

        FXMLLoader fxmlLoader = new FXMLLoader(resourceUrl);
        fxmlLoader.setControllerFactory(ctx::getBean);

        return new Scene(fxmlLoader.load());
    }
}
