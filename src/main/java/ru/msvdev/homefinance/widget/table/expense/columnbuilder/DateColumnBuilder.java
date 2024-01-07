package ru.msvdev.homefinance.widget.table.expense.columnbuilder;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import ru.msvdev.desktop.utils.widget.datatable.BaseColumnBuilder;
import ru.msvdev.desktop.utils.widget.datatable.cell.model.DateCellModel;
import ru.msvdev.desktop.utils.widget.datatable.cell.table.DateTableCell;
import ru.msvdev.homefinance.widget.table.expense.ExpenseRowModel;


public class DateColumnBuilder extends BaseColumnBuilder<ExpenseRowModel, DateCellModel> {

    public DateColumnBuilder() {
        setName("Дата");
        setPrefWidth(80);
        setEditable(true);
    }

    @Override
    protected void modelValueSetter(ExpenseRowModel expenseRowModel, DateCellModel dateCellModel) {
        expenseRowModel.setDate(dateCellModel.getValue());
    }

    @Override
    protected ObservableValue<DateCellModel> cellValueFactory(TableColumn.CellDataFeatures<ExpenseRowModel, DateCellModel> cellDataFeatures) {
        return cellDataFeatures.getValue().dateProperty();
    }

    @Override
    protected TableCell<ExpenseRowModel, DateCellModel> cellFactory(TableColumn<ExpenseRowModel, DateCellModel> tableColumn) {
        return new DateTableCell<>();
    }
}
