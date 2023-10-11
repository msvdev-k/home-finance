package ru.msvdev.homefinance.widget.table.expense.columnbuilder;

import ru.msvdev.homefinance.viewutils.table.BaseColumnBuilder;
import ru.msvdev.homefinance.viewutils.table.cell.DateCellModel;
import ru.msvdev.homefinance.viewutils.table.converter.DateStringConverter;
import ru.msvdev.homefinance.widget.table.expense.ExpenseRowModel;
import ru.msvdev.homefinance.widget.table.expense.editevent.DateEditEvent;
import ru.msvdev.homefinance.widget.table.expense.valuefactory.DateCellValueFactory;


public class DateColumnBuilder extends BaseColumnBuilder<ExpenseRowModel, DateCellModel> {

    public DateColumnBuilder() {
        setName("Дата");
        setPrefWidth(100);
        setEditable(true);
        setCellValueFactory(new DateCellValueFactory());
        setConverter(new DateStringConverter());
        setEditEvent(new DateEditEvent());
    }
}
