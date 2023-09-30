package ru.msvdev.homefinance.viewutils.table.converter;

import ru.msvdev.homefinance.viewutils.table.cell.CellModel;


public abstract class StringConverter<T extends CellModel<?>> {

    public String toString(T cellModel) {
        Object value = cellModel.getValue();
        return value == null ? "" : value.toString();
    }

    public abstract T fromString(String string);
}
