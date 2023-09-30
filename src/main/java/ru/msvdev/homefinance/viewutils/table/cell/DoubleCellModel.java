package ru.msvdev.homefinance.viewutils.table.cell;


public class DoubleCellModel extends CellModel<Double> implements Cloneable {

    @Override
    public DoubleCellModel clone() {
        try {
            return (DoubleCellModel) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
