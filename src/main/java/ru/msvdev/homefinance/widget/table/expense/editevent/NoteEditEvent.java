package ru.msvdev.homefinance.widget.table.expense.editevent;

import ru.msvdev.homefinance.viewutils.table.EditEvent;
import ru.msvdev.homefinance.viewutils.table.cell.StringCellModel;
import ru.msvdev.homefinance.widget.table.expense.ExpenseRowModel;


public class NoteEditEvent extends EditEvent<ExpenseRowModel, StringCellModel> {

    @Override
    protected void valueSetter(ExpenseRowModel rowModel, StringCellModel cellModel) {
        rowModel.setNote(cellModel.getValue());
    }
}
