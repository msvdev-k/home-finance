package ru.msvdev.homefinance.viewutils.table.converter;

import ru.msvdev.homefinance.viewutils.table.cell.CellModel;
import ru.msvdev.homefinance.viewutils.table.cell.DoubleCellModel;


public class DoubleStringConverter extends StringConverter<DoubleCellModel> {

    @Override
    public DoubleCellModel fromString(String string) {
        DoubleCellModel cellModel = new DoubleCellModel();

        if (string == null || string.trim().isEmpty()) {
            return cellModel;
        }

        try {
            Double value = Double.valueOf(string);
            cellModel.setValue(value);

        } catch (Exception e) {
            cellModel.setError(CellModel.CellError.NUMBER);
        }

        return cellModel;
    }
}
