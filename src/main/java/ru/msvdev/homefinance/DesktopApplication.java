package ru.msvdev.homefinance;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.msvdev.homefinance.config.AppConstant;
import ru.msvdev.homefinance.config.AppProperty;
import ru.msvdev.homefinance.config.ApplicationConfig;
import ru.msvdev.homefinance.data.db.DataFileProvider;
import ru.msvdev.homefinance.task.operation.TaskExecutor;
import ru.msvdev.homefinance.window.MainAppStage;
import ru.msvdev.homefinance.window.SceneLoader;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


public class DesktopApplication extends Application {
    static final Logger logger = LoggerFactory.getLogger(AppConstant.LOGGER_NAME);
    static long startTime;

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

        long startedTime = System.nanoTime();
        logger.info("Application started ({} s)", (startedTime - startTime) * 1e-9);
    }

    @Override
    public void stop() {
        DataFileProvider dataFileProvider = ctx.getBean(DataFileProvider.class);
        dataFileProvider.closeFile();

        TaskExecutor taskExecutor = ctx.getBean(TaskExecutor.class);
        taskExecutor.shutdownNow();
    }

    public static void main(String[] args) {
        logger.info("Application start");
        startTime = System.nanoTime();

        launch();

        long workingTime = System.nanoTime() - startTime;
        long days = TimeUnit.NANOSECONDS.toDays(workingTime);
        long hours = TimeUnit.NANOSECONDS.toHours(workingTime);
        long minutes = TimeUnit.NANOSECONDS.toMinutes(workingTime);
        long seconds = TimeUnit.NANOSECONDS.toSeconds(workingTime);
        logger.info("Application stop (working {} days, {} hours, {} minutes and {} seconds)",
                days, hours - days * 24, minutes - hours * 60, seconds - minutes * 60);
    }
}