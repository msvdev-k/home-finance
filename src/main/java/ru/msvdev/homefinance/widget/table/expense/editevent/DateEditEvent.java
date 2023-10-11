package ru.msvdev.homefinance.widget.table.expense.editevent;

import ru.msvdev.homefinance.viewutils.table.EditEvent;
import ru.msvdev.homefinance.viewutils.table.cell.DateCellModel;
import ru.msvdev.homefinance.widget.table.expense.ExpenseRowModel;


public class DateEditEvent extends EditEvent<ExpenseRowModel, DateCellModel> {

    @Override
    protected void valueSetter(ExpenseRowModel rowModel, DateCellModel cellModel) {
        rowModel.setDate(cellModel.getValue());
    }
}
