package ru.msvdev.homefinance.viewutils.table;

import javafx.event.EventHandler;
import javafx.scene.control.TableColumn;
import ru.msvdev.homefinance.viewutils.table.cell.CellModel;


public abstract class EditEvent<S, T extends CellModel<?>> implements EventHandler<TableColumn.CellEditEvent<S, T>> {

    @Override
    public void handle(TableColumn.CellEditEvent<S, T> event) {
        T newCellModel = event.getNewValue();

        if (newCellModel != null) {
            valueSetter(event.getRowValue(), newCellModel);
        } else {
            event.getTableView().refresh();
        }

        event.getTableView().requestFocus();
    }

    protected abstract void valueSetter(S rowModel, T cellModel);
}
