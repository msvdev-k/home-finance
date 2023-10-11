package ru.msvdev.homefinance.viewutils.table.cell;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;


@Getter
@Setter
public class StringCellModel extends CellModel<String> implements Cloneable {

    /**
     * Множество допустимых значения
     */
    private Set<String> validValues;

    @Override
    public StringCellModel clone() {
        try {
            return (StringCellModel) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
