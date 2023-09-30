package ru.msvdev.homefinance.viewutils.table.converter;

import ru.msvdev.homefinance.viewutils.table.cell.CellModel;
import ru.msvdev.homefinance.viewutils.table.cell.DateCellModel;

import java.time.LocalDate;


public class DateStringConverter extends StringConverter<DateCellModel> {

    @Override
    public DateCellModel fromString(String string) {
        DateCellModel cellModel = new DateCellModel();

        if (string == null || string.trim().isEmpty()) {
            return cellModel;
        }

        try {
            LocalDate date = LocalDate.parse(string);
            cellModel.setValue(date);

        } catch (Exception e) {
            cellModel.setError(CellModel.CellError.DATE);
        }

        return cellModel;
    }
}
