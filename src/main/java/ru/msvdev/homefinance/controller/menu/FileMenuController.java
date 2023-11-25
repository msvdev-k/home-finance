package ru.msvdev.homefinance.controller.menu;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import ru.msvdev.homefinance.config.AppProperty;
import ru.msvdev.homefinance.controller.DataAccessListener;
import ru.msvdev.homefinance.controller.ShowUtilityWindow;
import ru.msvdev.homefinance.task.data.file.CloseFileTaskBuilder;
import ru.msvdev.homefinance.task.data.file.OpenFileTaskBuilder;
import ru.msvdev.homefinance.task.data.io.csv.ExportToCsvTaskBuilder;
import ru.msvdev.homefinance.task.data.io.csv.ImportFromCsvTaskBuilder;
import ru.msvdev.homefinance.task.operation.TaskBuilder;
import ru.msvdev.homefinance.window.MainAppStage;

import java.io.File;
import java.util.List;


@Controller
@RequiredArgsConstructor
public class FileMenuController {

    private final MainAppStage mainAppStage;
    private final TaskBuilder taskBuilder;

    private final AppProperty appProperty;

    private final List<DataAccessListener> dataAccessListeners;
    private final List<ShowUtilityWindow> showUtilityWindows;

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
                fileChooser.showSaveDialog(mainAppStage.getStage()) :
                fileChooser.showOpenDialog(mainAppStage.getStage());

        if (file != null) {
            OpenFileTaskBuilder builder = taskBuilder.getBuilder(OpenFileTaskBuilder.class);
            builder.setFilePath(file.toPath());
            builder.setNewFile(newFile);

            builder.addRunningListener(this::setCursorWait);
            builder.addSucceededListener(v -> updateOpenFile(true));
            builder.addFailedListener(v -> updateOpenFile(false));
//            builder.addFailedListener(Throwable::printStackTrace);

            builder.buildAndRun();
        }
    }


    private void setCursorWait(boolean wait) {
        mainAppStage
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
        newMenuItem.setDisable(open);
        openMenuItem.setDisable(open);
        closeMenuItem.setDisable(!open);
        importCsvMenuItem.setDisable(!open);
        exportCsvMenuItem.setDisable(!open);

        if (!open) showUtilityWindows.forEach(ShowUtilityWindow::close);
        dataAccessListeners.forEach(listener ->
                listener.dataAccessUpdate(open));
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
    public void importCsvFileAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Файлы CSV (*.csv)", "*.csv")
        );

        File file = fileChooser.showOpenDialog(mainAppStage.getStage());

        if (file != null) {
            ImportFromCsvTaskBuilder builder = taskBuilder.getBuilder(ImportFromCsvTaskBuilder.class);
            builder.setFilePath(file.toPath());
            builder.addRunningListener(this::setCursorWait);
//            builder.addFailedListener(Throwable::printStackTrace);
            builder.buildAndRun();
        }
    }

    public void exportCsvFileAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Файлы CSV (*.csv)", "*.csv")
        );

        File file = fileChooser.showSaveDialog(mainAppStage.getStage());

        if (file != null) {
            ExportToCsvTaskBuilder builder = taskBuilder.getBuilder(ExportToCsvTaskBuilder.class);
            builder.setFilePath(file.toPath());
            builder.addRunningListener(this::setCursorWait);
//            builder.addFailedListener(Throwable::printStackTrace);
            builder.buildAndRun();
        }
    }

    @FXML
    public void exitAction(ActionEvent actionEvent) {
        Platform.exit();
    }

}