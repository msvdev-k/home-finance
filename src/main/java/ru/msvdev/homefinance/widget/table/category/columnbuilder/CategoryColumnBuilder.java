package ru.msvdev.homefinance.widget.table.category.columnbuilder;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import ru.msvdev.desktop.utils.widget.datatable.BaseColumnBuilder;
import ru.msvdev.desktop.utils.widget.datatable.cell.model.StringCellModel;
import ru.msvdev.desktop.utils.widget.datatable.cell.table.StringTableCell;
import ru.msvdev.homefinance.widget.table.category.CategoryRowModel;


public class CategoryColumnBuilder extends BaseColumnBuilder<CategoryRowModel, StringCellModel> {

    public CategoryColumnBuilder() {
        setName("Название");
        setPrefWidth(400);
        setEditable(true);
    }

    @Override
    protected void modelValueSetter(CategoryRowModel categoryRowModel, StringCellModel stringCellModel) {
        categoryRowModel.setCategory(stringCellModel.getValue());
    }

    @Override
    protected ObservableValue<StringCellModel> cellValueFactory(TableColumn.CellDataFeatures<CategoryRowModel, StringCellModel> cellDataFeatures) {
        return cellDataFeatures.getValue().categoryProperty();
    }

    @Override
    protected TableCell<CategoryRowModel, StringCellModel> cellFactory(TableColumn<CategoryRowModel, StringCellModel> tableColumn) {
        return new StringTableCell<>();
    }
}
