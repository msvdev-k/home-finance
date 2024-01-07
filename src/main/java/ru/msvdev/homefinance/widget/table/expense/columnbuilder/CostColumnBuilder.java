package ru.msvdev.homefinance.widget.table.expense.columnbuilder;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import ru.msvdev.desktop.utils.widget.datatable.BaseColumnBuilder;
import ru.msvdev.desktop.utils.widget.datatable.cell.model.MoneyCellModel;
import ru.msvdev.desktop.utils.widget.datatable.cell.table.MoneyTableCell;
import ru.msvdev.homefinance.widget.table.expense.ExpenseRowModel;


public class CostColumnBuilder extends BaseColumnBuilder<ExpenseRowModel, MoneyCellModel> {

    public CostColumnBuilder() {
        setName("Стоимость");
        setPrefWidth(90);
        setEditable(true);
    }

    @Override
    protected void modelValueSetter(ExpenseRowModel expenseRowModel, MoneyCellModel moneyCellModel) {
        expenseRowModel.setCost(moneyCellModel.getValue());
    }

    @Override
    protected ObservableValue<MoneyCellModel> cellValueFactory(TableColumn.CellDataFeatures<ExpenseRowModel, MoneyCellModel> cellDataFeatures) {
        return cellDataFeatures.getValue().costProperty();
    }

    @Override
    protected TableCell<ExpenseRowModel, MoneyCellModel> cellFactory(TableColumn<ExpenseRowModel, MoneyCellModel> tableColumn) {
        return new MoneyTableCell<>();
    }
}
