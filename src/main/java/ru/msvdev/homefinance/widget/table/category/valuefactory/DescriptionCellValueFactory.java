package ru.msvdev.homefinance.widget.table.category.valuefactory;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import ru.msvdev.homefinance.widget.table.category.CategoryRowModel;
import ru.msvdev.homefinance.viewutils.table.cell.StringCellModel;


public class DescriptionCellValueFactory implements Callback<TableColumn.CellDataFeatures<CategoryRowModel, StringCellModel>, ObservableValue<StringCellModel>> {
    @Override
    public ObservableValue<StringCellModel> call(TableColumn.CellDataFeatures<CategoryRowModel, StringCellModel> param) {
        return param.getValue().descriptionProperty();
    }
}
