package ru.msvdev.homefinance.viewutils.table.converter;

import ru.msvdev.homefinance.viewutils.table.cell.StringCellModel;


public class StringStringConverter extends StringConverter<StringCellModel> {

    @Override
    public StringCellModel fromString(String string) {
        StringCellModel cellModel = new StringCellModel();

        if (string != null && !string.trim().isEmpty()) {
            cellModel.setValue(string);
        }

        return cellModel;
    }
}
