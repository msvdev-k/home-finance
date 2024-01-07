package ru.msvdev.homefinance.scene;

import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.msvdev.desktop.utils.scene.ApplicationScene;
import ru.msvdev.desktop.utils.scene.SceneLoader;
import ru.msvdev.homefinance.config.AppProperty;


@Component
@RequiredArgsConstructor
public class OpenFileApplicationScene implements ApplicationScene {

    private final SceneLoader sceneLoader;
    private final AppProperty appProperty;

    @Override
    public <T extends ApplicationScene> void switchScene(Stage primaryStage, Class<T> currentScene) throws Exception {
        Scene scene = sceneLoader.load("/view/scene/open-file-scene-view.fxml");
        String title = appProperty.getApplicationName() + " " + appProperty.getApplicationVersion();

        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.centerOnScreen();
        primaryStage.setTitle(title);
        primaryStage.setResizable(true);

        if (!primaryStage.isShowing()) {
            primaryStage.show();
        }
    }
}
