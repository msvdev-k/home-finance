package ru.msvdev.homefinance.viewutils.table.cell;


public class StringCellModel extends CellModel<String> implements Cloneable {

    @Override
    public StringCellModel clone() {
        try {
            return (StringCellModel) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
