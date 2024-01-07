package ru.msvdev.homefinance.widget.table.expense.columnbuilder;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import lombok.Setter;
import ru.msvdev.desktop.utils.widget.datatable.BaseIdColumnBuilder;
import ru.msvdev.homefinance.widget.table.expense.ExpenseRowModel;


@Setter
public class IdColumnBuilder extends BaseIdColumnBuilder<ExpenseRowModel, Integer> {

    public IdColumnBuilder() {
        setName("ID");
        setPrefWidth(32);
    }

    @Override
    protected ObservableValue<Integer> cellValueFactory(TableColumn.CellDataFeatures<ExpenseRowModel, Integer> cellDataFeatures) {
        return cellDataFeatures.getValue().idProperty();
    }

}
