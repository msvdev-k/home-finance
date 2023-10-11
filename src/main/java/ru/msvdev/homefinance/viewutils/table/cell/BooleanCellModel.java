package ru.msvdev.homefinance.viewutils.table.cell;


public class BooleanCellModel extends CellModel<Boolean> implements Cloneable {

    @Override
    public BooleanCellModel clone() {
        try {
            return (BooleanCellModel) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
