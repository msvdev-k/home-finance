package ru.msvdev.homefinance.widget.table.expense.valuefactory;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import ru.msvdev.homefinance.viewutils.table.cell.MoneyCellModel;
import ru.msvdev.homefinance.widget.table.expense.ExpenseRowModel;


public class CostCellValueFactory implements Callback<TableColumn.CellDataFeatures<ExpenseRowModel, MoneyCellModel>, ObservableValue<MoneyCellModel>> {
    @Override
    public ObservableValue<MoneyCellModel> call(TableColumn.CellDataFeatures<ExpenseRowModel, MoneyCellModel> param) {
        return param.getValue().costProperty();
    }
}
