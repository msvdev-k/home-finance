package ru.msvdev.homefinance.widget.table.expense.columnbuilder;

import ru.msvdev.homefinance.viewutils.table.BaseColumnBuilder;
import ru.msvdev.homefinance.viewutils.table.cell.StringCellModel;
import ru.msvdev.homefinance.viewutils.table.converter.StringStringConverter;
import ru.msvdev.homefinance.widget.table.expense.ExpenseRowModel;
import ru.msvdev.homefinance.widget.table.expense.editevent.NoteEditEvent;
import ru.msvdev.homefinance.widget.table.expense.valuefactory.NoteCellValueFactory;


public class NoteColumnBuilder extends BaseColumnBuilder<ExpenseRowModel, StringCellModel> {

    public NoteColumnBuilder() {
        setName("Примечание");
        setPrefWidth(285);
        setEditable(true);
        setCellValueFactory(new NoteCellValueFactory());
        setConverter(new StringStringConverter());
        setEditEvent(new NoteEditEvent());
    }
}
