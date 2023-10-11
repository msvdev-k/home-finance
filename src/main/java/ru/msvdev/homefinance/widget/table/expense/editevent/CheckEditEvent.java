package ru.msvdev.homefinance.widget.table.expense.editevent;

import ru.msvdev.homefinance.viewutils.table.EditEvent;
import ru.msvdev.homefinance.viewutils.table.cell.BooleanCellModel;
import ru.msvdev.homefinance.widget.table.expense.ExpenseRowModel;


public class CheckEditEvent extends EditEvent<ExpenseRowModel, BooleanCellModel> {

    @Override
    protected void valueSetter(ExpenseRowModel rowModel, BooleanCellModel cellModel) {
        rowModel.setCheck(cellModel.getValue());
    }
}
