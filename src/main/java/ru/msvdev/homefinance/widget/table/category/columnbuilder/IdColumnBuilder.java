package ru.msvdev.homefinance.widget.table.category.columnbuilder;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import lombok.Setter;
import ru.msvdev.desktop.utils.widget.datatable.BaseIdColumnBuilder;
import ru.msvdev.homefinance.widget.table.category.CategoryRowModel;


@Setter
public class IdColumnBuilder extends BaseIdColumnBuilder<CategoryRowModel, Integer> {

    public IdColumnBuilder() {
        setName("ID");
        setPrefWidth(32);
    }

    @Override
    protected ObservableValue<Integer> cellValueFactory(TableColumn.CellDataFeatures<CategoryRowModel, Integer> cellDataFeatures) {
        return cellDataFeatures.getValue().idProperty();
    }

}
