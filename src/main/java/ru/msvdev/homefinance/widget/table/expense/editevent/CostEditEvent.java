package ru.msvdev.homefinance.widget.table.expense.editevent;

import ru.msvdev.homefinance.viewutils.table.EditEvent;
import ru.msvdev.homefinance.viewutils.table.cell.MoneyCellModel;
import ru.msvdev.homefinance.widget.table.expense.ExpenseRowModel;


public class CostEditEvent extends EditEvent<ExpenseRowModel, MoneyCellModel> {

    @Override
    protected void valueSetter(ExpenseRowModel rowModel, MoneyCellModel cellModel) {
        rowModel.setCost(cellModel.getValue());
    }
}
