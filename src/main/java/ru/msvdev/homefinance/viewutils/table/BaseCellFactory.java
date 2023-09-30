package ru.msvdev.homefinance.viewutils.table;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import ru.msvdev.homefinance.viewutils.table.cell.CellModel;
import ru.msvdev.homefinance.viewutils.table.converter.StringConverter;


public class BaseCellFactory<S, T extends CellModel<?>> implements Callback<TableColumn<S, T>, TableCell<S, T>> {

    private final StringConverter<T> converter;

    public BaseCellFactory(StringConverter<T> converter) {
        this.converter = converter;
    }

    @Override
    public TableCell<S, T> call(TableColumn<S, T> param) {
        return new BaseTableCell<>(converter);
    }
}
