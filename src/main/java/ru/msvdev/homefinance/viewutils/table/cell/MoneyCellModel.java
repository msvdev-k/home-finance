package ru.msvdev.homefinance.viewutils.table.cell;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
public class MoneyCellModel extends CellModel<BigDecimal> implements Cloneable {

    public static final String RUBLE_SIGN = "₽";// \u20BD

    private String sign = "";

    @Override
    public MoneyCellModel clone() {
        try {
            return (MoneyCellModel) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
