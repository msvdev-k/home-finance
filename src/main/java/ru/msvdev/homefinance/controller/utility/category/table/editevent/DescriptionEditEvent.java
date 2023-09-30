package ru.msvdev.homefinance.controller.utility.category.table.editevent;

import ru.msvdev.homefinance.controller.utility.category.table.CategoryRowModel;
import ru.msvdev.homefinance.viewutils.table.EditEvent;
import ru.msvdev.homefinance.viewutils.table.cell.StringCellModel;


public class DescriptionEditEvent extends EditEvent<CategoryRowModel, StringCellModel> {

    @Override
    protected void valueSetter(CategoryRowModel categoryRowModel, StringCellModel cellModel) {
        categoryRowModel.setDescription(cellModel);
    }
}
