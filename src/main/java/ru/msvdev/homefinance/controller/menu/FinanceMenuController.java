package ru.msvdev.homefinance.controller.menu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import ru.msvdev.homefinance.controller.utility.category.CategoryUtilityController;
import ru.msvdev.homefinance.controller.utility.expenses.AddExpensesUtilityController;
import ru.msvdev.homefinance.controller.utility.expenses.ExpensesUtilityController;

import java.io.IOException;


@Controller
@RequiredArgsConstructor
public class FinanceMenuController {

    private final CategoryUtilityController categoryUtilityController;
    private final AddExpensesUtilityController addExpensesUtilityController;
    private final ExpensesUtilityController expensesUtilityController;

    @FXML
    private Menu financeMenu;


    @FXML
    public void addExpensesAction(ActionEvent actionEvent) throws IOException {
        addExpensesUtilityController.show();
    }

    @FXML
    public void expensesListAction(ActionEvent actionEvent) throws IOException {
        expensesUtilityController.show();
    }

    public void statisticsAction(ActionEvent actionEvent) {

    }

    public void categoryManagementAction(ActionEvent actionEvent) throws IOException {
        categoryUtilityController.show();
    }
}