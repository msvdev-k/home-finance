package ru.msvdev.homefinance.widget.table.expense.columnbuilder;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import ru.msvdev.desktop.utils.widget.datatable.BaseColumnBuilder;
import ru.msvdev.desktop.utils.widget.datatable.cell.model.BooleanCellModel;
import ru.msvdev.desktop.utils.widget.datatable.cell.table.BooleanTableCell;
import ru.msvdev.homefinance.widget.table.expense.ExpenseRowModel;


public class CheckColumnBuilder extends BaseColumnBuilder<ExpenseRowModel, BooleanCellModel> {

    public CheckColumnBuilder() {
        setName("Проверено");
        setPrefWidth(90);
        setEditable(false);
    }

    @Override
    protected void modelValueSetter(ExpenseRowModel expenseRowModel, BooleanCellModel booleanCellModel) {
        expenseRowModel.setCheck(booleanCellModel.getValue());
    }

    @Override
    protected ObservableValue<BooleanCellModel> cellValueFactory(TableColumn.CellDataFeatures<ExpenseRowModel, BooleanCellModel> cellDataFeatures) {
        return cellDataFeatures.getValue().checkProperty();
    }

    @Override
    protected TableCell<ExpenseRowModel, BooleanCellModel> cellFactory(TableColumn<ExpenseRowModel, BooleanCellModel> tableColumn) {
        return new BooleanTableCell<>();
    }
}
