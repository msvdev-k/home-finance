package ru.msvdev.homefinance.controller.utility.category.table.columnbuilder;

import ru.msvdev.homefinance.controller.utility.category.table.CategoryRowModel;
import ru.msvdev.homefinance.controller.utility.category.table.editevent.CategoryEditEvent;
import ru.msvdev.homefinance.controller.utility.category.table.valuefactory.CategoryCellValueFactory;
import ru.msvdev.homefinance.viewutils.table.BaseColumnBuilder;
import ru.msvdev.homefinance.viewutils.table.cell.StringCellModel;
import ru.msvdev.homefinance.viewutils.table.converter.StringStringConverter;


public class CategoryColumnBuilder extends BaseColumnBuilder<CategoryRowModel, StringCellModel> {

    public CategoryColumnBuilder() {
        setName("Название");
        setPrefWidth(400);
        setCellValueFactory(new CategoryCellValueFactory());
        setConverter(new StringStringConverter());
        setEditEvent(new CategoryEditEvent());
    }
}
