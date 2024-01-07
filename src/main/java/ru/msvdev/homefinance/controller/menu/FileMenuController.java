package ru.msvdev.homefinance.controller.menu;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import ru.msvdev.desktop.utils.event.emitter.CloseWindowEventEmitter;
import ru.msvdev.desktop.utils.event.emitter.DataAccessEventEmitter;
import ru.msvdev.desktop.utils.scene.SceneSwitcher;
import ru.msvdev.desktop.utils.task.TaskBuilder;
import ru.msvdev.desktop.utils.scene.PrimaryStage;
import ru.msvdev.homefinance.config.AppProperty;
import ru.msvdev.homefinance.scene.CloseFileApplicationScene;
import ru.msvdev.homefinance.scene.OpenFileApplicationScene;
import ru.msvdev.homefinance.task.file.CloseFileTaskBuilder;
import ru.msvdev.homefinance.task.file.OpenFileTaskBuilder;
import ru.msvdev.homefinance.task.io.csv.ExportToCsvTaskBuilder;
import ru.msvdev.homefinance.task.io.csv.ImportFromCsvTaskBuilder;
import ru.msvdev.homefinance.task.io.xhb.ImportFromXhbTaskBuilder;

import java.io.File;
import java.util.Locale;


@Controller
@RequiredArgsConstructor
public class FileMenuController {

    private final PrimaryStage primaryStage;
    private final SceneSwitcher sceneSwitcher;
    private final TaskBuilder taskBuilder;

    private final AppProperty appProperty;

    private final DataAccessEventEmitter dataAccessEventEmitter;
    private final CloseWindowEventEmitter closeWindowEventEmitter;


    @FXML
    private MenuItem newMenuItem;
    @FXML
    private MenuItem openMenuItem;
    @FXML
    private MenuItem closeMenuItem;

    @FXML
    private MenuItem importCsvMenuItem;
    @FXML
    private MenuItem exportCsvMenuItem;


    private void openFile(boolean newFile) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        appProperty.getApplicationFileDescription(),
                        "*." + appProperty.getApplicationFileExtension()
                )
        );

        File file = newFile ?
                fileChooser.showSaveDialog(primaryStage.getStage()) :
                fileChooser.showOpenDialog(primaryStage.getStage());

        if (file != null) {
            OpenFileTaskBuilder builder = taskBuilder.getBuilder(OpenFileTaskBuilder.class);
            builder.setFilePath(file.toPath());
            builder.setNewFile(newFile);

            builder.addRunningListener(this::setCursorWait);
            builder.addSucceededListener(v -> updateOpenFile(true));
            builder.addFailedListener(v -> updateOpenFile(false));

            builder.buildAndRun();
        }
    }


    private void setCursorWait(boolean wait) {
        primaryStage
                .getStage()
                .getScene()
                .setCursor(wait ? Cursor.WAIT : Cursor.DEFAULT);
    }

    /**
     * Обновление сведений об открытии файла с данными
     *
     * @param open true - файл открыт, false - файл закрыт
     */
    private void updateOpenFile(boolean open) {
        if (open) {
            sceneSwitcher.switchSceneTo(OpenFileApplicationScene.class);
            dataAccessEventEmitter.fileOpenEmin();

        } else {
            sceneSwitcher.switchSceneTo(CloseFileApplicationScene.class);
            closeWindowEventEmitter.closeWindowEvent();
            dataAccessEventEmitter.fileCloseEmit();
        }

        newMenuItem.setDisable(open);
        openMenuItem.setDisable(open);
        closeMenuItem.setDisable(!open);
        importCsvMenuItem.setDisable(!open);
        exportCsvMenuItem.setDisable(!open);
    }


    @FXML
    public void newFileAction(ActionEvent actionEvent) {
        openFile(true);
    }

    @FXML
    public void openFileAction(ActionEvent actionEvent) {
        openFile(false);
    }

    @FXML
    public void closeFileAction(ActionEvent actionEvent) {
        CloseFileTaskBuilder builder = taskBuilder.getBuilder(CloseFileTaskBuilder.class);
        builder.addRunningListener(this::setCursorWait);
        builder.addSucceededListener(v -> updateOpenFile(false));
        builder.buildAndRun();
    }

    @FXML
    public void importFromFileAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Файлы CSV (*.csv)", "*.csv")
        );
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Файлы Home Bank (*.xhb)", "*.xhb")
        );

        File file = fileChooser.showOpenDialog(primaryStage.getStage());

        if (file != null) {
            String fileName = file.getName().toLowerCase(Locale.ROOT);

            if (fileName.endsWith(".xhb")) {
                ImportFromXhbTaskBuilder builder = taskBuilder.getBuilder(ImportFromXhbTaskBuilder.class);
                builder.setFilePath(file.toPath());
                builder.addRunningListener(this::setCursorWait);
                builder.buildAndRun();

            } else {
                ImportFromCsvTaskBuilder builder = taskBuilder.getBuilder(ImportFromCsvTaskBuilder.class);
                builder.setFilePath(file.toPath());
                builder.addRunningListener(this::setCursorWait);
                builder.buildAndRun();
            }
        }
    }

    public void exportCsvFileAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Файлы CSV (*.csv)", "*.csv")
        );

        File file = fileChooser.showSaveDialog(primaryStage.getStage());

        if (file != null) {
            ExportToCsvTaskBuilder builder = taskBuilder.getBuilder(ExportToCsvTaskBuilder.class);
            builder.setFilePath(file.toPath());
            builder.addRunningListener(this::setCursorWait);
            builder.buildAndRun();
        }
    }

    @FXML
    public void exitAction(ActionEvent actionEvent) {
        Platform.exit();
    }
}