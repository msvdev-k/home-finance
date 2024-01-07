package ru.msvdev.homefinance.widget.table.expense.columnbuilder;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import ru.msvdev.desktop.utils.widget.datatable.BaseColumnBuilder;
import ru.msvdev.desktop.utils.widget.datatable.cell.model.StringCellModel;
import ru.msvdev.desktop.utils.widget.datatable.cell.table.StringTableCell;
import ru.msvdev.homefinance.widget.table.expense.ExpenseRowModel;


public class NoteColumnBuilder extends BaseColumnBuilder<ExpenseRowModel, StringCellModel> {

    public NoteColumnBuilder() {
        setName("Примечание");
        setPrefWidth(285);
        setEditable(true);
    }

    @Override
    protected void modelValueSetter(ExpenseRowModel expenseRowModel, StringCellModel stringCellModel) {
        expenseRowModel.setNote(stringCellModel.getValue());
    }

    @Override
    protected ObservableValue<StringCellModel> cellValueFactory(TableColumn.CellDataFeatures<ExpenseRowModel, StringCellModel> cellDataFeatures) {
        return cellDataFeatures.getValue().noteProperty();
    }

    @Override
    protected TableCell<ExpenseRowModel, StringCellModel> cellFactory(TableColumn<ExpenseRowModel, StringCellModel> tableColumn) {
        return new StringTableCell<>();
    }
}
