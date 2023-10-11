package ru.msvdev.homefinance.widget.table.expense.valuefactory;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import ru.msvdev.homefinance.viewutils.table.cell.StringCellModel;
import ru.msvdev.homefinance.widget.table.expense.ExpenseRowModel;


public class CategoryCellValueFactory implements Callback<TableColumn.CellDataFeatures<ExpenseRowModel, StringCellModel>, ObservableValue<StringCellModel>> {
    @Override
    public ObservableValue<StringCellModel> call(TableColumn.CellDataFeatures<ExpenseRowModel, StringCellModel> param) {
        return param.getValue().categoryProperty();
    }
}
