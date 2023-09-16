package ru.msvdev.homefinance;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.msvdev.homefinance.data.db.DataFileProvider;
import ru.msvdev.homefinance.stage.MainAppStage;
import ru.msvdev.homefinance.component.SceneLoader;
import ru.msvdev.homefinance.component.TaskExecutor;
import ru.msvdev.homefinance.config.ApplicationConfig;
import ru.msvdev.homefinance.config.AppProperty;

import java.io.IOException;

public class DesktopApplication extends Application {

    private ApplicationContext ctx;

    @Override
    public void init() {
        ctx = new AnnotationConfigApplicationContext(ApplicationConfig.class);
    }

    @Override
    public void start(Stage stage) throws IOException {
        SceneLoader sceneLoader = ctx.getBean(SceneLoader.class);
        Scene scene = sceneLoader.load("/view/main-app-view.fxml");

        AppProperty appProperty = ctx.getBean(AppProperty.class);

        ctx.getBean(MainAppStage.class).setStage(stage);

        stage.setTitle(appProperty.getApplicationName() + " " + appProperty.getApplicationVersion());
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        DataFileProvider dataFileProvider = ctx.getBean(DataFileProvider.class);
        dataFileProvider.closeFile();

        TaskExecutor taskExecutor = ctx.getBean(TaskExecutor.class);
        taskExecutor.shutdownNow();
    }

    public static void main(String[] args) {
        launch();
    }
}