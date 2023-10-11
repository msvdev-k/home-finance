package ru.msvdev.homefinance.viewutils.table;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import lombok.Setter;
import ru.msvdev.homefinance.viewutils.table.cell.CellModel;
import ru.msvdev.homefinance.viewutils.table.converter.StringConverter;


@Setter
public class BaseColumnBuilder<S, T extends CellModel<?>> {

    private String name;
    private double prefWidth;
    private boolean editable;
    private Callback<TableColumn.CellDataFeatures<S, T>, ObservableValue<T>> cellValueFactory;
    private StringConverter<T> converter;
    private EditEvent<S, T> editEvent;


    public TableColumn<S, T> build() {
        TableColumn<S, T> column = new TableColumn<>(name);
        column.setPrefWidth(prefWidth);
        column.setEditable(editable);

        column.setCellValueFactory(cellValueFactory);

        BaseCellFactory<S, T> baseCellFactory = new BaseCellFactory<>(converter);
        column.setCellFactory(baseCellFactory);

        column.setOnEditCommit(editEvent);
        column.setOnEditCancel(editEvent);

        return column;
    }

}
