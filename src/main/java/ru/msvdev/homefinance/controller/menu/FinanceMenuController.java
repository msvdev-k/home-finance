package ru.msvdev.homefinance.controller.menu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import ru.msvdev.homefinance.controller.DataAccessListener;
import ru.msvdev.homefinance.controller.utility.category.CategoryUtilityController;
import ru.msvdev.homefinance.task.operation.TaskBuilder;
import ru.msvdev.homefinance.window.MainAppStage;

import java.io.IOException;


@Controller
@RequiredArgsConstructor
public class FinanceMenuController implements DataAccessListener {


    private final CategoryUtilityController categoryUtilityController;
    private final ApplicationContext ctx;
    private final MainAppStage mainAppStage;
    private final TaskBuilder taskBuilder;

    @FXML
    private Menu financeMenu;


    @Override
    public void dataAccessUpdate(boolean access) {
        financeMenu.setVisible(access);
    }


    @FXML
    public void addExpensesAction(ActionEvent actionEvent) {

    }

    @FXML
    public void expensesListAction(ActionEvent actionEvent) {

    }

    public void statisticsAction(ActionEvent actionEvent) {

    }

    public void categoryManagementAction(ActionEvent actionEvent) throws IOException {
        categoryUtilityController.show();
    }
}