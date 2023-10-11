package ru.msvdev.homefinance.widget.table.expense.columnbuilder;

import ru.msvdev.homefinance.viewutils.table.BaseColumnBuilder;
import ru.msvdev.homefinance.viewutils.table.cell.BooleanCellModel;
import ru.msvdev.homefinance.viewutils.table.converter.BooleanStringConverter;
import ru.msvdev.homefinance.widget.table.expense.ExpenseRowModel;
import ru.msvdev.homefinance.widget.table.expense.editevent.CheckEditEvent;
import ru.msvdev.homefinance.widget.table.expense.valuefactory.CheckCellValueFactory;


public class CheckColumnBuilder extends BaseColumnBuilder<ExpenseRowModel, BooleanCellModel> {

    public CheckColumnBuilder() {
        setName("Проверено");
        setPrefWidth(50);
        setEditable(false);
        setCellValueFactory(new CheckCellValueFactory());
        setConverter(new BooleanStringConverter());
        setEditEvent(new CheckEditEvent());
    }
}
