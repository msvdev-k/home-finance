package ru.msvdev.homefinance.viewutils.table.converter;

import ru.msvdev.homefinance.viewutils.table.cell.MoneyCellModel;

import java.math.BigDecimal;


public class MoneyStringConverter extends StringConverter<MoneyCellModel> {

    @Override
    public String toString(MoneyCellModel cellModel) {
        BigDecimal value = cellModel.getValue();
        if (value == null) {
            return "";
        }

        return String.format("%s %s", value, cellModel.getSign());
    }

    @Override
    public MoneyCellModel fromString(String string) {
        MoneyCellModel cellModel = new MoneyCellModel();

        if (string == null || string.trim().isEmpty()) {
            return cellModel;
        }

        try {
            BigDecimal value = new BigDecimal(string);
            if (value.compareTo(BigDecimal.ZERO) >= 0) {
                cellModel.setValue(value);
            }

        } catch (Exception ignored) {
        }

        return cellModel;
    }
}
