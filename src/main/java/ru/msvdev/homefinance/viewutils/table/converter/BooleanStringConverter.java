package ru.msvdev.homefinance.viewutils.table.converter;

import ru.msvdev.homefinance.viewutils.table.cell.BooleanCellModel;


public class BooleanStringConverter extends StringConverter<BooleanCellModel> {

    @Override
    public String toString(BooleanCellModel cellModel) {
        Boolean value = cellModel.getValue();
        return value != null && value ? "TRUE" : "";
    }

    @Override
    public BooleanCellModel fromString(String string) {
        BooleanCellModel cellModel = new BooleanCellModel();

        if (string == null || string.trim().isEmpty()) {
            return cellModel;
        }

        try {
            Boolean value = Boolean.valueOf(string);
            cellModel.setValue(value);

        } catch (Exception ignored) {
        }

        return cellModel;
    }
}
