package ru.msvdev.homefinance.widget.table.expense.columnbuilder;

import ru.msvdev.homefinance.viewutils.table.BaseColumnBuilder;
import ru.msvdev.homefinance.viewutils.table.cell.StringCellModel;
import ru.msvdev.homefinance.viewutils.table.converter.StringStringConverter;
import ru.msvdev.homefinance.widget.table.expense.ExpenseRowModel;
import ru.msvdev.homefinance.widget.table.expense.editevent.CategoryEditEvent;
import ru.msvdev.homefinance.widget.table.expense.valuefactory.CategoryCellValueFactory;


public class CategoryColumnBuilder extends BaseColumnBuilder<ExpenseRowModel, StringCellModel> {

    public CategoryColumnBuilder() {
        setName("Категория");
        setPrefWidth(360);
        setEditable(true);
        setCellValueFactory(new CategoryCellValueFactory());
        setConverter(new StringStringConverter());
        setEditEvent(new CategoryEditEvent());
    }
}
