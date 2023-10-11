package ru.msvdev.homefinance.widget.table.expense.valuefactory;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import ru.msvdev.homefinance.viewutils.table.cell.DateCellModel;
import ru.msvdev.homefinance.widget.table.expense.ExpenseRowModel;


public class DateCellValueFactory implements Callback<TableColumn.CellDataFeatures<ExpenseRowModel, DateCellModel>, ObservableValue<DateCellModel>> {
    @Override
    public ObservableValue<DateCellModel> call(TableColumn.CellDataFeatures<ExpenseRowModel, DateCellModel> param) {
        return param.getValue().dateProperty();
    }
}
