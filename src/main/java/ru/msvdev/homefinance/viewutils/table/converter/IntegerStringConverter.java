package ru.msvdev.homefinance.viewutils.table.converter;

import ru.msvdev.homefinance.viewutils.table.cell.CellModel;
import ru.msvdev.homefinance.viewutils.table.cell.IntegerCellModel;


public class IntegerStringConverter extends StringConverter<IntegerCellModel> {

    @Override
    public IntegerCellModel fromString(String string) {
        IntegerCellModel cellModel = new IntegerCellModel();

        if (string == null || string.trim().isEmpty()) {
            return cellModel;
        }

        try {
            Integer value = Integer.valueOf(string);
            cellModel.setValue(value);

        } catch (Exception e) {
            cellModel.setError(CellModel.CellError.NUMBER);
        }

        return cellModel;
    }
}
