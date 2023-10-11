package ru.msvdev.homefinance.controller.menu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import ru.msvdev.homefinance.controller.DataAccessListener;
import ru.msvdev.homefinance.controller.utility.category.CategoryUtilityController;
import ru.msvdev.homefinance.controller.utility.expenses.AddExpensesUtilityController;

import java.io.IOException;


@Controller
@RequiredArgsConstructor
public class FinanceMenuController implements DataAccessListener {


    private final CategoryUtilityController categoryUtilityController;
    private final AddExpensesUtilityController addExpensesUtilityController;

    @FXML
    private Menu financeMenu;


    @Override
    public void dataAccessUpdate(boolean access) {
        financeMenu.setVisible(access);
    }


    @FXML
    public void addExpensesAction(ActionEvent actionEvent) throws IOException {
        addExpensesUtilityController.show();
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