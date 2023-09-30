package ru.msvdev.homefinance.controller.utility.category.table.columnbuilder;

import ru.msvdev.homefinance.controller.utility.category.table.CategoryRowModel;
import ru.msvdev.homefinance.controller.utility.category.table.editevent.DescriptionEditEvent;
import ru.msvdev.homefinance.controller.utility.category.table.valuefactory.DescriptionCellValueFactory;
import ru.msvdev.homefinance.viewutils.table.BaseColumnBuilder;
import ru.msvdev.homefinance.viewutils.table.cell.StringCellModel;
import ru.msvdev.homefinance.viewutils.table.converter.StringStringConverter;


public class DescriptionColumnBuilder extends BaseColumnBuilder<CategoryRowModel, StringCellModel> {

    public DescriptionColumnBuilder() {
        setName("Описание");
        setPrefWidth(600);
        setCellValueFactory(new DescriptionCellValueFactory());
        setConverter(new StringStringConverter());
        setEditEvent(new DescriptionEditEvent());
    }
}
