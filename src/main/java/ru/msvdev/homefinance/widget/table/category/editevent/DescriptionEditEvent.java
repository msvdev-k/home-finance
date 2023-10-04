package ru.msvdev.homefinance.widget.table.category.editevent;

import ru.msvdev.homefinance.widget.table.category.CategoryRowModel;
import ru.msvdev.homefinance.viewutils.table.EditEvent;
import ru.msvdev.homefinance.viewutils.table.cell.StringCellModel;


public class DescriptionEditEvent extends EditEvent<CategoryRowModel, StringCellModel> {

    @Override
    protected void valueSetter(CategoryRowModel categoryRowModel, StringCellModel cellModel) {
        categoryRowModel.setDescription(cellModel.getValue());
    }
}
