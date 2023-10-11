package ru.msvdev.homefinance.widget.table.expense.valuefactory;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import ru.msvdev.homefinance.viewutils.table.cell.BooleanCellModel;
import ru.msvdev.homefinance.widget.table.expense.ExpenseRowModel;


public class CheckCellValueFactory implements Callback<TableColumn.CellDataFeatures<ExpenseRowModel, BooleanCellModel>, ObservableValue<BooleanCellModel>> {
    @Override
    public ObservableValue<BooleanCellModel> call(TableColumn.CellDataFeatures<ExpenseRowModel, BooleanCellModel> param) {
        return param.getValue().checkProperty();
    }
}
