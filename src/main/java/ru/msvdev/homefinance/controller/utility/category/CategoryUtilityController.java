package ru.msvdev.homefinance.controller.utility.category;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import ru.msvdev.homefinance.controller.ShowUtilityWindow;
import ru.msvdev.homefinance.widget.table.category.CategoryRowModel;
import ru.msvdev.homefinance.widget.table.category.CategoryTableController;
import ru.msvdev.homefinance.task.operation.TaskBuilder;
import ru.msvdev.homefinance.window.MainAppStage;
import ru.msvdev.homefinance.window.SceneLoader;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


@Controller
@RequiredArgsConstructor
public class CategoryUtilityController implements Initializable, ShowUtilityWindow {

    private final MainAppStage mainAppStage;
    private final SceneLoader sceneLoader;
    private final TaskBuilder taskBuilder;

    private CategoryTableController tableController;
    private Stage stage;


    @FXML
    private HBox buttonsBox;
    @FXML
    public TableView<CategoryRowModel> categoryTable;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (tableController == null) {
            tableController = new CategoryTableController(taskBuilder);
        }

        tableController.setTableView(categoryTable);
        tableController.initTable();
        tableController.refresh();
    }


    @Override
    public void show() throws IOException {
        if (stage != null && stage.isShowing()) return;

        Scene scene = sceneLoader.load("/view/utility/category-utility-view.fxml");

        stage = new Stage(StageStyle.UTILITY);
        stage.initOwner(mainAppStage.getStage());
        stage.setTitle("Управление категориями расходов");
        stage.setScene(scene);

        stage.show();
    }

    @Override
    public void close() {
        if (stage != null) {
            stage.close();
            stage = null;
        }
    }


    @FXML
    public void addItem(ActionEvent actionEvent) {
        tableController.addNewRow();
    }

    @FXML
    public void deleteItem(ActionEvent actionEvent) {
        tableController.removeSelected();
    }
}