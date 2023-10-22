package ru.msvdev.homefinance.controller.utility.expenses;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import ru.msvdev.homefinance.controller.ShowUtilityWindow;
import ru.msvdev.homefinance.task.operation.TaskBuilder;
import ru.msvdev.homefinance.widget.table.expense.ExpenseRowModel;
import ru.msvdev.homefinance.widget.table.expense.ExpenseTableController;
import ru.msvdev.homefinance.window.MainAppStage;
import ru.msvdev.homefinance.window.SceneLoader;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;


@Controller
@RequiredArgsConstructor
public class ExpensesUtilityController implements Initializable, ShowUtilityWindow {

    private static final String STATISTIC_LABEL_TEMPLATE = "Выделено %d записей на сумму %.2f рублей";

    private final MainAppStage mainAppStage;
    private final SceneLoader sceneLoader;
    private final TaskBuilder taskBuilder;

    private ExpenseTableController tableController;
    private Stage stage;


    @FXML
    public Button deleteButton;
    @FXML
    public Label statistic;
    @FXML
    public TableView<ExpenseRowModel> expensesTable;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (tableController == null) {
            tableController = new ExpenseTableController(taskBuilder);
            tableController.setStatisticListener(this::updateStatisticLabel);
        }

        tableController.setTableView(expensesTable);
        tableController.initTable();
    }


    @Override
    public void show() throws IOException {
        if (stage != null && stage.isShowing()) return;

        Scene scene = sceneLoader.load("/view/utility/expenses-utility-view.fxml");

        stage = new Stage(StageStyle.UTILITY);
        stage.initOwner(mainAppStage.getStage());
        stage.setTitle("Список расходов");
        stage.setScene(scene);

        expensesTable.requestFocus();

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
    public void deleteItem(ActionEvent actionEvent) {
        tableController.removeSelected();
    }

    private void updateStatisticLabel(Integer count, BigDecimal cost) {
        statistic.setText(String.format(STATISTIC_LABEL_TEMPLATE, count, cost));
    }
}