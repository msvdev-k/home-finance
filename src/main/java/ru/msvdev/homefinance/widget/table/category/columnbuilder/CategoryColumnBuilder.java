package ru.msvdev.homefinance.widget.table.category.columnbuilder;

import ru.msvdev.homefinance.widget.table.category.CategoryRowModel;
import ru.msvdev.homefinance.widget.table.category.editevent.CategoryEditEvent;
import ru.msvdev.homefinance.widget.table.category.valuefactory.CategoryCellValueFactory;
import ru.msvdev.homefinance.viewutils.table.BaseColumnBuilder;
import ru.msvdev.homefinance.viewutils.table.cell.StringCellModel;
import ru.msvdev.homefinance.viewutils.table.converter.StringStringConverter;


public class CategoryColumnBuilder extends BaseColumnBuilder<CategoryRowModel, StringCellModel> {

    public CategoryColumnBuilder() {
        setName("Название");
        setPrefWidth(400);
        setEditable(true);
        setCellValueFactory(new CategoryCellValueFactory());
        setConverter(new StringStringConverter());
        setEditEvent(new CategoryEditEvent());
    }
}
